package org.polarsys.capella.vp.ms.ui;
import static org.polarsys.capella.vp.ms.util.LinkedText2Situation.Token;

import java.util.Iterator;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.Position;
import org.polarsys.capella.common.linkedtext.ui.LinkedTextCategories;
import org.polarsys.capella.common.linkedtext.ui.LinkedTextDocument;
import org.polarsys.capella.common.linkedtext.ui.LinkedTextHyperlink;

public class SituationExpressionScanner {

  private final LinkedTextDocument document;
  
  private final Position[] hyperlinks;
  int hyperlinkIndex;
  Iterator<LinkedTextHyperlink> aaa;
  
  private String error;
  int pos;

  public SituationExpressionScanner(LinkedTextDocument document) {
    this.document = document;
    Position[] links = null;
    try {
      links = document.getPositions(LinkedTextCategories.HYPERLINK.name());
    } catch (BadPositionCategoryException e) {
      links = new Position[0];
    }
    this.hyperlinks = links;
    aaa = document.getHyperlinks().iterator();
  }
  
  public void reset() {
    pos = 0;
    error = null;
  }
  
  private Token findNextToken(boolean peek) {

    int oldPos = pos;
    Token next = null;
    
    do {
    
      if (pos == document.getLength()) {
        next = Token.EOS;
      }

      if (next == null) {
        if (hyperlinkIndex < hyperlinks.length) {
          Position hlPos = hyperlinks[hyperlinkIndex];
          if (pos == hlPos.getOffset()) {
            pos+= hlPos.getLength();
            hyperlinkIndex++;
            next = Token.HYPERLINK;
          }
        }
      }

      if (next == null) {
        for (Token t : Token.values()) {
          try {
            if (t.getLiteral() != null) {
              String test = document.get(pos, t.getLiteral().length());
              if (test.equals(t.getLiteral())) {
                pos+=t.getLiteral().length();
                next = t;
                break;
              }
            }
          } catch (BadLocationException e) {
            /* swallow this */
          }
        }
      }

      if (next == null) {
        try {
          if (Character.isWhitespace(document.getChar(pos))) {
            pos++;
          } else {
            next = Token.ERROR;
            StringBuilder builder = new StringBuilder(document.get());
            builder.insert(pos, "^^^");
            error = builder.toString();
          }
        } catch (BadLocationException e) {
          /* swallow this */
        }
      }
    } while (next == null);

    if (peek) {
      pos = oldPos;
      if (next == Token.HYPERLINK) {
        hyperlinkIndex--;
      }
    }
    return next;

  }

  /** 
   * Only call this one time for each HYPERLINK token received
   */
  public LinkedTextHyperlink nextHyperlink() {
    return aaa.next();
  }

  public String getError() {
    return error;
  }

  public Token nextToken() {
    return findNextToken(false);
  }

  public Token peek() {
    return findNextToken(true);
  }
  
}
