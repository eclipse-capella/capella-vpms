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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.polarsys.capella.vp.ms.BooleanExpression;
import org.polarsys.capella.vp.ms.BooleanOperation;
import org.polarsys.capella.vp.ms.InSituationExpression;
import org.polarsys.capella.vp.ms.InStateExpression;
import org.polarsys.capella.vp.ms.MsFactory;
import org.polarsys.capella.vp.ms.MsPackage;
import org.polarsys.capella.vp.ms.NotOperation;
import org.polarsys.capella.vp.ms.util.MsSwitch;

/**
 * Converts any expression to Negation Normal Form
 */
public class ExpressionToNNF extends MsSwitch<BooleanExpression> {
  
  @Override
  public BooleanExpression caseBooleanOperation(BooleanOperation object) {
    BooleanOperation result = (BooleanOperation) MsFactory.eINSTANCE.create(object.eClass());
    for (BooleanExpression c : object.getChildren()) {
      result.getChildren().add(doSwitch(c));
    }
    return result;
  }

  @Override
  public BooleanExpression caseNotOperation(NotOperation object) {
    
    BooleanExpression result = null;
    BooleanExpression child = object.getChildren().iterator().next();

    if (child.eClass() == MsPackage.Literals.IN_SITUATION_EXPRESSION || child.eClass() == MsPackage.Literals.IN_STATE_EXPRESSION) {
      result = EcoreUtil.copy(object);
    } else if (child.eClass() == MsPackage.Literals.NOT_OPERATION) {
      result = doSwitch(((NotOperation) child).getChildren().iterator().next());
    } else {
      EClass transform = null;
      if (child.eClass() == MsPackage.Literals.AND_OPERATION) {
        transform = MsPackage.Literals.OR_OPERATION;
      }
      if (child.eClass() == MsPackage.Literals.OR_OPERATION) {
        transform = MsPackage.Literals.AND_OPERATION;
      }
      result = (BooleanOperation) MsFactory.eINSTANCE.create(transform);
      for (BooleanExpression expression : ((BooleanOperation) child).getChildren()) {
        NotOperation notOp = MsFactory.eINSTANCE.createNotOperation();
        notOp.getChildren().add(EcoreUtil.copy(expression));
        ((BooleanOperation)result).getChildren().add(doSwitch(notOp));
      }
    }
    
    return result;
  }

  @Override
  public BooleanExpression caseInStateExpression(InStateExpression object) {
    return EcoreUtil.copy(object);
  }

  @Override
  public BooleanExpression caseInSituationExpression(InSituationExpression object) {
    return EcoreUtil.copy(object);
  }

}
