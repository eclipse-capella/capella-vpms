// Generated from MsExpression.g4 by ANTLR 4.3
package org.polarsys.capella.vp.ms.expression.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MsExpressionLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__9=1, T__8=2, T__7=3, T__6=4, T__5=5, T__4=6, T__3=7, T__2=8, T__1=9, 
		T__0=10, NEG=11, ID=12, WS=13;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'"
	};
	public static final String[] ruleNames = {
		"T__9", "T__8", "T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", 
		"T__0", "NEG", "ID", "WS"
	};


	public MsExpressionLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "MsExpression.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\17I\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4"+
		"\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3\13"+
		"\3\13\3\f\3\f\3\f\3\f\3\r\6\r?\n\r\r\r\16\r@\3\16\6\16D\n\16\r\16\16\16"+
		"E\3\16\3\16\2\2\17\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r"+
		"\31\16\33\17\3\2\4\b\2%%//\62;C\\aac|\5\2\13\f\17\17\"\"J\2\3\3\2\2\2"+
		"\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2"+
		"\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2"+
		"\2\33\3\2\2\2\3\35\3\2\2\2\5 \3\2\2\2\7%\3\2\2\2\t\'\3\2\2\2\13*\3\2\2"+
		"\2\r,\3\2\2\2\17.\3\2\2\2\21\61\3\2\2\2\23\65\3\2\2\2\25\67\3\2\2\2\27"+
		"9\3\2\2\2\31>\3\2\2\2\33C\3\2\2\2\35\36\7Q\2\2\36\37\7T\2\2\37\4\3\2\2"+
		"\2 !\7j\2\2!\"\7t\2\2\"#\7g\2\2#$\7h\2\2$\6\3\2\2\2%&\7$\2\2&\b\3\2\2"+
		"\2\'(\7>\2\2()\7c\2\2)\n\3\2\2\2*+\7*\2\2+\f\3\2\2\2,-\7+\2\2-\16\3\2"+
		"\2\2./\7\61\2\2/\60\7@\2\2\60\20\3\2\2\2\61\62\7C\2\2\62\63\7P\2\2\63"+
		"\64\7F\2\2\64\22\3\2\2\2\65\66\7?\2\2\66\24\3\2\2\2\678\7\60\2\28\26\3"+
		"\2\2\29:\7P\2\2:;\7Q\2\2;<\7V\2\2<\30\3\2\2\2=?\t\2\2\2>=\3\2\2\2?@\3"+
		"\2\2\2@>\3\2\2\2@A\3\2\2\2A\32\3\2\2\2BD\t\3\2\2CB\3\2\2\2DE\3\2\2\2E"+
		"C\3\2\2\2EF\3\2\2\2FG\3\2\2\2GH\b\16\2\2H\34\3\2\2\2\5\2@E\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}