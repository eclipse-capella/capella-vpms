/*******************************************************************************
 * Copyright (c) 2017, 2021 THALES GLOBAL SERVICES.
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

package org.polarsys.capella.vp.ms.provider;

import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.polarsys.capella.common.data.modellingcore.provider.ModelElementItemPropertyDescriptor;
import org.polarsys.capella.core.data.cs.Component;
import org.polarsys.capella.core.data.cs.ComponentPkg;
import org.polarsys.capella.vp.ms.CSConfiguration;
import org.polarsys.kitalpha.emde.model.ExtensibleElement;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class ChildConfigurationsPropertyDescriptor extends ModelElementItemPropertyDescriptor {

  public ChildConfigurationsPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator,
      String displayName, String description, EStructuralFeature feature, boolean isSettable, boolean multiLine,
      boolean sortChoices, Object staticImage, String category, String[] filterFlags) {
    super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, multiLine, sortChoices,
        staticImage, category, filterFlags);
  }

  private Collection<CSConfiguration> getOwnedConfigurations(ExtensibleElement e,
      Predicate<CSConfiguration> predicate) {
    Collection<CSConfiguration> result = new ArrayList<CSConfiguration>();
    for (EObject ext : e.getOwnedExtensions()) {
      if (ext instanceof CSConfiguration) {
        if (predicate.apply((CSConfiguration) ext)) {
          result.add((CSConfiguration) ext);
        }
      }
    }
    return result;
  }

  private Collection<CSConfiguration> getOwnedConfigurations(ExtensibleElement e) {
    return getOwnedConfigurations(e, Predicates.<CSConfiguration> alwaysTrue());
  }

  private Collection<CSConfiguration> getSiblingConfigurations(ExtensibleElement parent,
      CSConfiguration configuration) {
    return getOwnedConfigurations(parent, not(equalTo(configuration)));
  }

  
  @Override
  protected Collection<?> getComboBoxObjects(final Object object) {

    EObject parent = ((CSConfiguration) object).eContainer();
    
    final Collection<CSConfiguration> result = new ArrayList<CSConfiguration>();

    for (TreeIterator<EObject> it = parent.eAllContents(); it.hasNext();) {
      EObject next = it.next();
      if (next instanceof CSConfiguration && next != object) {
        result.add((CSConfiguration) next);
      } else if (!(next instanceof Component) && !(next instanceof ComponentPkg)){
        it.prune();
      }
    }
    return result;
  }

}
