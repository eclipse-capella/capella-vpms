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
package org.polarsys.capella.vp.ms.transition;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.EqualityHelper;
import org.polarsys.capella.common.data.modellingcore.AbstractTrace;
import org.polarsys.capella.common.lib.IdGenerator;
import org.polarsys.capella.core.data.capellacommon.AbstractState;
import org.polarsys.capella.core.transition.common.constants.ITransitionConstants;
import org.polarsys.capella.core.transition.common.handlers.traceability.TraceabilityHandlerHelper;
import org.polarsys.capella.core.transition.system.rules.AbstractCapellaElementRule;
import org.polarsys.capella.vp.ms.BooleanExpression;
import org.polarsys.capella.vp.ms.InStateExpression;
import org.polarsys.capella.vp.ms.MsPackage;
import org.polarsys.capella.vp.ms.Situation;
import org.polarsys.kitalpha.transposer.rules.handler.rules.api.IContext;
import org.polarsys.kitalpha.transposer.rules.handler.rules.api.IPremise;

public class SituationRule extends AbstractCapellaElementRule {

  public SituationRule() {
  }

  @Override
  protected EClass getSourceType() {
    return MsPackage.Literals.SITUATION;
  }

  @Override
  public EClass getTargetType(EObject element, IContext context) {
    return getSourceType();
  }

  @Override
  public void apply(EObject element, IContext context) throws Exception {

    super.apply(element, context);

    if ((Boolean) context.get(ITransitionConstants.TRANSPOSER_APPLY_IS_COMPLETE)) {

      Situation situation = (Situation) element;
      
      if (situation.getExpression() != null) {
        
        // copy the expression, replacing referenced states with their corresponding realizing states
        BooleanExpression transfoExpression = transfoExpression(situation.getExpression(), context);
 
        Situation tracing = null; 

        for (AbstractTrace trace : situation.getIncomingTraces()) {
          if (trace.getSourceElement() instanceof Situation) {
            tracing = (Situation) trace.getSourceElement();
          }
        }

        if (tracing == null || tracing.getExpression() == null || !equivalentExpressions(transfoExpression, tracing.getExpression(), context)) {
          for (EObject tracingTmp : TraceabilityHandlerHelper.getInstance(context).retrieveTracedElements(situation, context)) {
            ((Situation) tracingTmp).setExpression(transfoExpression);
          }
        }
      }
    }
  }
  
  //copies the expression, replacing state references with references to realizing states
  private BooleanExpression transfoExpression(BooleanExpression expression, IContext context) {
   
   @SuppressWarnings("serial")
  EcoreUtil.Copier copier = new EcoreUtil.Copier() {
     @Override
     protected void copyAttributeValue(EAttribute eAttribute, EObject eObject, Object value, Setting setting) {
       if (eAttribute.isID()) {
         setting.set(IdGenerator.createId());
       }
     }
     @Override
     protected void copyReference(EReference eReference, EObject eObject, EObject copyEObject) {
       if (eReference == MsPackage.Literals.IN_STATE_EXPRESSION__STATE) {
         AbstractState s = ((InStateExpression) eObject).getState();
         Collection<EObject> tracedStates = TraceabilityHandlerHelper.getInstance(context).retrieveTracedElements(s, context);
         if (tracedStates.size() > 0) {
           ((InStateExpression) copyEObject).setState((AbstractState) tracedStates.iterator().next());
         }
       }
     }
   };
   BooleanExpression target = (BooleanExpression) copier.copy(expression);
   copier.copyReferences();
   return target;
 }

  @SuppressWarnings("serial")
  private boolean equivalentExpressions(BooleanExpression transfo, BooleanExpression original, IContext context) {

    return new EqualityHelper() {

      @Override
      protected boolean haveEqualReference(EObject transfo, EObject original, EReference reference) {

        if (reference == MsPackage.Literals.IN_STATE_EXPRESSION__STATE) {
          
          AbstractState s1 = ((InStateExpression) transfo).getState();
          AbstractState s2 = ((InStateExpression) original).getState();

          AbstractState s1traced = null;
          AbstractState s2traced = null;

          if (s1 == null) {
            return s2 == null;
          } 
          
          if (s2 == null) {
            return s1 == null;
          }

          for (EObject r1 : TraceabilityHandlerHelper.getInstance(context).retrieveSourceElements(s1, context)) {
            s1traced = (AbstractState) r1;
          }

          for (AbstractState r2 : s2.getRealizedAbstractStates()) {
            s2traced = r2;
          }

          return s1traced == s2traced;

        }

        return super.haveEqualReference(transfo, original, reference);        
      }

      @Override
      protected boolean haveEqualAttribute(EObject eObject1, EObject eObject2, EAttribute attribute) {
        // expression  equality is purely structural, there are no meaningful attributes
        return true;
      }

    }.equals(transfo, original);

  }

  @Override
  protected void premicesRelated(EObject element, ArrayList<IPremise> needed) {
    super.premicesRelated(element, needed);
    needed.addAll(createDefaultPrecedencePremices(EcoreUtil.ExternalCrossReferencer.find(((Situation)element).getExpression()).keySet(), "referenced states"));
  }

}
