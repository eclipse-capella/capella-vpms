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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.w3c.dom.Element;

public class DSemanticDecoratorElement extends EObjectElement {

  private EObject target;
  
  public DSemanticDecoratorElement(DSemanticDecorator nativeWidget, CSSEngine engine) {
    super(nativeWidget, engine);
  }

  protected EObject getTarget() {
    return ((DSemanticDecorator)getNativeWidget()).getTarget();
  }

  @Override
  public String getLocalName() {
    return ((EObject) getTarget()).eClass().getName();
  }
  
  @Override
  public String getAttribute(String attr) {

    if ("class".equals(attr)) {
      return super.getAttribute(attr);
    }

    Element targetDom = getElement(getTarget());
    return targetDom.getAttribute(attr);
  }
  
  
  
}
