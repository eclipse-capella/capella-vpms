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

import static org.eclipse.e4.ui.css.swt.helpers.ThemeElementDefinitionHelper.normalizeId;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.eclipse.e4.ui.css.core.css2.CSS2ColorHelper;
import org.eclipse.e4.ui.css.core.css2.CSS2RGBColorImpl;
import org.eclipse.e4.ui.internal.css.swt.CSSActivator;
import org.eclipse.e4.ui.internal.css.swt.definition.IColorAndFontProvider;
import org.eclipse.sirius.viewpoint.RGBValues;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.RGBA;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.RGBColor;

public class CSSSiriusColorHelper {

  public static final String COLOR_DEFINITION_MARKER = "#";

  private static final Pattern HEX_COLOR_VALUE_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");

  private static Field[] cachedFields;

  /*--------------- SWT Color Helper -----------------*/

  public static RGBValues getSiriusColor(RGBColor rgbColor) {
    return getRGBA(rgbColor);
  }

  public static RGBValues getSiriusColor(CSSValue value) {
    if (value.getCssValueType() != CSSValue.CSS_PRIMITIVE_VALUE) {
      return null;
    }

    RGBValues rgba = getRGBA((CSSPrimitiveValue) value);
    return rgba;
  }

  public static boolean hasColorDefinitionAsValue(CSSValue value) {
    if (value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
      CSSPrimitiveValue primitiveValue = (CSSPrimitiveValue) value;
      if (primitiveValue.getPrimitiveType() == CSSPrimitiveValue.CSS_STRING) {
        return hasColorDefinitionAsValue(primitiveValue
            .getStringValue());
      }
    }
    return false;
  }

  public static boolean hasColorDefinitionAsValue(String name) {
    if (name.startsWith(COLOR_DEFINITION_MARKER)) {
      return !HEX_COLOR_VALUE_PATTERN.matcher(name).matches();
    }
    return false;
  }

  /**
   * Process the given string and return a corresponding RGBA object.
   *
   * @param value
   *            the SWT constant <code>String</code>
   * @return the value of the SWT constant, or <code>SWT.COLOR_BLACK</code> if
   *         it could not be determined
   */
  private static RGBA process(Display display, String value) {
    Field [] fields = getFields();
    try {
      for (Field field : fields) {
        if (field.getName().equals(value)) {
          return display.getSystemColor(field.getInt(null)).getRGBA();
        }
      }
    } catch (IllegalArgumentException e) {
      // no op - shouldnt happen. We check for static before calling
      // getInt(null)
    } catch (IllegalAccessException e) {
      // no op - shouldnt happen. We check for public before calling
      // getInt(null)
    }
    return  display.getSystemColor(SWT.COLOR_BLACK).getRGBA();
  }

  /**
   * Get the SWT constant fields.
   *
   * @return the fields
   * @since 3.3
   */
  private static Field[] getFields() {
    if (cachedFields == null) {
      Class<?> clazz = SWT.class;
      Field[] allFields = clazz.getDeclaredFields();
      ArrayList<Field> applicableFields = new ArrayList<Field>(
          allFields.length);

      for (Field field : allFields) {
        if (field.getType() == Integer.TYPE
            && Modifier.isStatic(field.getModifiers())
            && Modifier.isPublic(field.getModifiers())
            && Modifier.isFinal(field.getModifiers())
            && field.getName().startsWith("COLOR")) { //$NON-NLS-1$

          applicableFields.add(field);
        }
      }
      cachedFields = applicableFields.toArray(new Field [applicableFields.size()]);
    }
    return cachedFields;
  }

  public static RGBValues getRGBA(String name) {
    RGBColor color = CSS2ColorHelper.getRGBColor(name);
    if (color != null) {
      return getRGBA(color);
    }
    return null;
  }

  public static RGBValues getRGBA(RGBColor color) {
    return RGBValues.create((int) color.getRed().getFloatValue(
        CSSPrimitiveValue.CSS_NUMBER), (int) color.getGreen()
        .getFloatValue(CSSPrimitiveValue.CSS_NUMBER), (int) color
        .getBlue().getFloatValue(CSSPrimitiveValue.CSS_NUMBER));
  }

  public static RGBValues getRGBA(CSSValue value) {
    if (value.getCssValueType() != CSSValue.CSS_PRIMITIVE_VALUE) {
      return null;
    }
    return getRGBA((CSSPrimitiveValue) value);
  }

  public static RGBValues getRGBA(CSSPrimitiveValue value) {
    RGBValues rgba = null;
    switch (value.getPrimitiveType()) {
    case CSSPrimitiveValue.CSS_IDENT:
    case CSSPrimitiveValue.CSS_STRING:
      String string = value.getStringValue();
      rgba = getRGBA(string);
      break;
    case CSSPrimitiveValue.CSS_RGBCOLOR:
      RGBColor rgbColor = value.getRGBColorValue();
      rgba = getRGBA(rgbColor);
      break;
    }
    return rgba;
  }

  public static Integer getPercent(CSSPrimitiveValue value) {
    int percent = 0;
    switch (value.getPrimitiveType()) {
    case CSSPrimitiveValue.CSS_PERCENTAGE:
      percent = (int) value
      .getFloatValue(CSSPrimitiveValue.CSS_PERCENTAGE);
    }
    return Integer.valueOf(percent);
  }

//  public static Gradient getGradient(CSSValueList list, Display display) {
//    Gradient gradient = new Gradient();
//    for (int i = 0; i < list.getLength(); i++) {
//      CSSValue value = list.item(i);
//      if (value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
//        short primType = ((CSSPrimitiveValue) value).getPrimitiveType();
//
//        if (primType == CSSPrimitiveValue.CSS_IDENT) {
//          if (value.getCssText().equals("gradient")) {
//            // Skip the keyword "gradient"
//            continue;
//          } else if (value.getCssText().equals("linear")) {
//            gradient.setLinear(true);
//            continue;
//          } else if (value.getCssText().equals("radial")) {
//            gradient.setLinear(false);
//            continue;
//          }
//        }
//
//        switch (primType) {
//        case CSSPrimitiveValue.CSS_IDENT:
//        case CSSPrimitiveValue.CSS_STRING:
//        case CSSPrimitiveValue.CSS_RGBCOLOR:
//          RGBA rgba = getRGBA((CSSPrimitiveValue) value, display);
//          if (rgba != null) {
//            // note that in this call we lose the RGBA alpha
//            // component - we do currently not support alpha
//            // gradients
//            gradient.addRGB(rgba, (CSSPrimitiveValue) value);
//          } else {
//            //check for vertical gradient
//            gradient.setVertical(!value.getCssText().equals("false"));
//          }
//          break;
//        case CSSPrimitiveValue.CSS_PERCENTAGE:
//          gradient.addPercent(getPercent((CSSPrimitiveValue) value));
//          break;
//        }
//      }
//    }
//    return gradient;
//  }

//  @SuppressWarnings("rawtypes")
//  public static Color[] getSWTColors(Gradient grad, Display display,
//      CSSEngine engine) throws Exception {
//    List values = grad.getValues();
//    Color[] colors = new Color[values.size()];
//
//    for (int i = 0; i < values.size(); i++) {
//      CSSPrimitiveValue value = (CSSPrimitiveValue) values.get(i);
//      //We rely on the fact that when a gradient is created, it's colors are converted and in the registry
//      //TODO see bug #278077
//      Color color = (Color) engine.convert(value, Color.class, display);
//      colors[i] = color;
//    }
//    return colors;
//  }
//
//  public static int[] getPercents(Gradient grad) {
//    // There should be exactly one more RGBs. than percent,
//    // in which case just return the percents as array
//    if (grad.getRGBs().size() == grad.getPercents().size() + 1) {
//      int[] percents = new int[grad.getPercents().size()];
//      for (int i = 0; i < percents.length; i++) {
//        int value = (grad.getPercents().get(i)).intValue();
//        if (value < 0 || value > 100) {
//          // TODO this should be an exception because bad source
//          // format
//          return getDefaultPercents(grad);
//        }
//        percents[i] = value;
//      }
//      return percents;
//    } else {
//      // We can get here if either:
//      // A: the percents are empty (legal) or
//      // B: size mismatches (error)
//      // TODO this should be an exception because bad source format
//
//      return getDefaultPercents(grad);
//    }
//  }
//
//  /*
//   * Compute and return a default array of percentages based on number of
//   * colors o If two colors, {100} o if three colors, {50, 100} o if four
//   * colors, {33, 67, 100}
//   */
//  private static int[] getDefaultPercents(Gradient grad) {
//    // Needed to avoid /0 in increment calc
//
//    if (grad.getRGBs().size() <= 1) {
//      return new int[0];
//    }
//
//    int[] percents = new int[grad.getRGBs().size() - 1];
//    float increment = 100f / (grad.getRGBs().size() - 1);
//
//    for (int i = 0; i < percents.length; i++) {
//      percents[i] = Math.round((i + 1) * increment);
//    }
//    return percents;
//  }

  public static RGBColor getRGBColor(RGBValues color) {
    int red = color.getRed();
    int green = color.getGreen();
    int blue = color.getBlue();
    return new CSS2RGBColorImpl(red, green, blue);
  }

  private static RGBValues findColorByDefinition(String name) {
    IColorAndFontProvider provider = CSSActivator.getDefault().getColorAndFontProvider();
    if (provider != null) {
      RGB rgb = provider.getColor(normalizeId(name.substring(1)));
      if (rgb != null) {
        return RGBValues.create(rgb.red, rgb.green, rgb.blue);
      }
    }
    return null;
  }

  /** Simplify testing for color equality */
  public static boolean equals(Color c1, Color c2) {
    if (c1 == c2) {
      return true;
    }
    if (c1 == null || c2 == null) {
      return false;
    }
    return c1.equals(c2);
  }
  
}
