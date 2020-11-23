/*******************************************************************************
 * Copyright (c) 2020 THALES GLOBAL SERVICES.
 *  
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.vp.ms.ui;
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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.sirius.table.metamodel.table.DCell;
import org.eclipse.sirius.table.metamodel.table.DColumn;
import org.eclipse.sirius.table.metamodel.table.DLine;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.statushandlers.StatusManager;
import org.polarsys.capella.common.linkedtext.ui.LinkedTextDocument;
import org.polarsys.capella.common.linkedtext.ui.LinkedTextHyperlink;
import org.polarsys.capella.core.data.capellacommon.StateMachine;
import org.polarsys.capella.core.linkedtext.ui.CapellaEmbeddedLinkedTextEditorInput;
import org.polarsys.capella.vp.ms.BooleanExpression;
import org.polarsys.capella.vp.ms.Situation;
import org.polarsys.capella.vp.ms.expression.parser.MsExpressionUnparser;
import org.polarsys.capella.vp.ms.expression.parser.DefaultMsExpressionVisitor;
import org.polarsys.capella.vp.ms.expression.parser.LinkedText2Situation;
import org.polarsys.capella.vp.ms.expression.parser.MsExpressionUtil;
import org.polarsys.capella.vp.ms.expression.parser.LinkedText2Situation.SplitExpression;

public class SituationExpressionHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IStructuredSelection selection = HandlerUtil.getCurrentStructuredSelection(event);
    DCell cell = (DCell) selection.getFirstElement();
    DColumn col = cell.getColumn();
    DLine row = cell.getLine();
    String inputText = "";

    if (col.getTarget() instanceof Situation && row.getTarget() instanceof StateMachine) {

      StateMachine rowSm = (StateMachine) row.getTarget();

      LinkedText2Situation.SplitExpression splitExpression = SplitExpression.of(((Situation)col.getTarget()).getExpression());
      BooleanExpression rowExpression = splitExpression.get(rowSm);

      if (rowExpression != null) {
        inputText = new MsExpressionUnparser(MsExpressionUnparser.Mode.HYPERLINK).unparse(rowExpression);
      }

      final String text = inputText;
      final AtomicReference<String> toParse = new AtomicReference<String>(text);
      CapellaEmbeddedLinkedTextEditorInput input = new CapellaEmbeddedLinkedTextEditorInput(row.getTarget()) {
        @Override
        public String getText() {
          return text;
        }
        @Override
        public void setText(String linkedText) {
          toParse.set(linkedText);
        }
      };
      SituationEditorDialog dialog = new SituationEditorDialog(HandlerUtil.getActiveShell(event), input);

      if (dialog.open() == Window.OK) {


        String expression = dialog.getDocument().get().trim();

        if (expression.isEmpty()) {          
          splitExpression.remove(rowSm);
        } else {
          try {
            List<String> errors = new ArrayList<>();
            ParseTree tree = MsExpressionUtil.parse(
                toParse.get(), 
                createErrorListener(errors));
            if (errors.isEmpty()) {
              splitExpression.put(rowSm, new DefaultMsExpressionVisitor(createResolver(dialog.getDocument())).visit(tree));
            } else {
              StatusManager.getManager().handle(new Status(IStatus.ERROR, Activator.PLUGIN_ID, errors.get(0)), StatusManager.BLOCK);
              return null;
            }
          } catch (RecognitionException | NoSuchElementException e) {
            StatusManager.getManager().handle(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e), StatusManager.BLOCK);
            return null;
          }
        }

        TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(col.getTarget()); 
        Command cmd = new RecordingCommand(domain) { 
          @Override
          protected void doExecute() {
            ((Situation)col.getTarget()).setExpression(splitExpression.merge());
          }
        };
        domain.getCommandStack().execute(cmd);
      }
    }
    return null;
  }

  private ANTLRErrorListener createErrorListener(Collection<String> errors) {
    return new BaseErrorListener() {
      @Override
      public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
          String msg, RecognitionException e) {
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

  
}
