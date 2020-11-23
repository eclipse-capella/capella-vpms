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

import java.util.Collection;
import java.util.Collections;

import org.eclipse.sirius.diagram.DDiagramElement;

class DefaultScope extends Scope {

  public DefaultScope(Scope parent, Object element) {
    super(parent, element);
  }

  @Override
  Collection<? extends DDiagramElement> getChildren() {
    return Collections.emptyList();
  }

  @Override
  public <T> T accept(ScopeVisitor<T> visitor) {
    return visitor.visitDefaultScope(this);
  }

}