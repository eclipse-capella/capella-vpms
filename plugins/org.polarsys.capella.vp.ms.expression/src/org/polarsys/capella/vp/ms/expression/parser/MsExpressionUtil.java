package org.polarsys.capella.vp.ms.expression.parser;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTree;

public class MsExpressionUtil {

  public static ParseTree parse(String expression, ANTLRErrorListener errorListener) throws RecognitionException {
    ANTLRInputStream input = new ANTLRInputStream(expression);
    MsExpressionLexer lexer = new MsExpressionLexer(input);        
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    MsExpressionParser parser = new MsExpressionParser(tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(errorListener);
    return parser.orExpr();
  }

}
