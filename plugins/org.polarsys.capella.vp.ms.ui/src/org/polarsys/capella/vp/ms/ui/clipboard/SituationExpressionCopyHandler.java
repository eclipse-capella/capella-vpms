/*******************************************************************************
 * Copyright (c) 2023 THALES GLOBAL SERVICES and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.vp.ms.ui.clipboard;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.sirius.table.metamodel.table.DCell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This Service implement 'copy' command in SituationExpressions.
 * 
 * @author ebausson
 */
public class SituationExpressionCopyHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    ISelection selection = HandlerUtil.getCurrentStructuredSelection(event);
    if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
      Object firstElement = ((IStructuredSelection) selection).getFirstElement();
      if (firstElement instanceof DCell) {
        saveCellExpressionToClipboard((DCell) firstElement);
      }
    }
    return null;
  }

  private void saveCellExpressionToClipboard(DCell cell) {
    SituationExpressionClipboardService.setClipboardContent(cell);
  }
  
  @Override
  public boolean isEnabled() {
    boolean result = false;
    ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart().getSite().getSelectionProvider().getSelection();
    if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
      return !((IStructuredSelection) selection).isEmpty();
    }
    return result;
  }

}
