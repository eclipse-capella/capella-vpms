package org.polarsys.capella.vp.ms.ui;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.sirius.table.metamodel.table.DCell;
import org.eclipse.sirius.table.metamodel.table.DColumn;
import org.eclipse.sirius.table.metamodel.table.DLine;
import org.eclipse.sirius.table.ui.tools.api.editor.DTableEditor;
import org.eclipse.sirius.table.ui.tools.internal.editor.DTableCrossEditor;
import org.eclipse.sirius.table.ui.tools.internal.editor.DTableViewerManager;
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

  protected CSConfiguration adapt(Object element, Object evaluationContext) {
    CSConfiguration config = adapt(element, CSConfiguration.class);

    if (config == null) {
      if (element instanceof DCell && ((DCell) element).getColumn().getTarget() instanceof CSConfiguration) {
        config = (CSConfiguration) ((DCell) element).getColumn().getTarget();
      }
      if (element instanceof DLine) {
        
        IEvaluationContext c = (IEvaluationContext) evaluationContext;
        Object editor = c.getVariable("activePart");
        
        if (editor instanceof DTableCrossEditor) {
          DTableCrossEditor deditor = (DTableCrossEditor) editor;
          if (deditor.getTableViewer() instanceof DTableViewerManager) {
            int col = ((DTableViewerManager) deditor.getTableViewer()).getActiveColumn();
            if (col > 0) {
              DColumn dc = deditor.getTableModel().getColumns().get(col - 1);
              if (dc.getTarget() instanceof CSConfiguration) {
                config = (CSConfiguration) dc.getTarget();
              }
            }
          }
        }
      }
    }

    return config;
  }
  
  @Override
  protected boolean acceptSelectionElement(Object element, Object evaluationContext) {
    return adapt(element, evaluationContext) != null;
  }

  @Override
  protected Command createCommand(Collection<?> selection, Object evaluationContext) {    
    CompoundCommand result = new CompoundCommand() {
      @Override
      public Collection<?> getAffectedObjects() {
        return Collections.emptyList();
      }
    };
    Collection<CSConfiguration> seen = new HashSet<>();
    for (Object e : selection) {
      CSConfiguration conf = adapt(e, evaluationContext);
      if (seen.add(conf)) {
        for (ModelElement m : conf.getScope()) {
          if (!conf.getIncluded().contains(m) && !conf.getExcluded().contains(m)) {
            result.append(AddCommand.create(TransactionHelper.getEditingDomain(conf), conf, ref, m));
          }
        }
      }
    }
    return result;
  }

}
