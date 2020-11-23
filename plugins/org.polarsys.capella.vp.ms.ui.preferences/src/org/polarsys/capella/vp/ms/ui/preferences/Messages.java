/*******************************************************************************
 * Copyright (c) 2017, 2020 THALES GLOBAL SERVICES.
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
package org.polarsys.capella.vp.ms.ui.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages {

  private static final String BUNDLE_NAME = "org.polarsys.capella.vp.ms.ui.preferences.messages"; //$NON-NLS-1$
  public static String InitializeConfigurationAccessDialog_label;
  public static String InitializeConfigurationAccessDialog_message;
  public static String InitializeConfigurationAccessDialog_title;

  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages() {
  }
  
}
