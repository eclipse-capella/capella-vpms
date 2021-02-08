package org.polarsys.capella.vp.ms.migration;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.polarsys.capella.common.ef.ExecutionManager;
import org.polarsys.capella.core.data.migration.context.MigrationContext;
import org.polarsys.capella.core.data.migration.contribution.AbstractMigrationContribution;
import org.polarsys.capella.vp.ms.CSConfiguration;
import org.polarsys.capella.vp.ms.MsPackage;

public class MsMigrationContribution extends AbstractMigrationContribution {

  Collection<CSConfiguration> exclusionConfigs = new HashSet<>();

  @Override
  public EStructuralFeature getFeature(EObject object, String prefix, String name, boolean isElement) {
    if (object instanceof CSConfiguration && "elements".equals(name)) {
      return MsPackage.Literals.CS_CONFIGURATION__INCLUDED;
    }
    return super.getFeature(object, prefix, name, isElement);
  }

  @Override
  public void postMigrationExecute(ExecutionManager executionManager, ResourceSet resourceSet,
      MigrationContext context) {
    for (CSConfiguration e : exclusionConfigs) {
      e.getExcluded().addAll(e.getIncluded());
      e.getIncluded().clear();
    }
  }

  @Override
  public void contributePackageRegistry(Registry packageRegistry, MigrationContext context) {
    // we load the last version of the viewpoint with the latest version
    packageRegistry.put("http://www.polarsys.org/capella/ms", MsPackage.eINSTANCE);
  }

  @Override
  public boolean ignoreUnknownFeature(String prefix, String name, boolean isElement, EObject peekObject, String value,
      XMLResource resource, MigrationContext context) {
    if (peekObject instanceof CSConfiguration && "selector".equals(name)) {
      if ("exclusion".equals(value)) {
        exclusionConfigs.add((CSConfiguration) peekObject);
      }
      return true;
    }
    return false;
  }

}
