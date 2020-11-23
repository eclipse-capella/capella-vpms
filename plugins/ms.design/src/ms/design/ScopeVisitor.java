/*******************************************************************************
 * Copyright (c) 2018 THALES GLOBAL SERVICES.
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

public interface ScopeVisitor<T> {
  default T visitDiagramScope(DiagramScope ds) { return null; }
  default T visitAbstracDNodeScope(AbstractDNodeScope asd) { return null; }
  default T visitDNodeContainerScope(DNodeContainerScope dnc) { return null; }
  default T visitDNodeScope(DNodeScope dns) { return null; }
  default T visitDefaultScope(DefaultScope ds) { return null; }
}
