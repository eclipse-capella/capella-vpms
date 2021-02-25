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
package org.polarsys.capella.vp.ms.ui;

import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.polarsys.capella.common.helpers.TransactionHelper;

public abstract class EMFCommandHandler extends AbstractHandler {

  private Command command;
  private EditingDomain domain;

  protected void setBaseEnabled(boolean state) {
    super.setBaseEnabled(state);
    if (!state) {
      domain = null;
      command = null;
    }
  }

  protected boolean acceptSelectionElement(Object element, Object evaluationContext) {
    return element instanceof EObject;
  }

  @SuppressWarnings("unchecked")
  protected <T> T adapt(Object element, Class<T> clazz) {
    if (clazz.isInstance(element)) {
      return (T) element;
    } else if (element instanceof GraphicalEditPart) {
      View view = (View) ((GraphicalEditPart) element).getModel();
      element = view.getElement();
      if (element instanceof DSemanticDecorator) {
        EObject target = ((DSemanticDecorator) element).getTarget();
        if (clazz.isInstance(target)){
          return (T) target;
        }
      }
    }
    return null;
  }
  
  @Override
  public void setEnabled(Object evaluationContext) {
    Collection<?> sel = (Collection<?>) ((IEvaluationContext) evaluationContext).getDefaultVariable();
    for (Object o : sel) {

      if (!acceptSelectionElement(o, evaluationContext)) {
        setBaseEnabled(false);
        return;
      }

      EditingDomain csDomain = null;
      if (o instanceof EObject) {
        csDomain = TransactionHelper.getEditingDomain((EObject)o);
      } else if (o instanceof GraphicalEditPart) {
        View view = (View) ((GraphicalEditPart) o).getModel();
        csDomain = TransactionHelper.getEditingDomain(view.getElement());
      }
      if (csDomain == null) {
          setBaseEnabled(false);
          return;
        }
        if (domain == null) {
          domain = csDomain;
        }
        if (domain != csDomain) {
          setBaseEnabled(false);
          return;
        }
    }
    command = createCommand(sel, evaluationContext);
    setBaseEnabled(command.canExecute());
  }

  protected abstract Command createCommand(Collection<?> selection, Object evaluationContext);

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    try {
      domain.getCommandStack().execute(command);
    } finally {
      domain = null;
      command = null;
    }
    return null;
  }

}
