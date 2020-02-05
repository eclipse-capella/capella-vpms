/*******************************************************************************
 * Copyright (c) 2017 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/

package org.polarsys.capella.vp.ms.provider;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.polarsys.capella.common.data.modellingcore.ModelElement;
import org.polarsys.capella.common.data.modellingcore.provider.ModelElementItemPropertyDescriptor;
import org.polarsys.capella.core.data.cs.Component;
import org.polarsys.capella.core.data.fa.AbstractFunction;
import org.polarsys.capella.core.data.fa.FunctionalExchange;
import org.polarsys.capella.core.data.information.Port;
import org.polarsys.capella.core.data.interaction.Scenario;
import org.polarsys.capella.vp.ms.CSConfiguration;
import org.polarsys.capella.vp.ms.MsPackage;

public class CSConfigurationItemPropertyDescriptor extends ModelElementItemPropertyDescriptor {

  public CSConfigurationItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator,
      String displayName, String description, EStructuralFeature feature, boolean isSettable, boolean multiLine,
      boolean sortChoices, Object staticImage, String category, String[] filterFlags) {
    super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, multiLine, sortChoices,
        staticImage, category, filterFlags);
  }

  @Override
  protected Collection<?> getComboBoxObjects(Object object) {

    if (feature == MsPackage.Literals.CS_CONFIGURATION__ELEMENTS) {
      Collection<?> result = super.getComboBoxObjects(object);
      List<ModelElement> scope = ((CSConfiguration) object).getScope();
      for (Iterator<?> it = result.iterator(); it.hasNext();) {
        Object next = it.next();
        if (((next instanceof FunctionalExchange
            || next instanceof Scenario
            || next instanceof Port
            || next instanceof Component
            || next instanceof AbstractFunction)) && !scope.contains(next)){
          it.remove();
        }
      }
      return result;
    }

    @SuppressWarnings("unchecked")
    Class<? extends ModelElement> type = (Class<? extends ModelElement>) feature.getEType().getInstanceClass();
    return ((CSConfiguration) object).getScope(type);
  }

}
