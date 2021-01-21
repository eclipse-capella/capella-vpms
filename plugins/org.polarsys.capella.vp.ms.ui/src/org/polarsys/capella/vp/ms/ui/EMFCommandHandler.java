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

  protected boolean acceptSelectionElement(Object element) {
    return element instanceof EObject;
  }

  @Override
  public void setEnabled(Object evaluationContext) {
    Collection<?> sel = (Collection<?>) ((IEvaluationContext) evaluationContext).getDefaultVariable();
    for (Object o : sel) {

      if (!acceptSelectionElement(o)) {
        setBaseEnabled(false);
        return;
      }

      if (o instanceof EObject) {
        EditingDomain csDomain = TransactionHelper.getEditingDomain((EObject)o);
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
    }
    command = createCommand(sel);
    setBaseEnabled(command.canExecute());
  }

  protected abstract Command createCommand(Collection<?> selection);

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
