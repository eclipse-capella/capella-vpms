/*******************************************************************************
 * Copyright (c) 2017, 2020 THALES GLOBAL SERVICES.
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
package org.polarsys.capella.core.data.common.statemachine.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.polarsys.capella.common.helpers.EcoreUtil2;
import org.polarsys.capella.core.data.capellacommon.AbstractState;
import org.polarsys.capella.core.data.capellacommon.JoinPseudoState;
import org.polarsys.capella.core.data.capellacommon.State;
import org.polarsys.capella.core.data.capellacommon.StateTransition;

public class MDCHK_Join_SourceState_Region extends AbstractModelConstraint {
  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse.emf.validation.IValidationContext)
   */
  @Override
  public IStatus validate(IValidationContext ctx) {
    EList<AbstractState> sourceAbstractState = new BasicEList<AbstractState>();
    if (ctx.getTarget() instanceof JoinPseudoState) {
      JoinPseudoState join = (JoinPseudoState) ctx.getTarget();
      for (StateTransition transition : join.getIncoming()) {
        sourceAbstractState.add(transition.getSource());
      }
      EObject eObject = EcoreUtil2.getCommonAncestor(sourceAbstractState);
      if (!sourceAbstractState.contains(eObject) && (eObject instanceof State)) {
        return ctx.createSuccessStatus();
      }
      return ctx.createFailureStatus(join.getName());
    }
    return null;
  }
}
