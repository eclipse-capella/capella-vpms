package org.polarsys.capella.vp.ms.ui.clipboard;

import org.eclipse.sirius.table.metamodel.table.DCell;
import org.eclipse.sirius.table.metamodel.table.DColumn;
import org.eclipse.sirius.table.metamodel.table.DLine;
import org.polarsys.capella.core.data.capellacommon.StateMachine;
import org.polarsys.capella.vp.ms.BooleanExpression;
import org.polarsys.capella.vp.ms.Situation;
import org.polarsys.capella.vp.ms.expression.parser.LinkedText2Situation;
import org.polarsys.capella.vp.ms.expression.parser.LinkedText2Situation.SplitExpression;
import org.polarsys.capella.vp.ms.expression.parser.MsExpressionUnparser;

public class SituationExpressionClipboardService {

  private static String label = "";
  private static String expression = "";

  public static void setClipboardContent(DCell cell) {
    if (cell != null) {
      DColumn col = cell.getColumn();
      DLine row = cell.getLine();

      if (col.getTarget() instanceof Situation && row.getTarget() instanceof StateMachine) {

        StateMachine rowSm = (StateMachine) row.getTarget();

        LinkedText2Situation.SplitExpression splitExpression = SplitExpression.of(((Situation) col.getTarget()).getExpression());
        BooleanExpression rowExpression = splitExpression.get(rowSm);

        if (rowExpression != null) {
          expression = new MsExpressionUnparser(MsExpressionUnparser.Mode.HYPERLINK).unparse(rowExpression);
          label = cell.getLabel();
        }
      }
      
      
    }

  }

  public static boolean isEmpty() {
    return expression.isEmpty();
  }

  public static String getSavedLabel() {
    return label;
  }
  
  public static String getExpression() {
    return expression;
  }

}
