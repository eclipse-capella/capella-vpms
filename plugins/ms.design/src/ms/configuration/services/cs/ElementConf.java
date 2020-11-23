/*******************************************************************************
 * Copyright (c) 2017, 2020 ALTRAN.
 *  
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Altran - initial API and implementation
 *******************************************************************************/
package ms.configuration.services.cs;

import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.vp.ms.CSConfiguration;

public class ElementConf {
  protected EObject element;
  protected CSConfiguration config;

  public ElementConf(EObject element, CSConfiguration config) {
    this.config = config;
    this.element = element;
  }

  public EObject getElement() {
    return this.element;
  }

  public CSConfiguration getConfiguration() {
    return this.config;
  }

}
