/*******************************************************************************
 * Copyright (c) 2023 THALES GLOBAL SERVICES and others.
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

import org.eclipse.sirius.common.tools.api.util.StringUtil;
import org.eclipse.sirius.table.metamodel.table.DCell;
import org.eclipse.sirius.table.metamodel.table.DColumn;
import org.eclipse.sirius.table.metamodel.table.DLine;
import org.polarsys.capella.core.data.capellacommon.StateMachine;
import org.polarsys.capella.vp.ms.BooleanExpression;
import org.polarsys.capella.vp.ms.Situation;
import org.polarsys.capella.vp.ms.expression.parser.LinkedText2Situation;
import org.polarsys.capella.vp.ms.expression.parser.LinkedText2Situation.SplitExpression;
import org.polarsys.capella.vp.ms.expression.parser.MsExpressionUnparser;

/**
 * This Service allow clipboard implementation in SituationExpressions.
 * 
 * @author ebausson
 */
public class SituationExpressionClipboardService {

  private static String label = StringUtil.EMPTY_STRING;

  private static String expression = StringUtil.EMPTY_STRING;;

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
