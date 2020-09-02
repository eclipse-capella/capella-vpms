package org.polarsys.capella.vp.ms.ui;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
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
import org.polarsys.capella.core.data.capellacommon.StateMachine;
import org.polarsys.capella.core.linkedtext.ui.CapellaEmbeddedLinkedTextEditorInput;
import org.polarsys.capella.vp.ms.BooleanExpression;
import org.polarsys.capella.vp.ms.Situation;
import org.polarsys.capella.vp.ms.ui.SituationExpressionParser.ExpressionError;
import org.polarsys.capella.vp.ms.util.LinkedText2Situation;
import org.polarsys.capella.vp.ms.util.LinkedText2Situation.SplitExpression;

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
        inputText = new LinkedText2Situation.ExpressionUnparser().unparse(rowExpression);
      }

      final String text = inputText;
      CapellaEmbeddedLinkedTextEditorInput input = new CapellaEmbeddedLinkedTextEditorInput(row.getTarget()) {
        @Override
        public String getText() {
          return text;
        }
        @Override
        public void setText(String linkedText) {
        }
      };
      SituationEditorDialog dialog = new SituationEditorDialog(HandlerUtil.getActiveShell(event), input);

      if (dialog.open() == Window.OK) {
        SituationExpressionParser parser = new SituationExpressionParser(dialog.getDocument());
        try {
          BooleanExpression expr = parser.parse();
          if (expr != null) {
            splitExpression.put(rowSm, expr);
          } else {
            splitExpression.remove(rowSm);
          }
          TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(col.getTarget()); 
          Command cmd = new RecordingCommand(domain) { 
            @Override
            protected void doExecute() {
              ((Situation)col.getTarget()).setExpression(splitExpression.merge());
            }
          };
          domain.getCommandStack().execute(cmd);
        } catch (ExpressionError e) {
          StatusManager.getManager().handle(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e), StatusManager.LOG | StatusManager.BLOCK);
        }
      }
    }
    return null;
  }

}
