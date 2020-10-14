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
import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.polarsys.capella.core.data.cs.Component;
import org.polarsys.capella.core.transition.common.handlers.scope.IScopeRetriever;
import org.polarsys.capella.vp.ms.CSConfiguration;
import org.polarsys.capella.vp.ms.Situation;
import org.polarsys.kitalpha.emde.model.ExtensibleElement;
import org.polarsys.kitalpha.transposer.rules.handler.rules.api.IContext;

public class MsTransitionScopeRetriever implements IScopeRetriever {

  public MsTransitionScopeRetriever() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public IStatus init(IContext context) {
    return Status.OK_STATUS;
  }

  @Override
  public IStatus dispose(IContext context) {
    return Status.OK_STATUS;
  }

  @Override
  public Collection<? extends EObject> retrieveRelatedElements(EObject element, IContext context) {
    Collection<EObject> elements = new ArrayList<>();
    if (element instanceof Component) {
      for (EObject eObject : ((ExtensibleElement) element).getOwnedExtensions()) {
        if (eObject instanceof CSConfiguration) {
            elements.add(eObject);
        }
        else if (eObject instanceof Situation) {
          Situation situation = (Situation) eObject; 
          elements.add(situation);
          if (situation.getExpression() != null) {
            elements.add(((Situation)eObject).getExpression()); 
            // also transform the referenced state machines
            elements.addAll(EcoreUtil.ExternalCrossReferencer.find(((Situation)eObject).getExpression()).keySet());
          }
        }
      }
    }
    return elements;
  }

  @Override
  public Collection<? extends EObject> retrieveSharedElements(IContext context) {
    return Collections.emptyList();
  }

}
