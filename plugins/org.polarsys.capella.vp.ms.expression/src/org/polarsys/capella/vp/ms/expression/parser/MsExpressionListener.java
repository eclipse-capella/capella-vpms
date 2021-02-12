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
// Generated from grammar/MsExpression.g4 by ANTLR 4.7.2
package org.polarsys.capella.vp.ms.expression.parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by {@link MsExpressionParser}.
 */
public interface MsExpressionListener extends ParseTreeListener {
  /**
   * Enter a parse tree produced by {@link MsExpressionParser#orExpr}.
   * 
   * @param ctx
   *          the parse tree
   */
  void enterOrExpr(MsExpressionParser.OrExprContext ctx);

  /**
   * Exit a parse tree produced by {@link MsExpressionParser#orExpr}.
   * 
   * @param ctx
   *          the parse tree
   */
  void exitOrExpr(MsExpressionParser.OrExprContext ctx);

  /**
   * Enter a parse tree produced by {@link MsExpressionParser#andExpr}.
   * 
   * @param ctx
   *          the parse tree
   */
  void enterAndExpr(MsExpressionParser.AndExprContext ctx);

  /**
   * Exit a parse tree produced by {@link MsExpressionParser#andExpr}.
   * 
   * @param ctx
   *          the parse tree
   */
  void exitAndExpr(MsExpressionParser.AndExprContext ctx);

  /**
   * Enter a parse tree produced by {@link MsExpressionParser#notExpr}.
   * 
   * @param ctx
   *          the parse tree
   */
  void enterNotExpr(MsExpressionParser.NotExprContext ctx);

  /**
   * Exit a parse tree produced by {@link MsExpressionParser#notExpr}.
   * 
   * @param ctx
   *          the parse tree
   */
  void exitNotExpr(MsExpressionParser.NotExprContext ctx);

  /**
   * Enter a parse tree produced by {@link MsExpressionParser#href}.
   * 
   * @param ctx
   *          the parse tree
   */
  void enterHref(MsExpressionParser.HrefContext ctx);

  /**
   * Exit a parse tree produced by {@link MsExpressionParser#href}.
   * 
   * @param ctx
   *          the parse tree
   */
  void exitHref(MsExpressionParser.HrefContext ctx);
}