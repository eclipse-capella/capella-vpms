package org.polarsys.capella.vp.ms.diagram.test;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;

public class CSConfigurationWidget extends DefaultCondition implements EditPartListener {

  private final EditPart parentEditPart;

  private volatile EditPart partCreated;
  private volatile Exception e;

  public CSConfigurationWidget(EditPart parent) {
    this.parentEditPart = parent;
    parent.addEditPartListener(this);
  }

  public EditPart getCreatedWidget() {
    return partCreated;
  }

  @Override
  public boolean test() throws Exception {
    return partCreated != null;
  }

  @Override
  public String getFailureMessage() {
    return "Configuration Bordered Node didn't show up";
  }

  @Override
  public void childAdded(EditPart child, int index) {
    partCreated = child;
    parentEditPart.removeEditPartListener(this);
  }

  @Override
  public void partActivated(EditPart editpart) {
  }

  @Override
  public void partDeactivated(EditPart editpart) {
  }

  @Override
  public void removingChild(EditPart child, int index) {
  }

  @Override
  public void selectedStateChanged(EditPart editpart) {
  }
}
