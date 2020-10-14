/*******************************************************************************
 * Copyright (c) 2020 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.polarsys.capella.common.lib.IdGenerator;
import org.polarsys.capella.core.data.capellacommon.AbstractState;
import org.polarsys.capella.core.transition.common.handlers.traceability.TraceabilityHandlerHelper;
import org.polarsys.capella.core.transition.common.rules.AbstractRule;
import org.polarsys.capella.vp.ms.BooleanExpression;
import org.polarsys.capella.vp.ms.InStateExpression;
import org.polarsys.capella.vp.ms.MsPackage;
import org.polarsys.capella.vp.ms.Situation;
import org.polarsys.kitalpha.transposer.rules.handler.rules.api.IContext;
import org.polarsys.kitalpha.transposer.rules.handler.rules.api.IPremise;
/**
 * This class handles transformation for top level expressions and should not be invoked
 * on sub-expressions.
 */
public class BooleanExpressionRule extends AbstractRule {

  public BooleanExpressionRule() {
  }

  @SuppressWarnings("serial")
  @Override
  public void apply(EObject element, IContext context) throws Exception {
    BooleanExpression source = (BooleanExpression) element;
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

    BooleanExpression target = (BooleanExpression) copier.copy(source);
    copier.copyReferences();

    Collection<EObject> targetContainers = TraceabilityHandlerHelper.getInstance(context).retrieveTracedElements(source.eContainer(), context);
    if (!targetContainers.isEmpty()) {
      Situation first = (Situation) targetContainers.iterator().next();
      first.setExpression(target);
    }
  }

  @Override
  protected EClass getSourceType() {
    return MsPackage.Literals.BOOLEAN_EXPRESSION;
  }

  @Override
  public EClass getTargetType(EObject element, IContext context) {
    return getSourceType();
  }

  @Override
  protected void premicesRelated(EObject element, ArrayList<IPremise> needed) {
    super.premicesRelated(element, needed);

    // need to transform referenced states first..
    needed.addAll(createDefaultPrecedencePremices(EcoreUtil.ExternalCrossReferencer.find(element).keySet(), "referenced states"));
  }

}
