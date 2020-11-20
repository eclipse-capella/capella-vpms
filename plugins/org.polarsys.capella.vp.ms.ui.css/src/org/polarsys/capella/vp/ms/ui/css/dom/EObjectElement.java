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

import org.eclipse.e4.ui.css.core.dom.ElementAdapter;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.polarsys.capella.vp.ms.ui.css.CSSAdapter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EObjectElement extends ElementAdapter implements NodeList {

  public EObjectElement(EObject nativeWidget, CSSEngine engine) {
    super(nativeWidget, engine);
  }

  @Override
  public Node getParentNode() {
    return engine.getElement(((EObject)getNativeWidget()).eContainer());
  }

  @Override
  public NodeList getChildNodes() {
    return this;
  }

  @Override
  public String getNamespaceURI() {
    return ((EObject) getNativeWidget()).eClass().getEPackage().getNsURI();
  }

  @Override
  public String getCSSId() {
    return EcoreUtil.getID((EObject) getNativeWidget());
  }

  @Override
  public String getCSSClass() {
    CSSAdapter eObjectCss = (CSSAdapter) EcoreUtil.getExistingAdapter((EObject)getNativeWidget(), CSSAdapter.class);
    return eObjectCss == null ? null : eObjectCss.getCSSClass();
  }

  @Override
  public String getCSSStyle() {
    CSSAdapter eObjectCss = (CSSAdapter) EcoreUtil.getExistingAdapter((EObject)getNativeWidget(), CSSAdapter.class);
    return eObjectCss == null ? null : eObjectCss.getCSSStyle();
  }

  @Override
  public String getLocalName() {
    return ((EObject) getNativeWidget()).eClass().getName();
  }

  @Override
  public String getAttribute(String attr) {

    if ("class".equals(attr)) {
      return getCSSClass();
    }

    // must be careful here: 
    // in dom, if the attribute is the default, we must return null, not the default
    // e.g. to match Component[actor]
    EObject widget = (EObject) getNativeWidget();
    for (EAttribute ea : widget.eClass().getEAllAttributes()) {
      if (attr.equals(ea.getName())){
        if (widget.eIsSet(ea)) {
          return EcoreUtil.convertToString(ea.getEAttributeType(), widget.eGet(ea));
        }
      }
    }
    return null;
  }

  @Override
  public final boolean hasAttribute(String attr) {
    return getAttribute(attr) != null;
  }

  @Override
  // children size
  public int getLength() {
    return ((EObject)getNativeWidget()).eContents().size();
  }

  @Override
  // child at i
  public Node item(int index) {
    return getElement(((EObject)getNativeWidget()).eContents().get(index));
  }

}
