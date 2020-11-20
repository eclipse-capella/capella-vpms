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
package org.polarsys.capella.vp.ms.ui.css.dom;

import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.w3c.dom.Node;

public class DSemanticDiagramElement extends DSemanticDecoratorElement {

  public DSemanticDiagramElement(DSemanticDecorator nativeWidget, CSSEngine engine) {
    super(nativeWidget, engine);
  }

  @Override
  public Node item(int index) {
    return 
        getElement(((DDiagram) getNativeWidget()).getOwnedDiagramElements().get(index));
  }

  @Override
  public int getLength() {
    DDiagram diag = (DDiagram) getNativeWidget();
    return diag.getOwnedDiagramElements().size();
  }

}
