/*******************************************************************************
 * Copyright (c) 2017, 2020 ALTRAN.
 *  
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Altran - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.vp.ms.helpers;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.polarsys.capella.vp.ms.Comparison;

/**
 * @generated
 */
public class ComparisonHelper {

  private static final ComparisonHelper instance = new ComparisonHelper();

  /**
   * @generated
   */
  public static ComparisonHelper getInstance() {
    return instance;
  }

  /**
   * @generated
   */
  public Object doSwitch(Comparison object, EStructuralFeature feature) {
    // handle derivated feature

    // delegate to parent helper
    return org.polarsys.capella.core.data.helpers.capellacore.delegates.NamedElementHelper.getInstance()
        .doSwitch(object, feature);

  }

}