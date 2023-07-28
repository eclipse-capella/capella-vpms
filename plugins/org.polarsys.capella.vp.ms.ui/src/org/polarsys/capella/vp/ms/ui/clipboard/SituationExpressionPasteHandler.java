/*******************************************************************************
 * Copyright (c) 2024 THALES GLOBAL SERVICES and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.vp.ms.ui.clipboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.sirius.table.metamodel.table.DCell;
import org.eclipse.sirius.table.metamodel.table.DColumn;
import org.eclipse.sirius.table.metamodel.table.DLine;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.statushandlers.StatusManager;
import org.polarsys.capella.common.linkedtext.ui.LinkedTextDocument;
import org.polarsys.capella.common.linkedtext.ui.LinkedTextHyperlink;
import org.polarsys.capella.core.data.capellacommon.StateMachine;
import org.polarsys.capella.core.linkedtext.ui.CapellaEmbeddedLinkedTextEditorInput;
import org.polarsys.capella.vp.ms.Situation;
import org.polarsys.capella.vp.ms.expression.parser.DefaultMsExpressionVisitor;
import org.polarsys.capella.vp.ms.expression.parser.LinkedText2Situation;
import org.polarsys.capella.vp.ms.expression.parser.LinkedText2Situation.SplitExpression;
import org.polarsys.capella.vp.ms.expression.parser.MsExpressionUtil;
import org.polarsys.capella.vp.ms.ui.Activator;

/**
 * This Service implements 'paste' command in SituationExpressions.
 * 
 * @author ebausson
 */
public class SituationExpressionPasteHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    ISelection selection = HandlerUtil.getCurrentStructuredSelection(event);
    if (!selection.isEmpty() && selection instanceof IStructuredSelection && !SituationExpressionClipboardService.isEmpty()) {
      for (Object element : (IStructuredSelection) selection) {
        if (element instanceof DCell) {
          DCell targetCell = (DCell) element;
          LinkedText2Situation.SplitExpression expression = validateClipboardContent(targetCell);
          if (expression != null) {
            pasteContent(targetCell, expression);
          }
        }
      }
    }
    return null;
  }

  private LinkedText2Situation.SplitExpression validateClipboardContent(DCell targetCell) {
    if (targetCell != null && !SituationExpressionClipboardService.isEmpty()) {
      DLine row = targetCell.getLine();
      LinkedText2Situation.SplitExpression splitExpression = resolveSplitExpression(targetCell);
      String inputText = SituationExpressionClipboardService.getExpression();

      StateMachine rowSm = (StateMachine) row.getTarget();
      final AtomicReference<String> toParse = new AtomicReference<String>(inputText);
      CapellaEmbeddedLinkedTextEditorInput input = generateEditor(row, toParse, inputText);

      try {
        List<String> errors = new ArrayList<>();
        ParseTree tree = MsExpressionUtil.parse(toParse.get(), createErrorListener(errors));
        if (errors.isEmpty()) {
          splitExpression.put(rowSm, new DefaultMsExpressionVisitor(createResolver(LinkedTextDocument.load(input))).visit(tree));
        } else {
          StatusManager.getManager().handle(new Status(IStatus.ERROR, Activator.PLUGIN_ID, errors.get(0)), StatusManager.BLOCK);
          return null;
        }
      } catch (RecognitionException | NoSuchElementException e) {
        StatusManager.getManager().handle(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e), StatusManager.BLOCK);
        return null;
      }

      return splitExpression;
    }
    return null;
  }

  private void pasteContent(DCell targetCell, LinkedText2Situation.SplitExpression expression) {
    DColumn col = targetCell.getColumn();
    TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(col.getTarget()); 
    Command cmd = new RecordingCommand(domain) {
      @Override
      protected void doExecute() {
        ((Situation) col.getTarget()).setExpression(expression.merge());
      }
    };
    domain.getCommandStack().execute(cmd);

  }

  private CapellaEmbeddedLinkedTextEditorInput generateEditor(DLine row, AtomicReference<String> toParse, String inputText) {
    final String text = inputText;
    return new CapellaEmbeddedLinkedTextEditorInput(row.getTarget()) {
      @Override
      public String getText() {
        return text;
      }

      @Override
      public void setText(String linkedText) {
        toParse.set(linkedText);
      }
    };
    
  }

  private ANTLRErrorListener createErrorListener(Collection<String> errors) {
    return new BaseErrorListener() {
      @Override
      public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        errors.add("line " + line + ":" + charPositionInLine + " " + msg);
      }
    };
  }

  private Function<String,EObject> createResolver(LinkedTextDocument document){
    return new Function<String,EObject>() {
      @Override
      public EObject apply(String t) {
        for (LinkedTextHyperlink hl : document.getHyperlinks()) {
          if (t.equals(EcoreUtil.getID((EObject) hl.getTarget()))) {
            return (EObject) hl.getTarget();
          }
        }
        throw new NoSuchElementException(t);
      }
    };

  }

  private SplitExpression resolveSplitExpression(DCell cell) {
    return SplitExpression.of(((Situation) cell.getColumn().getTarget()).getExpression());
  }

}
