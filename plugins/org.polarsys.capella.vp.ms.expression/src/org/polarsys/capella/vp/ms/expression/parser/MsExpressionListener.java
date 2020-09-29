// Generated from MsExpression.g4 by ANTLR 4.3
package org.polarsys.capella.vp.ms.expression.parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MsExpressionParser}.
 */
public interface MsExpressionListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MsExpressionParser#notExpr}.
	 * @param ctx the parse tree
	 */
	void enterNotExpr(@NotNull MsExpressionParser.NotExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MsExpressionParser#notExpr}.
	 * @param ctx the parse tree
	 */
	void exitNotExpr(@NotNull MsExpressionParser.NotExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link MsExpressionParser#orExpr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(@NotNull MsExpressionParser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MsExpressionParser#orExpr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(@NotNull MsExpressionParser.OrExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link MsExpressionParser#href}.
	 * @param ctx the parse tree
	 */
	void enterHref(@NotNull MsExpressionParser.HrefContext ctx);
	/**
	 * Exit a parse tree produced by {@link MsExpressionParser#href}.
	 * @param ctx the parse tree
	 */
	void exitHref(@NotNull MsExpressionParser.HrefContext ctx);

	/**
	 * Enter a parse tree produced by {@link MsExpressionParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(@NotNull MsExpressionParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MsExpressionParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(@NotNull MsExpressionParser.AndExprContext ctx);
}