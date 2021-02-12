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

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import org.polarsys.capella.common.helpers.EcoreUtil2;
import org.polarsys.capella.core.data.capellacommon.CapellacommonPackage;
import org.polarsys.capella.core.data.capellacommon.StateMachine;
import org.polarsys.capella.vp.ms.AndOperation;
import org.polarsys.capella.vp.ms.BooleanExpression;
import org.polarsys.capella.vp.ms.BooleanOperation;
import org.polarsys.capella.vp.ms.InSituationExpression;
import org.polarsys.capella.vp.ms.InStateExpression;
import org.polarsys.capella.vp.ms.MsFactory;
import org.polarsys.capella.vp.ms.MsPackage;
import org.polarsys.capella.vp.ms.Situation;
import org.polarsys.capella.vp.ms.util.MsSwitch;

public class LinkedText2Situation {

  public enum Token {
    AND("AND"), OR("OR"), NOT("NOT"), PAREN_O("("), PAREN_C(")"), HYPERLINK(null), EOS(null), ERROR(null);

    String literal;

    Token(String literal) {
      this.literal = literal;
    }

    public String getLiteral() {
      return literal;
    }

  }

  /**
   * Provides a method {@link #split()} to split the boolean expression that defines a {@link Situation} into
   * subexpressions to allow displaying them in a SituationExpression table, and a method {@link #merge()} to merge the
   * subexpressions back into one.
   */
  @SuppressWarnings("serial")
  public static class SplitExpression extends LinkedHashMap<StateMachine, BooleanExpression> {

    private AndOperation expression;

    private SplitExpression(BooleanExpression expression) throws IllegalArgumentException {
      if (expression != null && expression.eClass() != MsPackage.Literals.AND_OPERATION) {
        throw new IllegalArgumentException("Argument must be an AND operation");
      }
      this.expression = (AndOperation) expression;
    }

    public static SplitExpression of(BooleanExpression expression) throws IllegalArgumentException {
      SplitExpression split = new SplitExpression(expression);
      split.split();
      return split;
    }

    /**
     * Merge the per-statemachine expressions back into the original expression.
     * 
     * @return the merged expression.
     */
    public AndOperation merge() {
      if (isEmpty()) {
        return null;
      }
      if (expression == null) {
        expression = MsFactory.eINSTANCE.createAndOperation();
      } else {
        expression.getChildren().clear();
      }
      expression.getChildren().addAll(values());
      return expression;
    }

    private void split() throws IllegalArgumentException {
      StateMachineCounter counter = new StateMachineCounter();

      if (expression != null) {

        // Each child-expression must then reference exactly one StateMachine
        for (BooleanExpression child : ((AndOperation) expression).getChildren()) {
          Set<StateMachine> childReferencedStateMachines = counter.doSwitch(child);
          switch (childReferencedStateMachines.size()) {
          case 0:
            throw new IllegalArgumentException("Found no statemachine references in subexpression");
          case 1: {
            StateMachine sm = childReferencedStateMachines.iterator().next();
            if (put(sm, child) != null) {
              throw new IllegalArgumentException(
                  "Found references to " + sm.getName() + " in more than one subexpressions");
            }
            break;
          }
          default:
            throw new IllegalArgumentException("Found reference to more than one statemachine in a subexpression");
          }
        }
      }
    }

    private class StateMachineCounter extends MsSwitch<Set<StateMachine>> {

      @Override
      public Set<StateMachine> caseBooleanOperation(BooleanOperation object) {
        Set<StateMachine> result = new HashSet<>();
        for (BooleanExpression child : object.getChildren()) {
          Set<StateMachine> childMachines = doSwitch(child);
          result.addAll(childMachines);
        }
        return result;
      }

      @Override
      public Set<StateMachine> caseInStateExpression(InStateExpression object) {
        StateMachine sm = (StateMachine) EcoreUtil2.getFirstContainer(object.getState(),
            CapellacommonPackage.Literals.STATE_MACHINE);
        return Collections.singleton(sm);
      }

      @Override
      public Set<StateMachine> caseInSituationExpression(InSituationExpression object) {
        return doSwitch(object.getSituation().getExpression());
      }

    }
  }

}
