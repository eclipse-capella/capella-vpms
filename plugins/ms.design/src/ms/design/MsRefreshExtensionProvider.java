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
import org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtension;
import org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtensionProvider;
import org.polarsys.capella.core.diagram.helpers.naming.DiagramDescriptionConstants;

import ms.configuration.services.cs.CsConfigurationServices;
import ms.configuration.services.cs.DiagramConstants;

public class MsRefreshExtensionProvider implements IRefreshExtensionProvider {

  private AbstractMsRefreshExtension scenarioRefresh = new ScenarioMsRefreshExtension();
  private AbstractMsRefreshExtension interfaceRefresh = new InterfaceDiagramMsRefreshExtension();
  private AbstractMsRefreshExtension defaultRefresh = new DefaultMsRefreshExtension();

  @Override
  public boolean provides(DDiagram diagram) {
    return CsConfigurationServices.isConfigurationsLayerActive(diagram);
  }

  @Override
  public IRefreshExtension getRefreshExtension(DDiagram diagram) {
    IRefreshExtension refresh = defaultRefresh;
    if (DiagramDescriptionConstants.INTERFACE_SCENARIO.equals(diagram.getDescription().getName())
        || DiagramConstants.EXCHANGE_SCENARIO.equals(diagram.getDescription().getName())) {
      refresh = scenarioRefresh;
    } else if (DiagramConstants.CDI_NAME.equals(diagram.getDescription().getName())) { 
      refresh = interfaceRefresh;
    }
    return refresh;
  }

}
