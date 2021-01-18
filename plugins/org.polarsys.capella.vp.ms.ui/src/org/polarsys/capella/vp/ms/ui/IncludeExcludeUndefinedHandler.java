package org.polarsys.capella.vp.ms.ui;

import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.handlers.HandlerUtil;
import org.polarsys.capella.common.data.modellingcore.ModelElement;
import org.polarsys.capella.common.helpers.TransactionHelper;
import org.polarsys.capella.core.model.handler.helpers.CapellaAdapterHelper;
import org.polarsys.capella.vp.ms.CSConfiguration;
import org.polarsys.capella.vp.ms.MsPackage;

public class IncludeExcludeUndefinedHandler extends AbstractHandler {

  public static final String EXCLUDE = "org.polarsys.capella.vp.ms.ui.excludeUndefined";
  public static final String INCLUDE = "org.polarsys.capella.vp.ms.ui.includeUndefined";

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    event.getApplicationContext();
    
    Collection<EObject> selection = CapellaAdapterHelper.resolveSemanticObjects(
        HandlerUtil.getCurrentStructuredSelection(event).toList());

    EditingDomain domain = TransactionHelper.getEditingDomain(selection);
    
    if (domain != null) {

      CompoundCommand c = new CompoundCommand();

      EReference addReference = null;
      EReference predicateReference = null;

      if (EXCLUDE.equals(event.getCommand().getId())) {
        addReference = MsPackage.Literals.CS_CONFIGURATION__EXCLUDED;
        predicateReference = MsPackage.Literals.CS_CONFIGURATION__INCLUDED;
        c.setLabel("Exclude remaining scope elements"); 
      } else if (INCLUDE.equals(event.getCommand().getId())) {
        addReference = MsPackage.Literals.CS_CONFIGURATION__INCLUDED;
        predicateReference = MsPackage.Literals.CS_CONFIGURATION__EXCLUDED;
        c.setLabel("Include remaining scope elements");
      } else {
        throw new IllegalArgumentException("Unknown command");
      }    
      for (EObject o : selection) {
        CSConfiguration conf = (CSConfiguration) o;
        for (ModelElement s : conf.getScope()) {
          if (!((Collection<?>) conf.eGet(predicateReference)).contains(s)) {
            c.append(AddCommand.create(domain, conf, addReference, s));
          }
        }
      }
      domain.getCommandStack().execute(c);
    }
    return null;
  }

}