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
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

public class CreateConfigurationElementsTableTest extends VpmsSwtBotTest {

  private static final String SESSION_FILE = "MsDiagramTest1.aird";

  private static final String CAPELLA_PROJECT_NAME = "MsDiagramTest1";
  
  private static final String NEW_DIAGRAM_LABEL = "New Diagram / Table...";
  private static final String CONFIGURATION_ELEMENTS_TABLE_LABEL = "Configuration Elements";

  private void createConfigurationElementsTable(String... treepath) {
    String[] fullPath = new String[treepath.length + 3];
    fullPath[0] = TEMP_PROJECT_NAME;
    fullPath[1] = SESSION_FILE;
    fullPath[2] = CAPELLA_PROJECT_NAME;
    System.arraycopy(treepath, 0, fullPath, 3, treepath.length);
    SWTBotTreeItem item = bot.tree().expandNode(fullPath);
    item.contextMenu(NEW_DIAGRAM_LABEL).menu(CONFIGURATION_ELEMENTS_TABLE_LABEL).click();
    bot.button("OK").click();
  }
  
  public void testCreateConfigurationElementsEntityPkg() {
    createConfigurationElementsTable("Operational Analysis", "Operational Entities");
  }
  
  public void testCreateConfigurationElementsEntity() {
    createConfigurationElementsTable("Operational Analysis", "Operational Entities", "Component");
  }
  
  public void testCreateConfigurationElementsEntityActor() {
    createConfigurationElementsTable("Operational Analysis", "Operational Entities", "Actor");
  }
  
  public void testCreateConfigurationElementsSystemPkg() {
    createConfigurationElementsTable("System Analysis", "Structure");
  }
  
  public void testCreateConfigurationElementsSystemComponent() {
    createConfigurationElementsTable("System Analysis", "Structure", "Component");
  }
  
  public void testCreateConfigurationElementsSystemActor() {
    createConfigurationElementsTable("System Analysis", "Structure", "Actor");
  }
  
  public void testCreateConfigurationElementsLogicalPkg() {
    createConfigurationElementsTable("Logical Architecture", "Structure");
  }
  
  public void testCreateConfigurationElementsLogicalComponent() {
    createConfigurationElementsTable("Logical Architecture", "Structure", "Logical System");
  }
  
  public void testCreateConfigurationElementsLogicalActor() {
    createConfigurationElementsTable("Logical Architecture", "Structure", "Actor");
  }

  public void testCreateConfigurationElementsPhysicalActor() {
    createConfigurationElementsTable("Physical Architecture", "Structure", "Actor");
  }
  
  public void testCreateConfigurationElementsPhysicalPkg() {
    createConfigurationElementsTable("Physical Architecture", "Structure");
  }
  
  public void testCreateConfigurationElementsPhysicalComponent() {
    createConfigurationElementsTable("Physical Architecture", "Structure", "Physical System");
  }

}
