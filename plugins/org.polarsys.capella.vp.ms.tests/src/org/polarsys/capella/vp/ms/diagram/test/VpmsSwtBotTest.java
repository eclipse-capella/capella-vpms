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
package org.polarsys.capella.vp.ms.diagram.test;
import org.eclipse.sirius.tests.swtbot.support.api.AbstractSiriusSwtBotGefTestCase;
import org.eclipse.sirius.tests.swtbot.support.api.business.UIDiagramRepresentation.ZoomLevel;
import org.eclipse.sirius.tests.swtbot.support.api.business.UIResource;
import org.eclipse.sirius.tests.swtbot.support.api.view.DesignerViews;
import org.eclipse.sirius.tests.swtbot.support.utils.SWTBotUtils;

public abstract class VpmsSwtBotTest extends AbstractSiriusSwtBotGefTestCase {

  private static final String DATA_UNIT_DIR = "model/MsDiagramTest1/";
  private static final String MODEL = "MsDiagramTest1.capella";
  private static final String SESSION_FILE = "MsDiagramTest1.aird";
  private static final String AFM_FILE = "MsDiagramTest1.afm";
  private static final String FILE_DIR = "/";

  
  @Override
  protected void onSetUpBeforeClosingWelcomePage() throws Exception {
      copyFileToTestProject("org.polarsys.capella.vp.ms.tests", DATA_UNIT_DIR, MODEL, SESSION_FILE, AFM_FILE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void onSetUpAfterOpeningDesignerPerspective() throws Exception {
      designerPerspectives.openPerspective("Capella");
      sessionAirdResource = new UIResource(designerProject, FILE_DIR, SESSION_FILE);
      localSession = designerPerspective.openSessionFromFile(sessionAirdResource, true);
      closeOutline();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void tearDown() throws Exception {
    if (editor != null) {
    // Restore the default zoom level
      editor.click(1, 1); // Set the focus on the diagram
      editor.zoom(ZoomLevel.ZOOM_100);
      // Go to the origin to avoid scroll bar
      editor.scrollTo(0, 0);
    }
      SWTBotUtils.waitAllUiEvents();
      // Reopen outline
      new DesignerViews(bot).openOutlineView();
      SWTBotUtils.waitAllUiEvents();
      super.tearDown();
  }

}
