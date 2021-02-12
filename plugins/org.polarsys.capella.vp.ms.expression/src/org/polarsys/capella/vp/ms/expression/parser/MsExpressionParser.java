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

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class MsExpressionParser extends Parser {
  static {
    RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION);
  }

  protected static final DFA[] _decisionToDFA;
  protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
  public static final int T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9,
      T__9 = 10, NEG = 11, ID = 12, WS = 13;
  public static final int RULE_orExpr = 0, RULE_andExpr = 1, RULE_notExpr = 2, RULE_href = 3;

  private static String[] makeRuleNames() {
    return new String[] { "orExpr", "andExpr", "notExpr", "href" };
  }

  public static final String[] ruleNames = makeRuleNames();

  private static String[] makeLiteralNames() {
    return new String[] { null, "'OR'", "'AND'", "'('", "')'", "'.'", "'<a'", "'href'", "'='", "'\"'", "'/>'",
        "'NOT'" };
  }

  private static final String[] _LITERAL_NAMES = makeLiteralNames();

  private static String[] makeSymbolicNames() {
    return new String[] { null, null, null, null, null, null, null, null, null, null, null, "NEG", "ID", "WS" };
  }

  private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
  public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

  /**
   * @deprecated Use {@link #VOCABULARY} instead.
   */
  @Deprecated
  public static final String[] tokenNames;
  static {
    tokenNames = new String[_SYMBOLIC_NAMES.length];
    for (int i = 0; i < tokenNames.length; i++) {
      tokenNames[i] = VOCABULARY.getLiteralName(i);
      if (tokenNames[i] == null) {
        tokenNames[i] = VOCABULARY.getSymbolicName(i);
      }

      if (tokenNames[i] == null) {
        tokenNames[i] = "<INVALID>";
      }
    }
  }

  @Override
  @Deprecated
  public String[] getTokenNames() {
    return tokenNames;
  }

  @Override

  public Vocabulary getVocabulary() {
    return VOCABULARY;
  }

  @Override
  public String getGrammarFileName() {
    return "MsExpression.g4";
  }

  @Override
  public String[] getRuleNames() {
    return ruleNames;
  }

  @Override
  public String getSerializedATN() {
    return _serializedATN;
  }

  @Override
  public ATN getATN() {
    return _ATN;
  }

  boolean testMode;

  public MsExpressionParser(TokenStream input, boolean testMode) {
    this(input);
    this.testMode = testMode;
  }

  public MsExpressionParser(TokenStream input) {
    super(input);
    _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
  }

  public static class OrExprContext extends ParserRuleContext {
    public List<AndExprContext> andExpr() {
      return getRuleContexts(AndExprContext.class);
    }

    public AndExprContext andExpr(int i) {
      return getRuleContext(AndExprContext.class, i);
    }

    public OrExprContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_orExpr;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof MsExpressionListener)
        ((MsExpressionListener) listener).enterOrExpr(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof MsExpressionListener)
        ((MsExpressionListener) listener).exitOrExpr(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof MsExpressionVisitor)
        return ((MsExpressionVisitor<? extends T>) visitor).visitOrExpr(this);
      else
        return visitor.visitChildren(this);
    }
  }

  public final OrExprContext orExpr() throws RecognitionException {
    OrExprContext _localctx = new OrExprContext(_ctx, getState());
    enterRule(_localctx, 0, RULE_orExpr);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(8);
        andExpr();
        setState(13);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == T__0) {
          {
            {
              setState(9);
              match(T__0);
              setState(10);
              andExpr();
            }
          }
          setState(15);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class AndExprContext extends ParserRuleContext {
    public List<NotExprContext> notExpr() {
      return getRuleContexts(NotExprContext.class);
    }

    public NotExprContext notExpr(int i) {
      return getRuleContext(NotExprContext.class, i);
    }

    public AndExprContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_andExpr;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof MsExpressionListener)
        ((MsExpressionListener) listener).enterAndExpr(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof MsExpressionListener)
        ((MsExpressionListener) listener).exitAndExpr(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof MsExpressionVisitor)
        return ((MsExpressionVisitor<? extends T>) visitor).visitAndExpr(this);
      else
        return visitor.visitChildren(this);
    }
  }

  public final AndExprContext andExpr() throws RecognitionException {
    AndExprContext _localctx = new AndExprContext(_ctx, getState());
    enterRule(_localctx, 2, RULE_andExpr);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(16);
        notExpr();
        setState(21);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == T__1) {
          {
            {
              setState(17);
              match(T__1);
              setState(18);
              notExpr();
            }
          }
          setState(23);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class NotExprContext extends ParserRuleContext {
    public HrefContext href() {
      return getRuleContext(HrefContext.class, 0);
    }

    public OrExprContext orExpr() {
      return getRuleContext(OrExprContext.class, 0);
    }

    public List<TerminalNode> ID() {
      return getTokens(MsExpressionParser.ID);
    }

    public TerminalNode ID(int i) {
      return getToken(MsExpressionParser.ID, i);
    }

    public TerminalNode NEG() {
      return getToken(MsExpressionParser.NEG, 0);
    }

    public NotExprContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_notExpr;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof MsExpressionListener)
        ((MsExpressionListener) listener).enterNotExpr(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof MsExpressionListener)
        ((MsExpressionListener) listener).exitNotExpr(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof MsExpressionVisitor)
        return ((MsExpressionVisitor<? extends T>) visitor).visitNotExpr(this);
      else
        return visitor.visitChildren(this);
    }
  }

  public final NotExprContext notExpr() throws RecognitionException {
    NotExprContext _localctx = new NotExprContext(_ctx, getState());
    enterRule(_localctx, 4, RULE_notExpr);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(25);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 2, _ctx)) {
        case 1: {
          setState(24);
          match(NEG);
        }
          break;
        }
        setState(38);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 4, _ctx)) {
        case 1: {
          setState(27);
          href();
        }
          break;
        case 2: {
          setState(28);
          match(T__2);
          setState(29);
          orExpr();
          setState(30);
          match(T__3);
        }
          break;
        case 3: {
          setState(32);
          if (!(testMode))
            throw new FailedPredicateException(this, "testMode");
          setState(35);
          _errHandler.sync(this);
          switch (getInterpreter().adaptivePredict(_input, 3, _ctx)) {
          case 1: {
            setState(33);
            match(ID);
            setState(34);
            match(T__4);
          }
            break;
          }
          setState(37);
          match(ID);
        }
          break;
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class HrefContext extends ParserRuleContext {
    public TerminalNode ID() {
      return getToken(MsExpressionParser.ID, 0);
    }

    public HrefContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_href;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof MsExpressionListener)
        ((MsExpressionListener) listener).enterHref(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof MsExpressionListener)
        ((MsExpressionListener) listener).exitHref(this);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof MsExpressionVisitor)
        return ((MsExpressionVisitor<? extends T>) visitor).visitHref(this);
      else
        return visitor.visitChildren(this);
    }
  }

  public final HrefContext href() throws RecognitionException {
    HrefContext _localctx = new HrefContext(_ctx, getState());
    enterRule(_localctx, 6, RULE_href);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(40);
        match(T__5);
        setState(41);
        match(T__6);
        setState(42);
        match(T__7);
        setState(43);
        match(T__8);
        setState(44);
        match(ID);
        setState(45);
        match(T__8);
        setState(46);
        match(T__9);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
    switch (ruleIndex) {
    case 2:
      return notExpr_sempred((NotExprContext) _localctx, predIndex);
    }
    return true;
  }

  private boolean notExpr_sempred(NotExprContext _localctx, int predIndex) {
    switch (predIndex) {
    case 0:
      return testMode;
    }
    return true;
  }

  public static final String _serializedATN = "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\17\63\4\2\t\2\4\3"
      + "\t\3\4\4\t\4\4\5\t\5\3\2\3\2\3\2\7\2\16\n\2\f\2\16\2\21\13\2\3\3\3\3\3"
      + "\3\7\3\26\n\3\f\3\16\3\31\13\3\3\4\5\4\34\n\4\3\4\3\4\3\4\3\4\3\4\3\4"
      + "\3\4\3\4\5\4&\n\4\3\4\5\4)\n\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\2\2"
      + "\6\2\4\6\b\2\2\2\64\2\n\3\2\2\2\4\22\3\2\2\2\6\33\3\2\2\2\b*\3\2\2\2\n"
      + "\17\5\4\3\2\13\f\7\3\2\2\f\16\5\4\3\2\r\13\3\2\2\2\16\21\3\2\2\2\17\r"
      + "\3\2\2\2\17\20\3\2\2\2\20\3\3\2\2\2\21\17\3\2\2\2\22\27\5\6\4\2\23\24"
      + "\7\4\2\2\24\26\5\6\4\2\25\23\3\2\2\2\26\31\3\2\2\2\27\25\3\2\2\2\27\30"
      + "\3\2\2\2\30\5\3\2\2\2\31\27\3\2\2\2\32\34\7\r\2\2\33\32\3\2\2\2\33\34"
      + "\3\2\2\2\34(\3\2\2\2\35)\5\b\5\2\36\37\7\5\2\2\37 \5\2\2\2 !\7\6\2\2!"
      + ")\3\2\2\2\"%\6\4\2\2#$\7\16\2\2$&\7\7\2\2%#\3\2\2\2%&\3\2\2\2&\'\3\2\2"
      + "\2\')\7\16\2\2(\35\3\2\2\2(\36\3\2\2\2(\"\3\2\2\2)\7\3\2\2\2*+\7\b\2\2"
      + "+,\7\t\2\2,-\7\n\2\2-.\7\13\2\2./\7\16\2\2/\60\7\13\2\2\60\61\7\f\2\2" + "\61\t\3\2\2\2\7\17\27\33%(";
  public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
  static {
    _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
    for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
      _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
    }
  }
}