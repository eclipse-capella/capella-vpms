/*******************************************************************************
 * Copyright (c) 2006, 2023 THALES GLOBAL SERVICES.
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
package ms.design;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.WorkspaceImage;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNode4EditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.WorkspaceImageEditPart;
import org.polarsys.kitalpha.sirius.rotativeimage.editpart.RotativeDNode4EditPart;
import org.polarsys.kitalpha.sirius.rotativeimage.editpart.RotativeImageEditPart;

/**
 * Specific Edit Part Provider for rotative image
 */
public class RotativeImageEditPartProvider extends AbstractEditPartProvider {

  @Override
  protected Class<?> getNodeEditPartClass(final View view) {
    String type = view.getType();

    if (type != null) {
      EObject resolvedSemanticElement = ViewUtil.resolveSemanticElement(view);
      if (resolvedSemanticElement != null) {

        if (String.valueOf(WorkspaceImageEditPart.VISUAL_ID).equals(type)) {
          if (resolvedSemanticElement instanceof WorkspaceImage) {
            final WorkspaceImage customStyle = (WorkspaceImage) resolvedSemanticElement;
            if (isRotative(customStyle)) {
              return RotativeImageEditPart.class;
            }
          }
        } else if (String.valueOf(DNode4EditPart.VISUAL_ID).equals(type)) {
          if (resolvedSemanticElement instanceof DNode) {
            
            DNode spec = (DNode) resolvedSemanticElement;
            if (spec.getOwnedStyle() != null && spec.getOwnedStyle() instanceof WorkspaceImage) {
              if (isRotative((WorkspaceImage) spec.getOwnedStyle())) {
                return RotativeDNode4EditPart.class;
              }
            }
          }
        }
      }
    }

    return super.getNodeEditPartClass(view);
  }

  private boolean isRotative(WorkspaceImage ownedStyle_p) {
    IPath path = new Path(ownedStyle_p.getWorkspacePath());
    if (path.segmentCount() >= 2) {
      String folder = path.segment(path.segmentCount() - 2);
      return "vpms-rotating".equals(folder);
    }
    return false;
  }

}
