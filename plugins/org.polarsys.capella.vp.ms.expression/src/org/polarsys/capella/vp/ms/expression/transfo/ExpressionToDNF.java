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
package org.polarsys.capella.vp.ms.expression.transfo;

import java.util.LinkedList;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.polarsys.capella.vp.ms.AndOperation;
import org.polarsys.capella.vp.ms.BooleanExpression;
import org.polarsys.capella.vp.ms.BooleanOperation;
import org.polarsys.capella.vp.ms.MsFactory;
import org.polarsys.capella.vp.ms.MsPackage;
import org.polarsys.capella.vp.ms.OrOperation;
import org.polarsys.capella.vp.ms.util.MsSwitch;

/**
 * Converts an Expression in NNF to DNF. To convert any expression, first use {@link ExpressionToNNF}.
 */
public class ExpressionToDNF extends MsSwitch<BooleanExpression> {

  public BooleanExpression caseBooleanExpression(BooleanExpression b) {
    return EcoreUtil.copy(b);
  }

  private BooleanOperation convertChildren(BooleanOperation op) {
    BooleanOperation result = (BooleanOperation) MsFactory.eINSTANCE.create(op.eClass());
    for (BooleanExpression child : op.getChildren()) {
      result.getChildren().add(doSwitch(child));
    }
    return result;
  }

  public BooleanOperation caseOrOperation(OrOperation op) {
    return convertChildren(op);
  }
  
  @Override
  public BooleanExpression caseAndOperation(AndOperation and) {

    // first, convert my children to DNF
    BooleanExpression result = convertChildren(and);
    
    OrOperation firstOr = null;
    LinkedList<BooleanExpression> rest = new LinkedList<>();

    for (BooleanExpression factor : ((AndOperation)result).getChildren()) {
      if (firstOr == null && factor.eClass() == MsPackage.Literals.OR_OPERATION) {
        firstOr = (OrOperation) factor;
      } else {
        rest.add(factor);
      }
    }

    if (firstOr != null) {
      // or inside and, must multiply: 

      if (rest.isEmpty()) {

        // corner case (and (or A)) => A) 
        result = firstOr; // ... which is already in DNF

      } else {

        // use the indices to multiply intuitively left to right
        // not strictly required, but it's easier to specify expected
        // test results like that
        BooleanExpression firstRest = rest.removeFirst();
        int restIndex = ((AndOperation)result).getChildren().indexOf(firstRest);
        int firstOrIndex = ((AndOperation)result).getChildren().indexOf(firstOr);

        OrOperation outerOr = MsFactory.eINSTANCE.createOrOperation();
        for (BooleanExpression child : firstOr.getChildren()) {

          AndOperation innerAnd = MsFactory.eINSTANCE.createAndOperation();

          if (firstOrIndex < restIndex) {
            innerAnd.getChildren().add(EcoreUtil.copy(child));
            innerAnd.getChildren().add(EcoreUtil.copy(firstRest));
          } else {
            innerAnd.getChildren().add(EcoreUtil.copy(firstRest));
            innerAnd.getChildren().add(EcoreUtil.copy(child));
          }

          outerOr.getChildren().add(doSwitch(innerAnd));

        }

        if (rest.isEmpty()) {

          result = outerOr;

        } else {

          AndOperation outerAnd = MsFactory.eINSTANCE.createAndOperation();
          outerAnd.getChildren().add(outerOr);
          for (BooleanExpression nextRest : rest) {
            outerAnd.getChildren().add(EcoreUtil.copy(nextRest));
          }
          result = doSwitch(outerAnd);
        }
      }
    }

    return result;
  }

}
