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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.core.transition.system.rules.AbstractCapellaElementRule;
import org.polarsys.capella.vp.ms.MsPackage;
import org.polarsys.kitalpha.transposer.rules.handler.rules.api.IContext;

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

}
