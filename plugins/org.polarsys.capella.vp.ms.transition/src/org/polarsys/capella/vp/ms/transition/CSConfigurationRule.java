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
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.core.transition.common.handlers.attachment.AttachmentHelper;
import org.polarsys.capella.core.transition.system.rules.AbstractCapellaElementRule;
import org.polarsys.capella.vp.ms.CSConfiguration;
import org.polarsys.capella.vp.ms.MsPackage;
import org.polarsys.kitalpha.transposer.rules.handler.rules.api.IContext;
import org.polarsys.kitalpha.transposer.rules.handler.rules.api.IPremise;

public class CSConfigurationRule extends AbstractCapellaElementRule {

  public CSConfigurationRule() {
    registerUpdatedAttribute(MsPackage.Literals.CS_CONFIGURATION__ACCESS);
    registerUpdatedAttribute(MsPackage.Literals.CS_CONFIGURATION__KIND);
    registerUpdatedAttribute(MsPackage.Literals.CS_CONFIGURATION__SELECTOR);
    registerUpdatedReference(MsPackage.Literals.CS_CONFIGURATION__CHILD_CONFIGURATIONS);
    registerUpdatedReference(MsPackage.Literals.CS_CONFIGURATION__ELEMENTS);
  }
  
  @Override
  protected EClass getSourceType() {
    return MsPackage.Literals.CS_CONFIGURATION;
  }

  @Override
  public EClass getTargetType(EObject element, IContext context) {
    return MsPackage.Literals.CS_CONFIGURATION;
  }

  @Override
  protected void attachRelated(EObject element, EObject result, IContext context) {
    super.attachRelated(element, result, context);
    AttachmentHelper.getInstance(context).attachTracedElements(element, result, MsPackage.Literals.CS_CONFIGURATION__ELEMENTS, context);
    AttachmentHelper.getInstance(context).attachTracedElements(element, result, MsPackage.Literals.CS_CONFIGURATION__CHILD_CONFIGURATIONS, context);
  }

  @Override
  protected void premicesRelated(EObject element, ArrayList<IPremise> needed) {
    super.premicesRelated(element, needed);
    needed.addAll(createDefaultPrecedencePremices(element, MsPackage.Literals.CS_CONFIGURATION__CHILD_CONFIGURATIONS));
    needed.addAll(createDefaultPrecedencePremices(element, MsPackage.Literals.CS_CONFIGURATION__ELEMENTS));
  }

  @Override
  public List<EObject> retrieveRelatedElements(EObject element, IContext context) {
    return new ArrayList<>(((CSConfiguration) element).getElements());
  }  

}
