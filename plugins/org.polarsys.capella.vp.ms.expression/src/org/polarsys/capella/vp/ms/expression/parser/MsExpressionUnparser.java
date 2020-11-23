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
package org.polarsys.capella.vp.ms.expression.parser;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.polarsys.capella.common.helpers.EcoreUtil2;
import org.polarsys.capella.core.data.capellacommon.AbstractState;
import org.polarsys.capella.core.data.capellacommon.CapellacommonPackage;
import org.polarsys.capella.core.data.capellacommon.StateMachine;
import org.polarsys.capella.vp.ms.AndOperation;
import org.polarsys.capella.vp.ms.BooleanExpression;
import org.polarsys.capella.vp.ms.InSituationExpression;
import org.polarsys.capella.vp.ms.InStateExpression;
import org.polarsys.capella.vp.ms.MsPackage;
import org.polarsys.capella.vp.ms.NotOperation;
import org.polarsys.capella.vp.ms.OrOperation;
import org.polarsys.capella.vp.ms.util.MsSwitch;

public class MsExpressionUnparser extends MsSwitch<String> {

  private BooleanExpression root;

  private Mode mode;
  private StateMachine defaultStateMachine;
  
  public static enum Mode {
    /**
     * Formats InStateExpressions suitable for linked text display
     */
    HYPERLINK,
    
    /**
     * Formats InStateExpressions to the simple name of the state
     */
    NAME,
    
    /**
     * Formats InStateExpressions to &lt;StateMachineName>.&lt;StateName> 
     */
    QNAME
  }
  
  public MsExpressionUnparser(Mode mode) {
    this.mode = mode;
  }
  
  /**
   * Set the default state machine, which has the following effect: 
   * If mode is QNAME, and the owning state machine of a state is the default
   * state machine, mode switches back to NAME, omitting the name of the state machine
   * for that state. Probably only useful for tests.
   * 
   * @param sm
   */
  public void setDefaultStatemachine(StateMachine sm) {
    this.defaultStateMachine = sm;
  }
  
  public String unparse(BooleanExpression expression) {
    root = expression;
    return doSwitch(expression);
  }
  
  private String getHref(EObject e) {
    return "<a href=\"" + EcoreUtil.getID(e) + "\"/>"; // FIXME linkedtext should offer a helper for this  
  }

  @Override
  public String caseInStateExpression(InStateExpression object) {
    switch (mode) {
    case HYPERLINK : return getHref(object.getState());
    case NAME : return object.getState().getName();
    case QNAME : {
      AbstractState s = object.getState();
      StateMachine sm = (StateMachine) EcoreUtil2.getFirstContainer(s, CapellacommonPackage.Literals.STATE_MACHINE);
      if (sm == defaultStateMachine) {
        return s.getName();
      } else {
        return String.format("%s.%s", sm.getName(), s.getName());
      }
    }
    }
    return null;
  }

  @Override
  public String caseInSituationExpression(InSituationExpression object) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String caseAndOperation(AndOperation object) {
    Collection<String> childText = new ArrayList<String>(); 
    for (BooleanExpression child : object.getChildren()) {
      childText.add(doSwitch(child));
    }
    String result = String.join(" AND ", childText);
    if (object != root && object.eContainer().eClass() == MsPackage.Literals.NOT_OPERATION) {
      result = "(" + result + ")";
    }
    return result;
  }

  @Override
  public String caseOrOperation(OrOperation object) {
    Collection<String> childText = new ArrayList<String>(); 
    for (BooleanExpression child : object.getChildren()) {
      childText.add(doSwitch(child));
    }
    String result = String.join(" OR ", childText);
    if (object != root && (object.eContainer().eClass() == MsPackage.Literals.AND_OPERATION || object.eContainer().eClass() == MsPackage.Literals.NOT_OPERATION)) {
      result = "(" + result + ")";
    }
    return result;
  }

  @Override
  public String caseNotOperation(NotOperation object) {
    String result = "NOT " + doSwitch(object.getChildren().get(0));
    if (object != root && object.eContainer().eClass() == MsPackage.Literals.NOT_OPERATION) {
      result = "(" + result + ")";
    }
    return result;
  }
}