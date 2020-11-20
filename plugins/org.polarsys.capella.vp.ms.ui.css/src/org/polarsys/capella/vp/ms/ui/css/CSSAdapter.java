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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class CSSAdapter extends AdapterImpl {

  private Set<String> cssClass = new HashSet<>();
  private String cssStyle;

  public CSSAdapter clear() {
    cssClass.clear();
    cssStyle = null;
    return this;
  }

  public String getCSSClass() {
    if (cssClass.isEmpty()) {
      return null;
    }
    return String.join(" ", cssClass);
  }

  public void addCSSClass(String cssClass) {
    this.cssClass.add(cssClass);
  }
  
  public void addCSSClass(CSSAdapter other) {
    this.cssClass.addAll(other.cssClass);
  }

  public String getCSSStyle() {
    return cssStyle;
  }

  public void setCSSStyle(String cssStyle) {
    this.cssStyle = cssStyle;
  }

  public static CSSAdapter getAdapter(EObject e) {
    CSSAdapter adapter = (CSSAdapter) EcoreUtil.getExistingAdapter(e, CSSAdapter.class);
    if (adapter == null) {
      adapter = new CSSAdapter();
      e.eAdapters().add(adapter);
    }
    return adapter;
  }

  @Override
  public boolean isAdapterForType(Object type) {
    return type == CSSAdapter.class;
  }

}
