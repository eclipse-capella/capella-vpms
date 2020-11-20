/*******************************************************************************
 * Copyright (c) 2020 THALES GLOBAL SERVICES.
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
package org.polarsys.capella.vp.ms.ui.css;

import org.eclipse.e4.ui.css.core.dom.IElementProvider;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.util.DiagramSwitch;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.polarsys.capella.vp.ms.ui.css.dom.DSemanticDecoratorElement;
import org.polarsys.capella.vp.ms.ui.css.dom.DSemanticDiagramElement;
import org.polarsys.capella.vp.ms.ui.css.dom.EObjectElement;
import org.w3c.dom.Element;

public class DiagramElementProvider implements IElementProvider {

  public static final IElementProvider INSTANCE = new DiagramElementProvider();

  @Override
  public Element getElement(Object element, CSSEngine engine) {
    if (element instanceof DSemanticDiagram) {
      return new DSemanticDiagramElement((DSemanticDiagram) element, engine);
    }
    if (element instanceof DSemanticDecorator) {
      return new DSemanticDecoratorElement((DSemanticDecorator) element, engine);
    }
    if (element instanceof EObject) {
      return new EObjectElement((EObject) element, engine);
    }
    return null;
  }
  
  
  
}
