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
package org.polarsys.capella.vp.ms.ui.css.properties;

import org.eclipse.e4.ui.css.core.dom.properties.ICSSPropertyHandler;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.sirius.diagram.BorderedStyle;
import org.eclipse.sirius.diagram.EdgeStyle;
import org.eclipse.sirius.diagram.FlatContainerStyle;
import org.eclipse.sirius.diagram.Square;
import org.eclipse.sirius.viewpoint.BasicLabelStyle;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.sirius.viewpoint.DStylizable;
import org.eclipse.sirius.viewpoint.RGBValues;
import org.eclipse.sirius.viewpoint.Style;
import org.polarsys.capella.vp.ms.ui.css.dom.DSemanticDecoratorElement;
import org.w3c.dom.css.CSSValue;

public class DiagramPropertyHandler implements ICSSPropertyHandler {

  @Override
  public boolean applyCSSProperty(Object element, String property, CSSValue value, String pseudo, CSSEngine engine)
      throws Exception {

    if ("background-color".equals(property)) {
      DSemanticDecoratorElement de = (DSemanticDecoratorElement) element;
      DSemanticDecorator widget = (DSemanticDecorator) de.getNativeWidget();
      if (widget instanceof DStylizable) {
        Style style = ((DStylizable) widget).getStyle();
        if (style instanceof Square) {
          ((Square) style).setColor((RGBValues) engine.convert(value, RGBValues.class, null));
        } else if (style instanceof FlatContainerStyle) {
          ((FlatContainerStyle)style).setBackgroundColor((RGBValues) engine.convert(value, RGBValues.class, null));
        }
      }
    }

    if ("foreground-color".equals(property)) {
      DSemanticDecoratorElement de = (DSemanticDecoratorElement) element;
      DSemanticDecorator widget = (DSemanticDecorator) de.getNativeWidget();
      if (widget instanceof DStylizable) {
        Style style = ((DStylizable) widget).getStyle();
        if (style instanceof Square) {
          ((Square) style).setColor((RGBValues) engine.convert(value, RGBValues.class, null));
        } else if (style instanceof FlatContainerStyle) {
          ((FlatContainerStyle)style).setForegroundColor((RGBValues) engine.convert(value, RGBValues.class, null));
        }
      }
    }

    if ("border-color".equals(property)) {
      DSemanticDecoratorElement de = (DSemanticDecoratorElement) element;
      DSemanticDecorator widget = (DSemanticDecorator) de.getNativeWidget();
      if (widget instanceof DStylizable) {
        Style style = ((DStylizable) widget).getStyle();
        if (style instanceof BorderedStyle) {
          ((BorderedStyle) style).setBorderColor((RGBValues) engine.convert(value, RGBValues.class, null));
        }
      }
    }

    if ("edge-color".equals(property)) {
      DSemanticDecoratorElement de = (DSemanticDecoratorElement) element;
      DSemanticDecorator widget = (DSemanticDecorator) de.getNativeWidget();
      if (widget instanceof DStylizable) {
        Style style = ((DStylizable) widget).getStyle();
        if (style instanceof EdgeStyle) {
          ((EdgeStyle) style).setStrokeColor((RGBValues) engine.convert(value, RGBValues.class, null));
        }
      }
    }

    if ("label-color".equals(property)) {
      DSemanticDecoratorElement de = (DSemanticDecoratorElement) element;
      DSemanticDecorator widget = (DSemanticDecorator) de.getNativeWidget();
      if (widget instanceof DStylizable) {
        Style style = ((DStylizable) widget).getStyle();
        if (style instanceof BasicLabelStyle) {
          ((BasicLabelStyle) style).setLabelColor((RGBValues) engine.convert(value, RGBValues.class, null));
        } else if (style instanceof FlatContainerStyle) {
          ((FlatContainerStyle) style).setLabelColor((RGBValues) engine.convert(value, RGBValues.class, null));
        }
      }      
    }

    return true; //?
  }

}
