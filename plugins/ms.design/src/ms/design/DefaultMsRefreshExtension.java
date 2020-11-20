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

import java.util.ArrayDeque;
import java.util.Deque;

import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DEdge;
import org.polarsys.capella.vp.ms.ui.css.CSSAdapter;

public class DefaultMsRefreshExtension extends AbstractMsRefreshExtension {

  protected ScopeFactory createScopeFactory() {
    return new ScopeFactory();
  }

  protected ScopeVisitor<?> createVisitor(){
    return new ConfigurationScopeVisitor();
  }

  protected void refreshNodes(DDiagram dDiagram) {
    ScopeFactory scopeFactory = createScopeFactory();
    ScopeVisitor<?> visitor = createVisitor();

    Deque<Scope> remaining = new ArrayDeque<Scope>();
    remaining.push(scopeFactory.doSwitch(dDiagram));

    while (!remaining.isEmpty()) {
      Scope scope = remaining.pop();
      scope.accept(visitor);
      scopeFactory.setParent(scope);
      for (DDiagramElement child : scope.getChildren()) {
        if (!(child instanceof DEdge)) {
          remaining.push(scopeFactory.doSwitch(child));
        }
      }
    }
  }

  protected void refreshEdges(DDiagram dDiagram) {
    for (DEdge edge : dDiagram.getEdges()) {
      CSSAdapter style = CSSAdapter.getAdapter(edge).clear();

      CSSAdapter targetStyle = CSSAdapter.getAdapter(edge.getTargetNode());
      style.addCSSClass(targetStyle);

      CSSAdapter sourceStyle = CSSAdapter.getAdapter(edge.getSourceNode());
      style.addCSSClass(sourceStyle);

    }
  }

  @Override
  public void postRefresh(DDiagram dDiagram) {
    refreshNodes(dDiagram);
    refreshEdges(dDiagram);
  }

}
