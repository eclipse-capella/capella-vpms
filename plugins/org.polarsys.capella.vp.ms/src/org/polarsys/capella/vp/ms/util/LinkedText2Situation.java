package org.polarsys.capella.vp.ms.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
import org.polarsys.capella.vp.ms.NotOperation;
import org.polarsys.capella.vp.ms.OrOperation;
import org.polarsys.capella.vp.ms.Situation;

public class LinkedText2Situation {

  public enum Token {
    AND("AND"),
    OR("OR"),
    NOT("NOT"),
    PAREN_O("("),
    PAREN_C(")"),
    HYPERLINK(null),
    EOS(null),
    ERROR(null);

    String literal;
    Token(String literal){
      this.literal = literal;
    }

    public String getLiteral() {
      return literal;
    }

  }

  
  
  public static class ExpressionUnparser extends MsSwitch<String> {

    private BooleanExpression root;
    
    public String unparse(BooleanExpression expression) {
      root = expression;
      return doSwitch(expression);
    }

    private String getHref(EObject e) {
      return "<a href=\"" + EcoreUtil.getID(e) + "\"/>"; // FIXME linkedtext should offer a helper for this  
    }
    
    @Override
    public String caseInStateExpression(InStateExpression object) {
      return getHref(object.getState());
    }

    @Override
    public String caseInSituationExpression(InSituationExpression object) {
      return getHref(object.getSituation());
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

  /**
   * Provides a method {@link #split()} to split the boolean expression
   * that defines a {@link Situation} into subexpressions to allow displaying them in a 
   * SituationExpression table, and a method {@link #merge()} to merge the subexpressions
   * back into one. 
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
     * Merge the per-statemachine expressions back into the original
     * expression.
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
            
          if (childReferencedStateMachines.size() == 1) {

            StateMachine sm = childReferencedStateMachines.iterator().next();

            if (put(sm, child) != null) {                
              throw new IllegalArgumentException("Found references to " + sm.getName() + " in more than one subexpressions");
            }
          } else {
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
        StateMachine sm = (StateMachine) EcoreUtil2.getFirstContainer(object.getState(), CapellacommonPackage.Literals.STATE_MACHINE);
        return Collections.singleton(sm);
      }
  
      @Override
      public Set<StateMachine> caseInSituationExpression(InSituationExpression object) {
        return doSwitch(object.getSituation().getExpression());
      }
  
    }
  }
  
  
}
