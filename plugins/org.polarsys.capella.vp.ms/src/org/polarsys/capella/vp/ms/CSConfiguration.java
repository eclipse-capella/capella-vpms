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

package org.polarsys.capella.vp.ms;

import org.eclipse.emf.common.util.EList;
import org.polarsys.capella.common.data.modellingcore.ModelElement;
import org.polarsys.capella.core.data.capellacommon.AbstractState;
import org.polarsys.capella.core.data.capellacore.NamedElement;
import org.polarsys.kitalpha.emde.model.ElementExtension;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>CS Configuration</b></em>'. <!-- end-user-doc
 * -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.polarsys.capella.vp.ms.CSConfiguration#getSupportedModes <em>Supported Modes</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.CSConfiguration#getElements <em>Elements</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.CSConfiguration#getDeploymentLinks <em>Deployment Links</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.CSConfiguration#getFunctions <em>Functions</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.CSConfiguration#getFunctionalChains <em>Functional Chains</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.CSConfiguration#getComponents <em>Components</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.CSConfiguration#getPorts <em>Ports</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.CSConfiguration#getChildConfigurations <em>Child Configurations</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.CSConfiguration#getKind <em>Kind</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.CSConfiguration#getAccess <em>Access</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.CSConfiguration#getSelector <em>Selector</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.CSConfiguration#getContext <em>Context</em>}</li>
 * </ul>
 *
 * @see org.polarsys.capella.vp.ms.MsPackage#getCSConfiguration()
 * @model annotation="http://www.polarsys.org/kitalpha/emde/1.0.0/constraint ExtendedElement='
 *        http://www.polarsys.org/capella/core/cs/1.4.0#//Component
 *        http://www.polarsys.org/capella/core/cs/1.4.0#//ComponentPkg'"
 *        annotation="http://www.polarsys.org/kitalpha/emde/1.0.0/constraintMapping
 *        Mapping='platform:/plugin/org.polarsys.capella.core.data.gen/model/CompositeStructure.ecore#//Component
 *        platform:/plugin/org.polarsys.capella.core.data.gen/model/CompositeStructure.ecore#//ComponentPkg'"
 * @generated
 */

public interface CSConfiguration extends NamedElement, ElementExtension {

  /**
   * Returns the value of the '<em><b>Supported Modes</b></em>' reference list. The list contents are of type
   * {@link org.polarsys.capella.core.data.capellacommon.AbstractState}.
   * 
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Supported Modes</em>' reference list isn't clear, there really should be more of a
   * description here...
   * </p>
   * <!-- end-user-doc -->
   * 
   * @return the value of the '<em>Supported Modes</em>' reference list.
   * @see org.polarsys.capella.vp.ms.MsPackage#getCSConfiguration_SupportedModes()
   * @model
   * @generated
   */

  EList<AbstractState> getSupportedModes();

  /**
   * Returns the value of the '<em><b>Included</b></em>' reference list. The list contents are of type
   * {@link org.polarsys.capella.common.data.modellingcore.ModelElement}.
   * 
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @return the value of the '<em>Included</em>' reference list.
   * @see org.polarsys.capella.vp.ms.MsPackage#getCSConfiguration_Included()
   * @model
   * @generated
   */

  EList<ModelElement> getIncluded();

  /**
   * Returns the value of the '<em><b>Excluded</b></em>' reference list. The list contents are of type
   * {@link org.polarsys.capella.common.data.modellingcore.ModelElement}.
   * 
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @return the value of the '<em>Excluded</em>' reference list.
   * @see org.polarsys.capella.vp.ms.MsPackage#getCSConfiguration_Excluded()
   * @model
   * @generated
   */

  EList<ModelElement> getExcluded();

  /**
   * Returns the value of the '<em><b>Child Configurations</b></em>' reference list. The list contents are of type
   * {@link org.polarsys.capella.vp.ms.CSConfiguration}.
   * 
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Child Configurations</em>' reference list isn't clear, there really should be more of a
   * description here...
   * </p>
   * <!-- end-user-doc -->
   * 
   * @return the value of the '<em>Child Configurations</em>' reference list.
   * @see org.polarsys.capella.vp.ms.MsPackage#getCSConfiguration_ChildConfigurations()
   * @model
   * @generated
   */

  EList<CSConfiguration> getChildConfigurations();

  /**
   * Returns the value of the '<em><b>Kind</b></em>' attribute. The literals are from the enumeration
   * {@link org.polarsys.capella.vp.ms.kind_Type}.
   * 
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Kind</em>' attribute isn't clear, there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * 
   * @return the value of the '<em>Kind</em>' attribute.
   * @see org.polarsys.capella.vp.ms.kind_Type
   * @see #setKind(kind_Type)
   * @see org.polarsys.capella.vp.ms.MsPackage#getCSConfiguration_Kind()
   * @model
   * @generated
   */

  kind_Type getKind();

  /**
   * Sets the value of the '{@link org.polarsys.capella.vp.ms.CSConfiguration#getKind <em>Kind</em>}' attribute.
   * 
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @param value
   *          the new value of the '<em>Kind</em>' attribute.
   * @see org.polarsys.capella.vp.ms.kind_Type
   * @see #getKind()
   * @generated
   */

  void setKind(kind_Type value);

  /**
   * Returns the value of the '<em><b>Access</b></em>' attribute. The default value is <code>"full"</code>. The literals
   * are from the enumeration {@link org.polarsys.capella.vp.ms.access_Type}.
   * 
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Access</em>' attribute isn't clear, there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * 
   * @return the value of the '<em>Access</em>' attribute.
   * @see org.polarsys.capella.vp.ms.access_Type
   * @see #setAccess(access_Type)
   * @see org.polarsys.capella.vp.ms.MsPackage#getCSConfiguration_Access()
   * @model default="full"
   * @generated
   */

  access_Type getAccess();

  /**
   * Sets the value of the '{@link org.polarsys.capella.vp.ms.CSConfiguration#getAccess <em>Access</em>}' attribute.
   * 
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @param value
   *          the new value of the '<em>Access</em>' attribute.
   * @see org.polarsys.capella.vp.ms.access_Type
   * @see #getAccess()
   * @generated
   */

  void setAccess(access_Type value);

  /**
   * Returns the value of the '<em><b>Context</b></em>' reference list. The list contents are of type
   * {@link org.polarsys.capella.vp.ms.Situation}.
   * 
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Context</em>' reference list isn't clear, there really should be more of a description
   * here...
   * </p>
   * <!-- end-user-doc -->
   * 
   * @return the value of the '<em>Context</em>' reference list.
   * @see org.polarsys.capella.vp.ms.MsPackage#getCSConfiguration_Context()
   * @model
   * @generated
   */

  EList<Situation> getContext();

  /**
   * Returns the value of the '<em><b>Compare To</b></em>' reference list. The list contents are of type
   * {@link org.polarsys.capella.vp.ms.CSConfiguration}.
   * 
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Compare To</em>' reference list isn't clear, there really should be more of a
   * description here...
   * </p>
   * <!-- end-user-doc -->
   * 
   * @return the value of the '<em>Compare To</em>' reference list.
   * @see org.polarsys.capella.vp.ms.MsPackage#getCSConfiguration_CompareTo()
   * @model
   * @generated
   */

  EList<CSConfiguration> getCompareTo();

  /**
   * 
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @model kind="operation"
   * @generated
   */

  EList<ModelElement> getScope();

  /**
   * 
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @model
   * @generated
   */

  <T extends ModelElement> EList<T> getScope(Class<T> type);

} // CSConfiguration
