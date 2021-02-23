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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.AbstractDNode;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.polarsys.capella.core.data.cs.PhysicalLink;
import org.polarsys.capella.core.data.fa.ComponentExchange;
import org.polarsys.capella.core.data.fa.FunctionalExchange;
import org.polarsys.capella.vp.ms.CSConfiguration;
import org.polarsys.capella.vp.ms.ui.css.CSSAdapter;

public class DefaultMsRefreshExtension extends AbstractMsRefreshExtension {

  protected ScopeFactory createScopeFactory() {
    return new ScopeFactory();
  }

  protected ScopeVisitor<?> createVisitor(){
    return new ConfigurationScopeVisitor() {

      Map<DEdge, Scope> rememberedEdgeScopes = new HashMap<DEdge, Scope>();
    
      @Override
    public Collection<CSConfiguration> visitAbstracDNodeScope(AbstractDNodeScope asd) {
      Collection<CSConfiguration> result = super.visitAbstracDNodeScope(asd);
      AbstractDNode node = (AbstractDNode) asd.getElement();
      if (node instanceof EdgeTarget) {
        for (DEdge edge : (((EdgeTarget) node).getOutgoingEdges())) {
          updateEdgeStyle(asd, edge, true);
        }
        for (DEdge edge : (((EdgeTarget) node).getIncomingEdges())) {
          updateEdgeStyle(asd, edge, false);
        }
      }
      return result;
    }
    
    // Have we already handled the opposite scope, then return it,
    // otherwise this scope becomes the opposite scope and the edge 
    // is handled later from the other end
    private Scope getOppositeScope(DEdge edge, Scope thisScope) {
      Scope otherEnd = rememberedEdgeScopes.remove(edge);
      if (otherEnd == null) {
        rememberedEdgeScopes.put(edge, thisScope);
      }
      return otherEnd;
    }

    private void updateEdgeStyle(AbstractDNodeScope thisScope, DEdge edge, boolean outgoing) {
      Scope opposite = getOppositeScope(edge, thisScope);
      if (opposite != null) {
        Scope in = outgoing ? opposite : thisScope;
        Scope out = outgoing ? thisScope : opposite;
        if (edge.getTarget() instanceof FunctionalExchange) {
          updateFunctionalExchangeStyle(edge, (FunctionalExchange) edge.getTarget(), in, out);
        } else if (edge.getTarget() instanceof ComponentExchange) {
          updateComponentExchangeStyle(edge, (ComponentExchange) edge.getTarget(), in, out);
        } else if (edge.getTarget() instanceof PhysicalLink) {
          updatePhysicalLinkStyle(edge, (PhysicalLink) edge.getTarget(), in, out);
        }
      }
    }

    private void updateFunctionalExchangeStyle(DEdge edge, FunctionalExchange fe, Scope fipScope, Scope fopScope) {
      updateExchangeStyle(edge, fipScope, fe.getTargetFunctionInputPort(), fopScope, fe.getSourceFunctionOutputPort());
    }

    private void updateComponentExchangeStyle(DEdge edge, ComponentExchange ce, Scope inScope, Scope outScope) {
      updateExchangeStyle(edge, inScope, ce.getTargetPort(), outScope, ce.getSourcePort());
    }

    private void updatePhysicalLinkStyle(DEdge edge, PhysicalLink pe, Scope inScope, Scope outScope) {
      updateExchangeStyle(edge, inScope, pe.getTargetPhysicalPort(), outScope, pe.getSourcePhysicalPort());
    }

    private void updateExchangeStyle(DEdge edge, Scope inScope, EObject inScopeTarget, Scope outScope, EObject outScopeTarget) {
      CSSAdapter style = CSSAdapter.getAdapter(edge).clear();
      Collection<CSConfiguration> applied = new ArrayList<>(getAllScopeConfigurations(outScope));
      applied.retainAll(getAllScopeConfigurations(inScope));
      updateStyle(style, edge.getTarget(), applied);
    }

    };

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
        Scope childScope = scopeFactory.doSwitch(child);
        if (!(child instanceof DEdge)) {
          remaining.push(childScope);
        }
      }
    }
  }

  @Override
  public void postRefresh(DDiagram dDiagram) {
    refreshNodes(dDiagram);
  }

}
