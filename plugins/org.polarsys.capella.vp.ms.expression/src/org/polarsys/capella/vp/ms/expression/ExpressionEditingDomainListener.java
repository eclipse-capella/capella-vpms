/*******************************************************************************
 * Copyright (c) 2020 THALES GLOBAL SERVICES.
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
package org.polarsys.capella.vp.ms.expression;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.RollbackException;
import org.polarsys.capella.common.ef.domain.AbstractEditingDomainResourceSetListenerImpl;
import org.polarsys.capella.vp.ms.BooleanOperation;
import org.polarsys.capella.vp.ms.MsPackage;

public class ExpressionEditingDomainListener extends AbstractEditingDomainResourceSetListenerImpl {

  @Override
  public boolean isPrecommitOnly() {
    return true;
  }

    public ExpressionEditingDomainListener() {
      super(NotificationFilter.createFeatureFilter(MsPackage.Literals.BOOLEAN_OPERATION__CHILDREN));
    }

    @Override
    public Command transactionAboutToCommit(ResourceSetChangeEvent event) throws RollbackException {
      final Collection<BooleanOperation> toRemove = new HashSet<BooleanOperation>();
      for (Notification notif : event.getNotifications()) {
        BooleanOperation op = (BooleanOperation) notif.getNotifier();
        if (op.getChildren().isEmpty() && op.eContainer() != null) {
          toRemove.add(op);
        }
      }
      if (toRemove.size() > 0) {
        return new RecordingCommand(event.getEditingDomain()) {
          @Override
          protected void doExecute() {
            // TODO this works, but it is slow.. better use SemanticEditingDomain.getCrossReferencer() to find crossrefs in constant time
            EcoreUtil.deleteAll(toRemove, true);
          }
        }; 
      }
      return null;
    }
}
