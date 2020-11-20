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
package org.polarsys.capella.vp.ms.ui.css.helpers;

import org.eclipse.e4.ui.css.core.css2.CSS2ColorHelper;
import org.eclipse.e4.ui.css.core.dom.properties.converters.AbstractCSSValueConverter;
import org.eclipse.e4.ui.css.core.dom.properties.converters.ICSSValueConverter;
import org.eclipse.e4.ui.css.core.dom.properties.converters.ICSSValueConverterConfig;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.helpers.CSSSWTColorHelper;
import org.eclipse.sirius.viewpoint.RGBValues;
import org.eclipse.swt.graphics.RGB;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.RGBColor;

public class CSSValueSiriusRGBConverterImpl extends AbstractCSSValueConverter {

  public static final ICSSValueConverter INSTANCE = new CSSValueSiriusRGBConverterImpl();

  public CSSValueSiriusRGBConverterImpl() {
    super(RGBValues.class);
  }

  @Override
  public Object convert(CSSValue value, CSSEngine engine, Object context)
      throws Exception {
    return CSSSiriusColorHelper.getRGBA(value);
  }

  @Override
  public String convert(Object value, CSSEngine engine, Object context,
      ICSSValueConverterConfig config) throws Exception {
    RGBValues color = (RGBValues) value;
    RGBColor rgbColor = CSSSiriusColorHelper.getRGBColor(color);
    return CSS2ColorHelper.getColorStringValue(rgbColor, config);
  }

}
