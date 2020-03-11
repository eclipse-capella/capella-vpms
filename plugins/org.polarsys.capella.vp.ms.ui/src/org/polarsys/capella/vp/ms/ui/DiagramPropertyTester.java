package org.polarsys.capella.vp.ms.ui;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.sirius.table.metamodel.table.DTable;
import org.eclipse.sirius.table.ui.tools.api.editor.DTableEditor;

public class DiagramPropertyTester extends PropertyTester {
  
  @Override
  public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
    if ("description".equals(property)) {
      if (receiver instanceof DTableEditor) {
        final DTableEditor editor = (DTableEditor) receiver;
        DTable table = (DTable) editor.getRepresentation();
        return expectedValue.equals(table.getDescription().getName());
      }
      if (receiver instanceof DDiagramEditor) {
        final DDiagramEditor editor = (DDiagramEditor) receiver;
        final DDiagram diagram = (DDiagram) editor.getRepresentation();
        return expectedValue.equals(diagram.getDescription().getName());
      }
      return false;
    }
    throw new IllegalArgumentException("Unknown Property: " + property);
  }
  


}
