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

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.polarsys.capella.common.data.activity.ActivityEdge;
import org.polarsys.capella.common.data.activity.InputPin;
import org.polarsys.capella.common.data.activity.OutputPin;
import org.polarsys.capella.common.data.modellingcore.AbstractInformationFlow;
import org.polarsys.capella.common.data.modellingcore.AbstractType;
import org.polarsys.capella.common.data.modellingcore.InformationsExchanger;
import org.polarsys.capella.common.data.modellingcore.ModelElement;
import org.polarsys.capella.core.data.capellacommon.AbstractState;
import org.polarsys.capella.core.data.capellacore.Feature;
import org.polarsys.capella.core.data.capellacore.impl.NamedElementImpl;
import org.polarsys.capella.core.data.cs.Component;
import org.polarsys.capella.core.data.cs.ComponentPkg;
import org.polarsys.capella.core.data.cs.Part;
import org.polarsys.capella.core.data.cs.PhysicalLink;
import org.polarsys.capella.core.data.cs.PhysicalPort;
import org.polarsys.capella.core.data.fa.AbstractFunction;
import org.polarsys.capella.core.data.fa.ComponentExchange;
import org.polarsys.capella.core.data.fa.ComponentPort;
import org.polarsys.capella.core.data.fa.FunctionInputPort;
import org.polarsys.capella.core.data.fa.FunctionOutputPort;
import org.polarsys.capella.core.data.fa.FunctionalChain;
import org.polarsys.capella.core.data.interaction.InstanceRole;
import org.polarsys.capella.core.data.interaction.Scenario;
import org.polarsys.capella.core.data.la.LogicalComponent;
import org.polarsys.capella.core.data.la.LogicalComponentPkg;
import org.polarsys.capella.core.data.oa.CommunicationMean;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.core.data.pa.PhysicalComponentPkg;
import org.polarsys.capella.vp.ms.CSConfiguration;
import org.polarsys.capella.vp.ms.MsPackage;
import org.polarsys.capella.vp.ms.Situation;
import org.polarsys.capella.vp.ms.access_Type;
import org.polarsys.capella.vp.ms.kind_Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>CS Configuration</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.polarsys.capella.vp.ms.impl.CSConfigurationImpl#getSupportedModes <em>Supported Modes</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.impl.CSConfigurationImpl#getElements <em>Elements</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.impl.CSConfigurationImpl#getDeploymentLinks <em>Deployment Links</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.impl.CSConfigurationImpl#getFunctions <em>Functions</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.impl.CSConfigurationImpl#getFunctionalChains <em>Functional Chains</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.impl.CSConfigurationImpl#getComponents <em>Components</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.impl.CSConfigurationImpl#getPorts <em>Ports</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.impl.CSConfigurationImpl#getSubsetOf <em>Subset Of</em>}</li>
 * <li>{@link org.polarsys.capella.vp.ms.impl.CSConfigurationImpl#getKind <em>Kind</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CSConfigurationImpl extends NamedElementImpl implements CSConfiguration {

  /**
   * The cached value of the '{@link #getSupportedModes() <em>Supported Modes</em>}' reference list. <!-- begin-user-doc
   * --> <!-- end-user-doc -->
   * 
   * @see #getSupportedModes()
   * @generated
   * @ordered
   */
  protected EList<AbstractState> supportedModes;

  /**
   * The cached value of the '{@link #getIncluded() <em>Included</em>}' reference list. <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * 
   * @see #getIncluded()
   * @generated
   * @ordered
   */
  protected EList<ModelElement> included;

  /**
   * The cached value of the '{@link #getExcluded() <em>Excluded</em>}' reference list. <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * 
   * @see #getExcluded()
   * @generated
   * @ordered
   */
  protected EList<ModelElement> excluded;

  /**
   * The cached value of the '{@link #getChildConfigurations() <em>Child Configurations</em>}' reference list. <!--
   * begin-user-doc --> <!-- end-user-doc -->
   * 
   * @see #getChildConfigurations()
   * @generated
   * @ordered
   */
  protected EList<CSConfiguration> childConfigurations;

  /**
   * The default value of the '{@link #getKind() <em>Kind</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
   * -->
   * 
   * @see #getKind()
   * @generated
   * @ordered
   */
  protected static final kind_Type KIND_EDEFAULT = kind_Type.ATOMIC;

  /**
   * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @see #getKind()
   * @generated
   * @ordered
   */
  protected kind_Type kind = KIND_EDEFAULT;

  /**
   * The default value of the '{@link #getAccess() <em>Access</em>}' attribute. <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * 
   * @see #getAccess()
   * @generated
   * @ordered
   */
  protected static final access_Type ACCESS_EDEFAULT = access_Type.FULL;

  /**
   * The cached value of the '{@link #getAccess() <em>Access</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
   * -->
   * 
   * @see #getAccess()
   * @generated
   * @ordered
   */
  protected access_Type access = ACCESS_EDEFAULT;

  /**
   * The cached value of the '{@link #getContext() <em>Context</em>}' reference list. <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * 
   * @see #getContext()
   * @generated
   * @ordered
   */
  protected EList<Situation> context;

  /**
   * The cached value of the '{@link #getCompareTo() <em>Compare To</em>}' reference list. <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * 
   * @see #getCompareTo()
   * @generated
   * @ordered
   */
  protected EList<CSConfiguration> compareTo;

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected CSConfigurationImpl() {

    super();

  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return MsPackage.Literals.CS_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */

  @Override
  public EList<AbstractState> getSupportedModes() {

    if (supportedModes == null) {
      supportedModes = new EObjectResolvingEList<AbstractState>(AbstractState.class, this,
          MsPackage.CS_CONFIGURATION__SUPPORTED_MODES);
    }
    return supportedModes;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */

  @Override
  public EList<ModelElement> getIncluded() {

    if (included == null) {
      included = new EObjectResolvingEList<ModelElement>(ModelElement.class, this,
          MsPackage.CS_CONFIGURATION__INCLUDED);
    }
    return included;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */

  @Override
  public EList<ModelElement> getExcluded() {

    if (excluded == null) {
      excluded = new EObjectResolvingEList<ModelElement>(ModelElement.class, this,
          MsPackage.CS_CONFIGURATION__EXCLUDED);
    }
    return excluded;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */

  @Override
  public EList<CSConfiguration> getChildConfigurations() {

    if (childConfigurations == null) {
      childConfigurations = new EObjectResolvingEList<CSConfiguration>(CSConfiguration.class, this,
          MsPackage.CS_CONFIGURATION__CHILD_CONFIGURATIONS);
    }
    return childConfigurations;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */

  @Override
  public kind_Type getKind() {

    return kind;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */

  @Override
  public void setKind(kind_Type newKind) {

    kind_Type oldKind = kind;
    kind = newKind == null ? KIND_EDEFAULT : newKind;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MsPackage.CS_CONFIGURATION__KIND, oldKind, kind));

  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */

  @Override
  public access_Type getAccess() {

    return access;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */

  @Override
  public void setAccess(access_Type newAccess) {

    access_Type oldAccess = access;
    access = newAccess == null ? ACCESS_EDEFAULT : newAccess;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MsPackage.CS_CONFIGURATION__ACCESS, oldAccess, access));

  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */

  @Override
  public EList<Situation> getContext() {

    if (context == null) {
      context = new EObjectResolvingEList<Situation>(Situation.class, this, MsPackage.CS_CONFIGURATION__CONTEXT);
    }
    return context;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */

  @Override
  public EList<CSConfiguration> getCompareTo() {

    if (compareTo == null) {
      compareTo = new EObjectResolvingEList<CSConfiguration>(CSConfiguration.class, this,
          MsPackage.CS_CONFIGURATION__COMPARE_TO);
    }
    return compareTo;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public EList<ModelElement> getScope() {

    EList<ModelElement> result = new UniqueEList<ModelElement>();
    if (eContainer() instanceof Component) {
      Component parent = (Component) eContainer();
      addComboBoxElements(result, parent);
    } else if (eContainer() instanceof ComponentPkg) {
      for (TreeIterator<EObject> it = eContainer().eAllContents(); it.hasNext();) {
        EObject next = it.next();
        if (next instanceof Component) {
          it.prune();
          addComboBoxElements(result, (Component) next);
          result.add((ModelElement) next);
        }
      }
    }
    return result;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  @SuppressWarnings("unchecked")
  public <T extends ModelElement> EList<T> getScope(Class<T> type) {
    EList<T> result = new BasicEList<T>();
    for (ModelElement e : getScope()) {
      if (type.isInstance(e)) {
        result.add((T) e);
      }
    }
    return result;
  }

  private void handlePart(Collection<ModelElement> result, Part part) {
    AbstractType type = part.getAbstractType();
    result.add(type);
    if (type instanceof Component && getAccess() == access_Type.FULL || getAccess() == access_Type.SUBCOMPONENTS) {
      addComboBoxElements(result, (Component) type);
    }
  }

  private void addComboBoxElements(Collection<ModelElement> result, Component parent) {

    for (Feature f : parent.getOwnedFeatures()) {
      if (f instanceof Part && ((Part) f).getDeployingParts().isEmpty()) {
        handlePart(result, (Part) f);
      }
    }

    Deque<ComponentPkg> toVisit = new ArrayDeque<ComponentPkg>();
    if (parent instanceof PhysicalComponent) {
      toVisit.addAll(((PhysicalComponent) parent).getOwnedPhysicalComponentPkgs());
    } else if (parent instanceof LogicalComponent) {
      toVisit.addAll(((LogicalComponent) parent).getOwnedLogicalComponentPkgs());
    }

    while (!toVisit.isEmpty()) {
      ComponentPkg pkg = toVisit.pop();
      for (Part p : pkg.getOwnedParts()) {
        handlePart(result, p);
      }
      if (pkg instanceof PhysicalComponentPkg) {
        toVisit.addAll(((PhysicalComponentPkg) pkg).getOwnedPhysicalComponentPkgs());
      } else if (pkg instanceof LogicalComponentPkg) {
        toVisit.addAll(((LogicalComponentPkg) pkg).getOwnedLogicalComponentPkgs());
      }
    }

    for (Part representingPart : parent.getRepresentingParts()) {
      for (InstanceRole ir : representingPart.getRepresentingInstanceRoles()) {
        if (ir.eContainer() instanceof Scenario) {
          result.add((Scenario) ir.eContainer());
        }
      }
      if (getAccess() == access_Type.FULL) {
        for (Part deployed : representingPart.getDeployedParts()) {
          handlePart(result, deployed);
        }
      }
    }

    for (AbstractFunction allocated : parent.getAllocatedFunctions()) {

      result.add(allocated);
      for (InstanceRole ir : allocated.getRepresentingInstanceRoles()) {
        if (ir.eContainer() instanceof Scenario) {
          result.add((Scenario) ir.eContainer());
        }
      }

      for (InputPin in : allocated.getInputs()) {
        if (in instanceof FunctionInputPort) {
          result.add(in);
        }
      }

      for (OutputPin out : allocated.getOutputs()) {
        if (out instanceof FunctionOutputPort) {
          result.add(out);
        }
      }

      for (ActivityEdge edge : allocated.getIncoming()) {
        if (result.contains(edge.getSource())) {
          result.add(edge);
        }
      }

      for (ActivityEdge edge : allocated.getOutgoing()) {
        if (result.contains(edge.getTarget())) {
          result.add(edge);
        }
      }

      for (FunctionalChain chain : allocated.getInvolvingFunctionalChains()) {
        result.add(chain);
      }
    }

    for (ComponentPort cp : parent.getContainedComponentPorts()) {
      result.add(cp);

      for (ComponentExchange e : cp.getComponentExchanges()) {
        if (cp == e.getSourcePort()) {
          if (result.contains(e.getTargetPort())) {
            result.add(e);
          }
        } else if (cp == e.getTargetPort()) {
          if (result.contains(e.getSourcePort())) {
            result.add(e);
          }
        }
      }
    }

    for (PhysicalPort pp : parent.getContainedPhysicalPorts()) {
      result.add(pp);
      for (PhysicalLink l : pp.getInvolvedLinks()) {
        if (pp == l.getSourcePhysicalPort()) {
          if (result.contains(l.getTargetPhysicalPort())) {
            result.add(l);
          }
        } else if (pp == l.getTargetPhysicalPort()) {
          if (result.contains(l.getSourcePhysicalPort())) {
            result.add(l);
          }
        }
      }
    }

    if (parent instanceof InformationsExchanger) {
      for (AbstractInformationFlow f : ((InformationsExchanger) parent).getOutgoingInformationFlows()) {
        if (f instanceof CommunicationMean) {
          if (result.contains(((CommunicationMean) f).getTarget())) {
            result.add(f);
          }
        }
      }
      for (AbstractInformationFlow f : ((InformationsExchanger) parent).getIncomingInformationFlows()) {
        if (f instanceof CommunicationMean) {
          if (result.contains(((CommunicationMean) f).getSource())) {
            result.add(f);
          }
        }
      }
    }

  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
    case MsPackage.CS_CONFIGURATION__SUPPORTED_MODES:
      return getSupportedModes();
    case MsPackage.CS_CONFIGURATION__INCLUDED:
      return getIncluded();
    case MsPackage.CS_CONFIGURATION__EXCLUDED:
      return getExcluded();
    case MsPackage.CS_CONFIGURATION__CHILD_CONFIGURATIONS:
      return getChildConfigurations();
    case MsPackage.CS_CONFIGURATION__KIND:
      return getKind();
    case MsPackage.CS_CONFIGURATION__ACCESS:
      return getAccess();
    case MsPackage.CS_CONFIGURATION__CONTEXT:
      return getContext();
    case MsPackage.CS_CONFIGURATION__COMPARE_TO:
      return getCompareTo();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue) {
    switch (featureID) {
    case MsPackage.CS_CONFIGURATION__SUPPORTED_MODES:
      getSupportedModes().clear();
      getSupportedModes().addAll((Collection<? extends AbstractState>) newValue);
      return;
    case MsPackage.CS_CONFIGURATION__INCLUDED:
      getIncluded().clear();
      getIncluded().addAll((Collection<? extends ModelElement>) newValue);
      return;
    case MsPackage.CS_CONFIGURATION__EXCLUDED:
      getExcluded().clear();
      getExcluded().addAll((Collection<? extends ModelElement>) newValue);
      return;
    case MsPackage.CS_CONFIGURATION__CHILD_CONFIGURATIONS:
      getChildConfigurations().clear();
      getChildConfigurations().addAll((Collection<? extends CSConfiguration>) newValue);
      return;
    case MsPackage.CS_CONFIGURATION__KIND:
      setKind((kind_Type) newValue);
      return;
    case MsPackage.CS_CONFIGURATION__ACCESS:
      setAccess((access_Type) newValue);
      return;
    case MsPackage.CS_CONFIGURATION__CONTEXT:
      getContext().clear();
      getContext().addAll((Collection<? extends Situation>) newValue);
      return;
    case MsPackage.CS_CONFIGURATION__COMPARE_TO:
      getCompareTo().clear();
      getCompareTo().addAll((Collection<? extends CSConfiguration>) newValue);
      return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  public void eUnset(int featureID) {
    switch (featureID) {
    case MsPackage.CS_CONFIGURATION__SUPPORTED_MODES:
      getSupportedModes().clear();
      return;
    case MsPackage.CS_CONFIGURATION__INCLUDED:
      getIncluded().clear();
      return;
    case MsPackage.CS_CONFIGURATION__EXCLUDED:
      getExcluded().clear();
      return;
    case MsPackage.CS_CONFIGURATION__CHILD_CONFIGURATIONS:
      getChildConfigurations().clear();
      return;
    case MsPackage.CS_CONFIGURATION__KIND:
      setKind(KIND_EDEFAULT);
      return;
    case MsPackage.CS_CONFIGURATION__ACCESS:
      setAccess(ACCESS_EDEFAULT);
      return;
    case MsPackage.CS_CONFIGURATION__CONTEXT:
      getContext().clear();
      return;
    case MsPackage.CS_CONFIGURATION__COMPARE_TO:
      getCompareTo().clear();
      return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID) {
    switch (featureID) {
    case MsPackage.CS_CONFIGURATION__SUPPORTED_MODES:
      return supportedModes != null && !supportedModes.isEmpty();
    case MsPackage.CS_CONFIGURATION__INCLUDED:
      return included != null && !included.isEmpty();
    case MsPackage.CS_CONFIGURATION__EXCLUDED:
      return excluded != null && !excluded.isEmpty();
    case MsPackage.CS_CONFIGURATION__CHILD_CONFIGURATIONS:
      return childConfigurations != null && !childConfigurations.isEmpty();
    case MsPackage.CS_CONFIGURATION__KIND:
      return kind != KIND_EDEFAULT;
    case MsPackage.CS_CONFIGURATION__ACCESS:
      return access != ACCESS_EDEFAULT;
    case MsPackage.CS_CONFIGURATION__CONTEXT:
      return context != null && !context.isEmpty();
    case MsPackage.CS_CONFIGURATION__COMPARE_TO:
      return compareTo != null && !compareTo.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  public String toString() {
    if (eIsProxy())
      return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (kind: "); //$NON-NLS-1$
    result.append(kind);
    result.append(", access: "); //$NON-NLS-1$
    result.append(access);
    result.append(')');
    return result.toString();
  }

} // CSConfigurationImpl