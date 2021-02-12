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
package org.polarsys.capella.vp.ms.expression.parser;

import java.util.function.Function;

import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.core.data.capellacommon.AbstractState;
import org.polarsys.capella.vp.ms.AndOperation;
import org.polarsys.capella.vp.ms.BooleanExpression;
import org.polarsys.capella.vp.ms.InSituationExpression;
import org.polarsys.capella.vp.ms.InStateExpression;
import org.polarsys.capella.vp.ms.MsFactory;
import org.polarsys.capella.vp.ms.NotOperation;
import org.polarsys.capella.vp.ms.OrOperation;
import org.polarsys.capella.vp.ms.Situation;
import org.polarsys.capella.vp.ms.expression.parser.MsExpressionParser.AndExprContext;
import org.polarsys.capella.vp.ms.expression.parser.MsExpressionParser.HrefContext;
import org.polarsys.capella.vp.ms.expression.parser.MsExpressionParser.NotExprContext;
import org.polarsys.capella.vp.ms.expression.parser.MsExpressionParser.OrExprContext;

public class DefaultMsExpressionVisitor extends MsExpressionBaseVisitor<BooleanExpression> {

  private final Function<String, EObject> resolver;

  public DefaultMsExpressionVisitor(Function<String, EObject> resolver) {
    this.resolver = resolver;
  }

  @Override
  public BooleanExpression visitNotExpr(NotExprContext ctx) {
    BooleanExpression result = null;
    if (ctx.orExpr() != null) {
      result = visitOrExpr(ctx.orExpr());
    } else if (ctx.href() != null) {
      result = visitHref(ctx.href());
    }
    if (ctx.NEG() != null) {
      BooleanExpression child = result;
      result = MsFactory.eINSTANCE.createNotOperation();
      ((NotOperation) result).getChildren().add(child);
    }
    return result;
  }

  @Override
  public BooleanExpression visitOrExpr(OrExprContext ctx) {
    if (ctx.andExpr().size() > 1) {
      OrOperation result = MsFactory.eINSTANCE.createOrOperation();
      for (AndExprContext and : ctx.andExpr()) {
        result.getChildren().add(visit(and));
      }
      return result;
    } else {
      return visit(ctx.andExpr(0));
    }
  }

  @Override
  public BooleanExpression visitHref(HrefContext ctx) {
    EObject target = resolver.apply(ctx.ID().getText());
    BooleanExpression result = null;
    if (target instanceof AbstractState) {
      result = MsFactory.eINSTANCE.createInStateExpression();
      ((InStateExpression) result).setState((AbstractState) target);
    } else if (target instanceof Situation) {
      result = MsFactory.eINSTANCE.createInSituationExpression();
      ((InSituationExpression) result).setSituation((Situation) target);
    }
    return result;
  }

  @Override
  public BooleanExpression visitAndExpr(AndExprContext ctx) {
    if (ctx.notExpr().size() > 1) {
      AndOperation result = MsFactory.eINSTANCE.createAndOperation();
      for (NotExprContext not : ctx.notExpr()) {
        result.getChildren().add(visit(not));
      }
      return result;
    } else {
      return visit(ctx.notExpr(0));
    }
  }

}
