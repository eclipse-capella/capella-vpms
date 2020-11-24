/*******************************************************************************
 * Copyright (c) 2017, 2020 THALES GLOBAL SERVICES.
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

package org.polarsys.capella.vp.ms.helpers;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.polarsys.capella.common.model.helpers.IHelper;
import org.polarsys.capella.vp.ms.AndOperation;
import org.polarsys.capella.vp.ms.BooleanExpression;
import org.polarsys.capella.vp.ms.BooleanOperation;
import org.polarsys.capella.vp.ms.CSConfiguration;
import org.polarsys.capella.vp.ms.InSituationExpression;
import org.polarsys.capella.vp.ms.InStateExpression;
import org.polarsys.capella.vp.ms.NotOperation;
import org.polarsys.capella.vp.ms.OrOperation;
import org.polarsys.capella.vp.ms.Situation;

/**
 * @generated
 */
public class MsPackageHelper implements IHelper {

  /**
   * @generated
   */
  public Object getValue(EObject object, EStructuralFeature feature, EAnnotation annotation) {
    Object ret = null;

    if (ret == null && object instanceof CSConfiguration) {
      ret = CSConfigurationHelper.getInstance().doSwitch((CSConfiguration) object, feature);
    }
    if (ret == null && object instanceof Situation) {
      ret = SituationHelper.getInstance().doSwitch((Situation) object, feature);
    }
    if (ret == null && object instanceof BooleanExpression) {
      ret = BooleanExpressionHelper.getInstance().doSwitch((BooleanExpression) object, feature);
    }
    if (ret == null && object instanceof BooleanOperation) {
      ret = BooleanOperationHelper.getInstance().doSwitch((BooleanOperation) object, feature);
    }
    if (ret == null && object instanceof InStateExpression) {
      ret = InStateExpressionHelper.getInstance().doSwitch((InStateExpression) object, feature);
    }
    if (ret == null && object instanceof InSituationExpression) {
      ret = InSituationExpressionHelper.getInstance().doSwitch((InSituationExpression) object, feature);
    }
    if (ret == null && object instanceof AndOperation) {
      ret = AndOperationHelper.getInstance().doSwitch((AndOperation) object, feature);
    }
    if (ret == null && object instanceof OrOperation) {
      ret = OrOperationHelper.getInstance().doSwitch((OrOperation) object, feature);
    }
    if (ret == null && object instanceof NotOperation) {
      ret = NotOperationHelper.getInstance().doSwitch((NotOperation) object, feature);
    }
    return ret;
  }

}
