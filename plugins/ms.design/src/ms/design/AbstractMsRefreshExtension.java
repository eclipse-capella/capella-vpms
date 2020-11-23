/*******************************************************************************
 * Copyright (c) 2018, 2020 THALES GLOBAL SERVICES.
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

import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtension;

import ms.configuration.services.cs.CsConfigurationServices;

abstract class AbstractMsRefreshExtension implements IRefreshExtension {

  protected final CsConfigurationServices cs = new CsConfigurationServices();
  protected final CSSRefreshExtension css;
  
  AbstractMsRefreshExtension(CSSRefreshExtension css){
    this.css = css;
  }
  
  @Override
  public void beforeRefresh(DDiagram dDiagram) {    
  }

  /**
   * Finds or creates the style object for the given element
   */
  protected final CSConfigurationStyle getCSConfigurationStyle(DDiagramElement view) {
    return css.getCSConfigurationStyle(view);
  }


}
