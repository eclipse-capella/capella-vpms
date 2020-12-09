package org.polarsys.capella.vp.ms.expression.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.polarsys.capella.core.data.capellacommon.AbstractState;
import org.polarsys.capella.core.data.capellacommon.CapellacommonFactory;
import org.polarsys.capella.core.data.capellacommon.Region;
import org.polarsys.capella.core.data.capellacommon.StateMachine;
import org.polarsys.capella.vp.ms.BooleanExpression;
import org.polarsys.capella.vp.ms.expression.parser.DefaultMsExpressionVisitor;
import org.polarsys.capella.vp.ms.expression.parser.MsExpressionBaseListener;
import org.polarsys.capella.vp.ms.expression.parser.MsExpressionLexer;
import org.polarsys.capella.vp.ms.expression.parser.MsExpressionParser;
import org.polarsys.capella.vp.ms.expression.parser.MsExpressionParser.NotExprContext;
import org.polarsys.capella.vp.ms.expression.parser.MsExpressionUnparser;
import org.polarsys.capella.vp.ms.expression.transfo.ExpressionToDNF;
import org.polarsys.capella.vp.ms.expression.transfo.ExpressionToNNF;



public class DefaultMsExpressionVisitorTest {

  public static final String GRAMMAR_ID = "org.polarsys.capella.vp.ms.expression.parser.MsExpression";
  public static final Map<String, EObject> IDMAP = new HashMap<String, EObject>();
  
  private BooleanExpression _testVisitor(ParseTree tree) throws IOException {
    BooleanExpression expr = new DefaultMsExpressionVisitor(IDMAP::get).visit(tree);
    return expr;
  }

  @ParameterizedTest
  @MethodSource("provideTestExpressions")
  public void testParser(String testExpr, String roundtripExpectedOut, String nnf, String dnf) throws Exception {

    StateMachine defaultStateMachine = makeStatemachine("default");
    
    String expression = translate(testExpr, defaultStateMachine);

    ANTLRInputStream input = new ANTLRInputStream(expression);
    // create a lexer that feeds off of input CharStream 
    MsExpressionLexer lexer = new MsExpressionLexer(input); 
    // create a buffer of tokens pulled from the lexer 

    CommonTokenStream tokens = new CommonTokenStream(lexer);

    MsExpressionParser parser = new MsExpressionParser(tokens);
    ParseTree tree = parser.orExpr();

    
    // now translate this into a BooleanExpression EMF model
    BooleanExpression msExpression = _testVisitor(tree);

    MsExpressionUnparser up = new MsExpressionUnparser(MsExpressionUnparser.Mode.QNAME);
    up.setDefaultStatemachine(defaultStateMachine);
    
    // and unparse this EMF expression back to test text and see if the roundtrip result is the expected one
    String unparsedTestExpr = up.unparse(msExpression);
    
    String expected = roundtripExpectedOut;
    if ("-".equals(roundtripExpectedOut)) {
      expected = testExpr;
    }
    assertEquals(expected, unparsedTestExpr);

    
    String expectedNNF = nnf;
    if ("-".equals(nnf)) {
      expectedNNF = expected;
    }
    BooleanExpression nnfExpression = new ExpressionToNNF().doSwitch(msExpression);
    String unparsedNNFExpr = up.unparse(nnfExpression);
    assertEquals(expectedNNF, unparsedNNFExpr);
    
    String expectedDNF = dnf;
    if ("-".equals(dnf)) {
      expectedDNF = expectedNNF;
    }
    BooleanExpression dnfExpression = new ExpressionToDNF().doSwitch(nnfExpression);
    String unparsedDNFExpr = up.unparse(dnfExpression);
    assertEquals(expectedDNF, unparsedDNFExpr);
    
  }

  private static StateMachine makeStatemachine(String name) {
    StateMachine sm = CapellacommonFactory.eINSTANCE.createStateMachine(name);
    sm.getOwnedRegions().add(CapellacommonFactory.eINSTANCE.createRegion());
    return sm;
  }
  
  // translates the simple expressions in the fixture file, 
  // the test expressions use symbols like A B C, instead of clumsy <a href='...'> tags
  // so we first parse these test expressions and then translate them into their clumsy version...
  private static String translate(String inputText, StateMachine defaultStateMachine) {
    ANTLRInputStream input = new ANTLRInputStream(inputText);
    // create a lexer that feeds off of input CharStream 
    MsExpressionLexer lexer = new MsExpressionLexer(input); 
    // create a buffer of tokens pulled from the lexer 
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    MsExpressionParser parser = new MsExpressionParser(tokens, true);
    ParseTree tree = parser.orExpr();
    ParseTreeWalker walker = new ParseTreeWalker();
    final TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
    Map<String, StateMachine> stateMachines = new HashMap<String, StateMachine>();
    stateMachines.put("default", defaultStateMachine);

    MsExpressionBaseListener listener = new MsExpressionBaseListener() {
        @Override
        public void exitNotExpr(NotExprContext ctx) {
          if (!ctx.ID().isEmpty()) {
            
            // presume unqualified
            String smName = "default";
            int stateIndex = 0;
            
            if (ctx.ID(1) != null) {
              smName = ctx.ID(0).getText();
              stateIndex = 1;
            }
            
            StateMachine sm = stateMachines.get(smName);
            if (sm == null) {
              sm = makeStatemachine(ctx.ID(0).getText());
              stateMachines.put(sm.getName(), sm);
            }
            
            String stateName = ctx.ID(stateIndex).getText();
            Region region = sm.getOwnedRegions().get(0);
            AbstractState state = null;
            for (AbstractState s : region.getOwnedStates()) {
              if (smName.equals(s.getName())) {
                state = s;
                break;
              }
            }

            if (state == null) {
              state = CapellacommonFactory.eINSTANCE.createState(stateName);
              region.getOwnedStates().add(state);
              IDMAP.put(state.getId(), state);
            }

            rewriter.replace(ctx.ID(0).getSymbol(), ctx.stop,
                "<a href=\"" + state.getId() + "\"/>");
          }
        }
    };
    walker.walk(listener, tree);
    return rewriter.getText();
  }
  

  private static Collection<Arguments> provideTestExpressions() throws IOException {
    Collection<Arguments> result = new ArrayList<>();
    
    try (BufferedReader in = new BufferedReader(
        new InputStreamReader(
            FileLocator.openStream(Platform.getBundle("org.polarsys.capella.vp.ms.tests"), new Path("fixtures.txt"), false)))){
      String inputline = null;
      Object[] args = new String[4];
      int argIndex = 0;
      while ((inputline = in.readLine()) != null) {
        if (!inputline.startsWith("#")) {
          if (inputline.trim().isEmpty()) {
            result.add(Arguments.of(args));
            argIndex = 0;
            args = new String[4];
          } else if (argIndex <= 3) {
            args[argIndex] = inputline.trim();
            argIndex++;
          }
        }
      }
    }
    return result;
}
}
