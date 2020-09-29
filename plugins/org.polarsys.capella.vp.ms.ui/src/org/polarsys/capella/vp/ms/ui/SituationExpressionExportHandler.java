package org.polarsys.capella.vp.ms.ui;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.csv.CSVFormat;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.sirius.table.metamodel.table.DColumn;
import org.eclipse.sirius.table.metamodel.table.DTable;
import org.eclipse.sirius.table.ui.tools.api.editor.DTableEditor;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;
import org.polarsys.capella.vp.ms.Situation;
import org.polarsys.capella.vp.ms.expression.ag.CSVExporter;

public class SituationExpressionExportHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    DTableEditor editor = (DTableEditor) HandlerUtil.getActiveEditor(event);
    CSVExporter exporter = new CSVExporter(CSVFormat.EXCEL);

    DTable table = (DTable) editor.getRepresentation();
    for (DColumn c : table.getColumns()) {
      Situation s = (Situation) c.getTarget();
      if (s.getExpression() != null) {
        exporter.export(s.getExpression());
      }
    }
    StringWriter writer = new StringWriter();
    try {
      exporter.finish(writer);
    } catch (IOException e) {
      throw new ExecutionException("IOException during export", e);
    }
    
    Display display = HandlerUtil.getActiveWorkbenchWindow(event).getShell().getDisplay();
    Clipboard cb = new Clipboard(display);
    
    TextTransfer textTransfer = TextTransfer.getInstance();
    cb.setContents(new Object[]{writer.toString()}, new Transfer[]{textTransfer});
    cb.dispose();
    return null;
  }
}
