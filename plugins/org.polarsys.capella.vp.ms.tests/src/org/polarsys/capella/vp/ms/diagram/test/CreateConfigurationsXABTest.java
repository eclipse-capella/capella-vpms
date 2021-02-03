package org.polarsys.capella.vp.ms.diagram.test;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.ui.edit.api.part.AbstractDiagramContainerEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNode4EditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeContainerEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeEditPart;
import org.eclipse.sirius.tests.swtbot.support.api.AbstractSiriusSwtBotGefTestCase;
import org.eclipse.sirius.tests.swtbot.support.api.business.UIDiagramRepresentation.ZoomLevel;
import org.eclipse.sirius.tests.swtbot.support.api.business.UIResource;
import org.eclipse.sirius.tests.swtbot.support.api.editor.SWTBotSiriusDiagramEditor;
import org.eclipse.sirius.tests.swtbot.support.api.view.DesignerViews;
import org.eclipse.sirius.tests.swtbot.support.utils.SWTBotUtils;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.polarsys.capella.core.data.cs.Part;
import org.polarsys.capella.vp.ms.CSConfiguration;

public class CreateConfigurationsXABTest extends AbstractSiriusSwtBotGefTestCase {

  private static final String DATA_UNIT_DIR = "model/MSDiagramTest1/";
  private static final String MODEL = "MsDiagramTest1.melodymodeller";
  private static final String SESSION_FILE = "MsDiagramTest1.aird";
  private static final String AFM_FILE = "MsDiagramTest1.afm";
  private static final String FILE_DIR = "/";

  private static final String NODE_CREATION_TOOL_NAME = "Configuration";
  
  @Override
  protected void onSetUpBeforeClosingWelcomePage() throws Exception {
      copyFileToTestProject("org.polarsys.capella.vp.ms.tests", DATA_UNIT_DIR, MODEL, SESSION_FILE, AFM_FILE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void onSetUpAfterOpeningDesignerPerspective() throws Exception {
      sessionAirdResource = new UIResource(designerProject, FILE_DIR, SESSION_FILE);
      localSession = designerPerspective.openSessionFromFile(sessionAirdResource, true);
      closeOutline();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void tearDown() throws Exception {
      // Restore the default zoom level
      editor.click(1, 1); // Set the focus on the diagram
      editor.zoom(ZoomLevel.ZOOM_100);
      // Go to the origin to avoid scroll bar
      editor.scrollTo(0, 0);
      SWTBotUtils.waitAllUiEvents();
      // Reopen outline
      new DesignerViews(bot).openOutlineView();
      SWTBotUtils.waitAllUiEvents();
      super.tearDown();
  }

  private void createConfigurations(String diagramDescriptionName, String diagramName) {
    editor = (SWTBotSiriusDiagramEditor) openRepresentation(localSession.getOpenedSession(), diagramDescriptionName, diagramName, DDiagram.class);
    SWTBotUtils.waitAllUiEvents();
    editor.changeLayerActivation("Configurations");
    SWTBotUtils.waitAllUiEvents();
    bot.sleep(1000);
    editor.activateTool(NODE_CREATION_TOOL_NAME);
   
    SWTBotUtils.waitAllUiEvents();
    CSConfigurationWidget c = new CSConfigurationWidget(editor.getDiagramEditPart());
    editor.click(10, 10);
    bot.waitUntilWidgetAppears(c);
    SWTBotUtils.waitAllUiEvents();
    checkConfig(c.getCreatedWidget(), DNodeEditPart.class);
    
    final SWTBotGefEditPart actor = editor.getEditPart("Actor", DNodeContainerEditPart.class);
    editor.activateTool(NODE_CREATION_TOOL_NAME);
    c = new CSConfigurationWidget(actor.part());
    editor.clickCentered(actor);
    bot.waitUntilWidgetAppears(c);
    checkConfig(c.getCreatedWidget(), DNode4EditPart.class);

    final SWTBotGefEditPart component = editor.getEditPart("Component", AbstractDiagramContainerEditPart.class);
    editor.activateTool(NODE_CREATION_TOOL_NAME);
    c = new CSConfigurationWidget(component.part());
    editor.clickCentered(component);
    bot.waitUntilWidgetAppears(c);
    checkConfig(c.getCreatedWidget(), DNode4EditPart.class);

    final SWTBotGefEditPart nestedComponent = editor.getEditPart("NestedComponent", AbstractDiagramContainerEditPart.class);
    editor.activateTool(NODE_CREATION_TOOL_NAME);
    c = new CSConfigurationWidget(nestedComponent.part());
    editor.clickCentered(nestedComponent);
    bot.waitUntilWidgetAppears(c);
    checkConfig(c.getCreatedWidget(), DNode4EditPart.class);
    
  }
  
  private void checkConfig(EditPart createdWidget, Class<? extends EditPart> class1) {
    
    assertTrue(class1.isInstance(createdWidget)); // it's a bordered node
    View view = (View) createdWidget.getModel();
    DSemanticDecorator decorator = (DSemanticDecorator) view.getElement();
    EObject target = decorator.getTarget();
    assertTrue(target instanceof CSConfiguration);

    View parentView = (View) createdWidget.getParent().getModel();
    DSemanticDecorator parentDecorator = (DSemanticDecorator) parentView.getElement();
    EObject parentTarget = parentDecorator.getTarget();

    // diagrams show parts, configurations are added to components 
    if (parentTarget instanceof Part) {
      parentTarget = ((Part) parentTarget).getAbstractType();
    }

    assertSame(parentTarget, target.eContainer());

  }

  /**
   * Ensures that a node created in a Diagram has the expected location.
   * 
   * @param zoomLevel
   *          the zoomLevel to set on the editor
   */
  public void testCreateConfigurationsOABDiagram() {
    createConfigurations("Operational Entity Blank", "oab1");
  }

  public void testCreateConfigurationsOABDiagram2() {
    createConfigurations("Operational Entity Blank", "oab2");
  }
  
  public void testCreateConfigurationsSABDiagram1() {
    createConfigurations("System Architecture Blank", "sab1");
  }
  
  public void testCreateConfigurationsSABDiagram2() {
    createConfigurations("System Architecture Blank", "sab2");
  }
  
  public void testCreateConfigurationsLABDiagram1() {
    createConfigurations("Logical Architecture Blank", "lab1");
  }
  
  public void testCreateConfigurationsLABDiagram2() {
    createConfigurations("Logical Architecture Blank", "lab2");
  }
  
  
  public void testCreateConfigurationsPABDiagram1() {
    createConfigurations("Physical Architecture Blank", "pab1");
  }
  
  public void testCreateConfigurationsPABDiagram2() {
    createConfigurations("Physical Architecture Blank", "pab2");
  }
  
  
}
