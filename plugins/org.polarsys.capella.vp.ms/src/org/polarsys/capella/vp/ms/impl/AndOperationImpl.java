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

package org.polarsys.capella.vp.ms.impl;

import org.eclipse.emf.ecore.EClass;
import org.polarsys.capella.vp.ms.AndOperation;
import org.polarsys.capella.vp.ms.MsPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>And Operation</b></em>'. <!-- end-user-doc -->
 *
 * @generated
 */
public class AndOperationImpl extends BooleanOperationImpl implements AndOperation {
  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected AndOperationImpl() {

    super();

  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return MsPackage.Literals.AND_OPERATION;
  }

} // AndOperationImpl