package org.polarsys.capella.vp.ms.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.polarsys.capella.common.linkedtext.ui.LinkedTextDocument;
import org.polarsys.capella.common.linkedtext.ui.LinkedTextHyperlink;
import org.polarsys.capella.core.data.capellacommon.AbstractState;
import org.polarsys.capella.vp.ms.AndOperation;
import org.polarsys.capella.vp.ms.BooleanExpression;
import org.polarsys.capella.vp.ms.InStateExpression;
import org.polarsys.capella.vp.ms.MsFactory;
import org.polarsys.capella.vp.ms.NotOperation;
import org.polarsys.capella.vp.ms.OrOperation;
import org.polarsys.capella.vp.ms.util.LinkedText2Situation.Token;

/**
 * 
 * Expression:
 *    OrExpression
 *    '(' Expression ')'
 *    
 *    
 * OrExpression:
 *    AndExpression ('OR' AndExpression)*   
 * 
 * AndExpression:
 *    NotExpression ('AND' NotExpression)*
 *    
 * NotExpression:
 *    'NOT'? (HYPERLINK | '(' Expression ')')
 * 
 */
public class SituationExpressionParser {

  @SuppressWarnings("serial")
  static class ExpressionError extends Exception {
    public ExpressionError(String error) {
      super(error);
    }
  }
  
  private final SituationExpressionScanner scanner;
  private final LinkedTextDocument document;

 
  public SituationExpressionParser(LinkedTextDocument document) {
    this.document = document;
    this.scanner = new SituationExpressionScanner(document);
  }
 
  public BooleanExpression parse() throws ExpressionError {
    
    // special case, if the stream is empty, it's an empty expression and we return null
    if (peek() == Token.EOS) {
      return null;
    }

    BooleanExpression result = orExpr();
    Token next = next();
    if (next != Token.EOS) {
      try {
        throw new ExpressionError("Expected end of expression but found: " + next + ": ^" + document.get(scanner.pos, Math.min(10, document.getLength() - scanner.pos)));
      } catch (BadLocationException e) {
        /**/
      }
    }
    return result;
  }

  private BooleanExpression orExpr() throws ExpressionError {
    BooleanExpression result = null;
    List<BooleanExpression> children = new ArrayList<BooleanExpression>();
    children.add(andExpr());
    while (scanner.peek() == Token.OR) {
      scanner.nextToken();
      children.add(andExpr());
    }
    if (children.size() > 1) {
      OrOperation orOp = MsFactory.eINSTANCE.createOrOperation();
      orOp.getChildren().addAll(children);
      result = orOp;
    } else {
      result = children.get(0);
    }
    return result;
  }
  
  private BooleanExpression andExpr() throws ExpressionError {
    BooleanExpression result = null;
    List<BooleanExpression> children = new ArrayList<BooleanExpression>();
    children.add(notExpr());
    while (scanner.peek() == Token.AND) {
      scanner.nextToken();
      children.add(notExpr());
    }
    if (children.size() > 1) {
      AndOperation andOp = MsFactory.eINSTANCE.createAndOperation();
      andOp.getChildren().addAll(children);
      result = andOp;
    } else {
      result = children.get(0);
    }
    return result;
  }

  // notExpr:
  //    NOT? (Hyperlink | '(' expr ')')
  private BooleanExpression notExpr() throws ExpressionError {

    BooleanExpression result = null;
    boolean negate = false;

    Token next = next();
    if (next == Token.NOT) {
      negate = true;
      next = next();
    }

    if (next == Token.HYPERLINK) {
      LinkedTextHyperlink link = scanner.nextHyperlink();
      result = MsFactory.eINSTANCE.createInStateExpression();
      ((InStateExpression) result).setState((AbstractState) link.getTarget());
    } else if (next == Token.PAREN_O) {  
      result = orExpr();
      next = next();
      if (next != Token.PAREN_C) {
        throw new ExpressionError("Expected ) but found: " + next);
      }
    } else {
      throw new ExpressionError("Expected " + (!negate ? "NOT, " : "") + "Hyperlink or ( but found: " + next);
    }

    if (negate) {
      NotOperation not = MsFactory.eINSTANCE.createNotOperation();
      not.getChildren().add(result);
      result = not;
    }
    return result;
  }

  private Token peek() throws ExpressionError {
    Token result = scanner.peek();
    if (result == Token.ERROR) {
      throw new ExpressionError(scanner.getError());
    }
    return result;
  }

  private Token next() throws ExpressionError {
    Token result = scanner.nextToken();
    if (result == Token.ERROR) {
      throw new ExpressionError(scanner.getError());
    }
    return result;
  }
  
}
