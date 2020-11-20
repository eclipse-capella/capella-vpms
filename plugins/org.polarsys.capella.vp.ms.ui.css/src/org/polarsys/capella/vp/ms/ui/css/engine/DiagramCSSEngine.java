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
package org.polarsys.capella.vp.ms.ui.css.engine;

import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.e4.ui.css.core.dom.parsers.CSSParser;
import org.eclipse.e4.ui.css.core.dom.parsers.CSSParserFactory;
import org.eclipse.e4.ui.css.core.dom.parsers.ICSSParserFactory;
import org.eclipse.e4.ui.css.core.impl.dom.parsers.CSSParserImpl;
import org.eclipse.e4.ui.css.core.impl.engine.CSSEngineImpl;
import org.eclipse.e4.ui.css.core.impl.engine.RegistryCSSPropertyHandlerProvider;
import org.eclipse.e4.ui.css.core.impl.sac.CSSSelectorFactoryImpl;
import org.eclipse.e4.ui.css.swt.properties.converters.CSSValueSWTRGBConverterImpl;
import org.polarsys.capella.vp.ms.ui.css.DiagramElementProvider;
import org.polarsys.capella.vp.ms.ui.css.helpers.CSSValueSiriusRGBConverterImpl;

public class DiagramCSSEngine extends CSSEngineImpl {

  public DiagramCSSEngine() {
    setElementProvider(DiagramElementProvider.INSTANCE);
    
    // Register SWT RGB CSSValue Converter
    registerCSSValueConverter(CSSValueSiriusRGBConverterImpl.INSTANCE);
    
    propertyHandlerProviders.add(new RegistryCSSPropertyHandlerProvider(
        RegistryFactory.getRegistry()));

    
  }

  
  @Override
  public void reapply() {

  }

  
  
}
