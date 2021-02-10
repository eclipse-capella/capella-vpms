/*******************************************************************************
 * Copyright (c) 2018, 2020 THALES GLOBAL SERVICES.
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
package org.polarsys.capella.vp.ms.ui.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.polarsys.capella.common.ef.command.AbstractReadWriteCommand;
import org.polarsys.capella.core.business.queries.IBusinessQuery;
import org.polarsys.capella.core.data.core.properties.sections.NamedElementSection;
import org.polarsys.capella.core.model.handler.helpers.CapellaAdapterHelper;
import org.polarsys.capella.core.ui.properties.controllers.AbstractMultipleSemanticFieldController;
import org.polarsys.capella.core.ui.properties.controllers.IMultipleSemanticFieldController;
import org.polarsys.capella.core.ui.properties.fields.AbstractSemanticField;
import org.polarsys.capella.core.ui.properties.fields.MultipleSemanticField;
import org.polarsys.capella.vp.ms.CSConfiguration;
import org.polarsys.capella.vp.ms.MsPackage;
import org.polarsys.capella.vp.ms.provider.MsEditPlugin;

public class CSConfigurationSection extends NamedElementSection {

  private MultipleSemanticField includedField;
  private MultipleSemanticField excludedField;

  private MultipleSemanticField contextField;

  private MultipleSemanticField childConfigurationsField;

  private KindGroup kindGroup;

  @Override
  public boolean select(Object toTest) {
    EObject obj = CapellaAdapterHelper.resolveSemanticObject(toTest);

    return obj != null && obj.eClass().equals(org.polarsys.capella.vp.ms.MsPackage.eINSTANCE.getCSConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
    super.createControls(parent, aTabbedPropertySheetPage);

    boolean displayedInWizard = isDisplayedInWizard();

    kindGroup = new KindGroup(rootParentComposite, getWidgetFactory(),
        MsEditPlugin.INSTANCE.getString("_UI_CSConfiguration_kind_feature"), 2); //$NON-NLS-1$


    includedField = new MultipleSemanticField(getReferencesGroup(),
        MsEditPlugin.INSTANCE.getString("_UI_CSConfiguration_included_feature"), getWidgetFactory(), new ItemProviderFieldController() {
      @Override
      protected void doAddOperationInWriteOpenValues(EObject semanticElement, EStructuralFeature semanticFeature,
          EObject object) {
        super.doAddOperationInWriteOpenValues(semanticElement, semanticFeature, object);
        doRemoveOperationInWriteOpenValues(semanticElement, MsPackage.Literals.CS_CONFIGURATION__EXCLUDED, object);
        excludedField.loadData(semanticElement); // refresh hack...
      }
      
      
      
    });
    includedField.setDisplayedInWizard(displayedInWizard);

    excludedField = new MultipleSemanticField(getReferencesGroup(),
        MsEditPlugin.INSTANCE.getString("_UI_CSConfiguration_excluded_feature"), getWidgetFactory(), new ItemProviderFieldController() {
          @Override
          protected void doAddOperationInWriteOpenValues(EObject semanticElement, EStructuralFeature semanticFeature,
              EObject object) {
            super.doAddOperationInWriteOpenValues(semanticElement, semanticFeature, object);
            doRemoveOperationInWriteOpenValues(semanticElement, MsPackage.Literals.CS_CONFIGURATION__INCLUDED, object);
            includedField.loadData(semanticElement); // refresh hack..
          }
    });
    excludedField.setDisplayedInWizard(displayedInWizard);

    childConfigurationsField = new MultipleSemanticField(getReferencesGroup(),
        MsEditPlugin.INSTANCE.getString("_UI_CSConfiguration_childConfigurations_feature"), getWidgetFactory(), //$NON-NLS-1$
        new ItemProviderFieldController());

    contextField = new MultipleSemanticField(getReferencesGroup(),
        MsEditPlugin.INSTANCE.getString("_UI_CSConfiguration_context_feature"), getWidgetFactory(), //$NON-NLS-1$
        new ItemProviderFieldController());

  }

  /**
   * @see org.polarsys.capella.core.ui.properties.sections.AbstractSection#loadData(org.polarsys.capella.core.data.capellacore.CapellaElement)
   */
  @Override
  public void loadData(EObject capellaElement) {
    super.loadData(capellaElement);
  
    includedField.loadData(capellaElement, MsPackage.Literals.CS_CONFIGURATION__INCLUDED);
    excludedField.loadData(capellaElement, MsPackage.Literals.CS_CONFIGURATION__EXCLUDED);
    childConfigurationsField.loadData(capellaElement, MsPackage.Literals.CS_CONFIGURATION__CHILD_CONFIGURATIONS);
    kindGroup.loadData(capellaElement, MsPackage.Literals.CS_CONFIGURATION__KIND);
    contextField.loadData(capellaElement, MsPackage.Literals.CS_CONFIGURATION__CONTEXT);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<AbstractSemanticField> getSemanticFields() {
    List<AbstractSemanticField> fields = new ArrayList<AbstractSemanticField>();
    fields.addAll(super.getSemanticFields());
    fields.add(includedField);
    fields.add(excludedField);
    fields.add(kindGroup);
    fields.add(childConfigurationsField);
    fields.add(contextField);
    return fields;
  }

  static class DerivedMultipleSemanticField extends MultipleSemanticField {

    final EStructuralFeature delegate;

    public DerivedMultipleSemanticField(Composite parent, String label, TabbedPropertySheetWidgetFactory widgetFactory,
        IMultipleSemanticFieldController controller, EStructuralFeature delegate) {
      super(parent, label, widgetFactory, controller);
      this.delegate = delegate;
    }

    @Override
    protected void removeAllDataValue(final EObject object, final EStructuralFeature feature) {
      AbstractReadWriteCommand command = new AbstractReadWriteCommand() {
        @Override
        public void run() {
          ((Collection<?>) (((CSConfiguration) object).eGet(delegate))).removeAll((Collection<?>) object.eGet(feature));
        }
      };
      executeCommand(command);
    }
  }

  /**
   * This is a field controller that delegates reading the list of available values to the elements
   * ItemPropertyDescriptor implementation.
   */
  static class ItemProviderFieldController extends AbstractMultipleSemanticFieldController {

    @Override
    public List<EObject> readOpenValues(EObject semanticElement, EStructuralFeature semanticFeature,
        boolean available) {
      List<EObject> current = loadValues(semanticElement, semanticFeature);
      List<EObject> result = null;
      if (available) {
        AdapterFactoryEditingDomain domain = (AdapterFactoryEditingDomain) AdapterFactoryEditingDomain
            .getEditingDomainFor(semanticElement);
        IItemPropertySource propertySource = (IItemPropertySource) domain.getAdapterFactory().adapt(semanticElement,
            IItemPropertySource.class);
        IItemPropertyDescriptor descriptor = propertySource.getPropertyDescriptor(semanticElement, semanticFeature);
        result = new ArrayList<EObject>((Collection<? extends EObject>) descriptor.getChoiceOfValues(semanticElement));
        result.removeAll(current);
      } else {
        result = current;
      }
      return result;
    }

    @Override
    protected IBusinessQuery getReadOpenValuesQuery(EObject semanticElement) {
      throw new UnsupportedOperationException();
    }

  }

}
