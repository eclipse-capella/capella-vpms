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
package org.polarsys.capella.vp.ms.ui;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.polarsys.capella.core.model.handler.command.IDeleteHelper;
import org.polarsys.capella.vp.ms.MsPackage;

public class MsDeleteHelper implements IDeleteHelper {

  @Override
  public boolean isDeleteSemanticStructure(EObject sourceObject_p, EObject linkObject_p, EStructuralFeature feature_p) {
    return false;
  }

  @Override
  public boolean isDeleteElement(EObject sourceObject, EObject linkObject_p, EStructuralFeature feature) {
    return feature == MsPackage.Literals.IN_STATE_EXPRESSION__STATE;
  }

  @Override
  public Collection<?> getExpandedSelection(Collection<?> selection_p) {
    return selection_p;
  }

  @Override
  public Collection<EObject> getAdditionalElements(EObject sourceObject_p, EObject linkObject_p,
      EStructuralFeature feature_p) {
    return Collections.emptyList();
  }

  @Override
  public Collection<Command> getAdditionalCommands(EObject sourceObject_p, EObject linkObject_p,
      EStructuralFeature feature_p) {
    return Collections.emptyList();
  }

}
