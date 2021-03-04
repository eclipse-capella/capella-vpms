/*******************************************************************************
 * Copyright (c) 2021 THALES GLOBAL SERVICES.
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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.polarsys.capella.vp.ms.access_Type;

public class Initializer extends AbstractPreferenceInitializer {
  @Override
  public void initializeDefaultPreferences() {
    DefaultScope.INSTANCE.getNode(Activator.PLUGIN_ID).put(MsPreferenceConstants.PREF_DEFAULT_CONFIGURATION_ACCESS,
        access_Type.FULL.getLiteral());
  }
}