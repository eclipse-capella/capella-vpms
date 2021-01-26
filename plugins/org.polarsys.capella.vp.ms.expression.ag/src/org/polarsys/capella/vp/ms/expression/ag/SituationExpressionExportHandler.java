/*******************************************************************************
 * Copyright (c) 2020, 2021 THALES GLOBAL SERVICES.
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
package org.polarsys.capella.vp.ms.expression.ag;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.table.metamodel.table.DColumn;
import org.eclipse.sirius.table.metamodel.table.DLine;
import org.eclipse.sirius.table.metamodel.table.DTable;
import org.eclipse.sirius.table.ui.tools.api.editor.DTableEditor;
import org.eclipse.ui.handlers.HandlerUtil;
import org.polarsys.capella.core.data.capellacommon.StateMachine;
import org.polarsys.capella.vp.ms.Situation;

import com.google.common.collect.Ordering;

public class SituationExpressionExportHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    DTableEditor editor = (DTableEditor) HandlerUtil.getActiveEditor(event);
    ExcelExporter exporter = new ExcelExporter();
    Collection<Situation> toExport = new ArrayList<>();
    DTable table = (DTable) editor.getRepresentation();
    for (DColumn c : table.getColumns()) {
      Situation s = (Situation) c.getTarget();
      if (s.getExpression() != null) {
        toExport.add(s);
      }
    }

    // state machine columns should have same order as they appear in the table
    List<StateMachine> sm = new ArrayList<>();
    
    for (TreeIterator<EObject> it = table.eAllContents(); it.hasNext();) {
      EObject next = it.next();
      if (next instanceof DLine && ((DLine) next).getTarget() instanceof StateMachine) {
        sm.add((StateMachine) ((DLine)next).getTarget());
      }
    }
    
    Comparator<StateMachine> columnOrder = Ordering.explicit(sm);

    Session session = SessionManager.INSTANCE.getSession(table.getTarget());
    URI sessionURI = session.getSessionResource().getURI();

    // make a file that probably doesn't exist yet..
    DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
    String ts = format.format(new Date());
    String excel = sessionURI.trimSegments(1).appendSegment(table.getName() + "_" + ts).appendFileExtension("xlsx").toPlatformString(true);

    IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(excel));

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    try {
      exporter.export(toExport, columnOrder, bytes);
      ByteArrayInputStream inbb = new ByteArrayInputStream(bytes.toByteArray());
      file.create(inbb, true, new NullProgressMonitor());
    } catch (IOException | CoreException e) {
      throw new ExecutionException("IOException during export", e);
    }

    return null;
  }
}
