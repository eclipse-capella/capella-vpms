package org.polarsys.capella.vp.ms.ui;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.polarsys.capella.common.data.modellingcore.ModelElement;
import org.polarsys.capella.common.helpers.TransactionHelper;
import org.polarsys.capella.vp.ms.CSConfiguration;
import org.polarsys.capella.vp.ms.MsPackage;

public class AddRemainingHandler extends EMFCommandHandler {

  public static class Excluded extends AddRemainingHandler {
    public Excluded() {
      super(MsPackage.Literals.CS_CONFIGURATION__EXCLUDED);
    }
  }

  public static class Included extends AddRemainingHandler {
    public Included() {
      super(MsPackage.Literals.CS_CONFIGURATION__INCLUDED);
    }
  }

  private final EReference ref;

  private AddRemainingHandler(EReference ref) {
    this.ref = ref;
  }

  @Override
  protected boolean acceptSelectionElement(Object element) {
    return element instanceof CSConfiguration;
  }

  protected Command createCommand(Collection<?> selection) {    

    CompoundCommand result = new CompoundCommand() {
      @Override
      public Collection<?> getAffectedObjects() {
        return Collections.emptyList();
      }
    };

    for (Object e : selection) {
      if (!(e instanceof CSConfiguration)) {
        return UnexecutableCommand.INSTANCE;
      }
      CSConfiguration conf = (CSConfiguration) e;
      for (ModelElement m : conf.getScope()) {
        if (!conf.getIncluded().contains(m) && !conf.getExcluded().contains(m)) {
          result.append(AddCommand.create(TransactionHelper.getEditingDomain(conf), conf, ref, m));
        }
      }
    }
    return result;
  }

}
