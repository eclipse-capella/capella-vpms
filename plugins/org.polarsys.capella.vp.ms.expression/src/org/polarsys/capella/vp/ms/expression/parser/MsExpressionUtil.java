package org.polarsys.capella.vp.ms.expression.parser;

import java.util.function.Function;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.vp.ms.BooleanExpression;

public class MsExpressionUtil {

  public static BooleanExpression parse(String expression, Function<String, EObject> resolver, ANTLRErrorListener errorListener) throws RecognitionException {
    ANTLRInputStream input = new ANTLRInputStream(expression);
    MsExpressionLexer lexer = new MsExpressionLexer(input);        
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    MsExpressionParser parser = new MsExpressionParser(tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(errorListener);
    parser.setErrorHandler(new BailErrorStrategy());
    try {
      ParseTree tree = parser.orExpr();
      return new DefaultMsExpressionVisitor(resolver).visit(tree);
    } catch (ParseCancellationException e) {
      return null;
    }
  }

}
