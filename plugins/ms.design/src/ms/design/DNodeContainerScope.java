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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DNodeContainer;

class DNodeContainerScope extends AbstractDNodeScope {

  public DNodeContainerScope(Scope parent, DNodeContainer element) {
    super(parent, element);
  }

  public <T> T accept(ScopeVisitor<T> visitor) {
    return visitor.visitDNodeContainerScope(this);
  }

  @Override
  protected Collection<? extends DDiagramElement> getChildren() {
    Collection<DDiagramElement> result = new ArrayList<DDiagramElement>();
    result.addAll(((DNodeContainer) getElement()).getOwnedDiagramElements());
    result.addAll(super.getChildren());
    return result;
  }
}