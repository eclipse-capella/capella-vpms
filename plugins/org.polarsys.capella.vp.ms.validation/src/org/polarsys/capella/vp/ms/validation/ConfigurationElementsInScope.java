/*******************************************************************************
 * Copyright (c) 2021 THALES GLOBAL SERVICES.
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
package org.polarsys.capella.vp.ms.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.ConstraintStatus;
import org.polarsys.capella.core.validation.rule.AbstractValidationRule;
import org.polarsys.capella.vp.ms.CSConfiguration;

public class ConfigurationElementsInScope extends AbstractValidationRule {

  @Override
  public IStatus validate(IValidationContext ctx) {

    if (ctx.getEventType() == EMFEventType.NULL) {

      Collection<IStatus> errors = new ArrayList<>();

      CSConfiguration c = (CSConfiguration) ctx.getTarget();
      for (EObject e : c.getIncluded()) {
        if (!(c.getScope().contains(e))) {
          errors.add(ConstraintStatus.createStatus(ctx, Collections.singleton(e), "Included element ''{0}'' is not in the scope of ''{1}''", e, ctx.getTarget()));
        }
      }
      for (EObject e : c.getIncluded()) {
        if (!(c.getScope().contains(e))) {
          errors.add(ConstraintStatus.createStatus(ctx, Collections.singleton(e), "Excluded element ''{0}'' is not in the scope of ''{1}''", e, ctx.getTarget()));
        }
      }

      if (errors.size() > 0) {
        return ConstraintStatus.createMultiStatus(ctx, errors);
      } 
    }

    return ctx.createSuccessStatus();
  }

}
