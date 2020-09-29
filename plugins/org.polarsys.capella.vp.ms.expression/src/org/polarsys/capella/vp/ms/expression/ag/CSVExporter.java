package org.polarsys.capella.vp.ms.expression.ag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.polarsys.capella.common.helpers.EcoreUtil2;
import org.polarsys.capella.core.data.capellacommon.AbstractState;
import org.polarsys.capella.core.data.capellacommon.CapellacommonPackage;
import org.polarsys.capella.core.data.capellacommon.StateMachine;
import org.polarsys.capella.vp.ms.BooleanExpression;
import org.polarsys.capella.vp.ms.BooleanOperation;
import org.polarsys.capella.vp.ms.InSituationExpression;
import org.polarsys.capella.vp.ms.InStateExpression;
import org.polarsys.capella.vp.ms.MsPackage;
import org.polarsys.capella.vp.ms.NotOperation;
import org.polarsys.capella.vp.ms.expression.parser.MsExpressionUnparser;
import org.polarsys.capella.vp.ms.expression.transfo.ExpressionToDNF;
import org.polarsys.capella.vp.ms.expression.transfo.ExpressionToNNF;
import org.polarsys.capella.vp.ms.util.MsSwitch;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

public class CSVExporter extends MsSwitch<Object> {

  List<String> smHeaders = new ArrayList<String>();
  Map<StateMachine, Integer> smColumns = new HashMap<StateMachine, Integer>();
  List<Multimap<StateMachine, BooleanExpression>> records = new ArrayList<>();

  int index = 0;
  
  // this maps the root disjunctions to a collection of literals that make up the conjunctions
  Multimap<BooleanExpression, BooleanExpression> literals;
  BooleanExpression root;
  CSVFormat format;
  
  public CSVExporter(CSVFormat format) {
    this.format = format;
  }

  private StateMachine getStateMachine(BooleanExpression e) {
    if (e instanceof NotOperation) {
      return getStateMachine(((NotOperation) e).getChildren().iterator().next());
    }
    if (e instanceof InStateExpression) {
      AbstractState s = ((InStateExpression) e).getState();
      return (StateMachine) EcoreUtil2.getFirstContainer(s, CapellacommonPackage.Literals.STATE_MACHINE);
    }
    return null;
  }

  public void finish(Appendable out) throws IOException {

    CSVFormat withHeaders = format.withHeader(smHeaders.toArray(new String[smHeaders.size()]));
    MsExpressionUnparser unparser = new MsExpressionUnparser(MsExpressionUnparser.Mode.NAME);
    CSVPrinter printer = withHeaders.print(out);
    CSVPrinter debug = withHeaders.print(System.out);
    for (Multimap<StateMachine, BooleanExpression> r : records) {
      Object[] printableRecord = new String[smColumns.size()];
      Arrays.fill(printableRecord, "");
      for (StateMachine sm : r.keySet()) {
        int colIndex = smColumns.get(sm);
        printableRecord[colIndex] = r.get(sm).stream().map(be -> unparser.unparse(be)).collect(Collectors.joining(", "));
      }
      printer.printRecord(printableRecord);
      debug.printRecord(printableRecord);
    }
  }

  public CSVExporter export(BooleanExpression expression) {

    BooleanExpression dnf = new ExpressionToDNF().doSwitch(new ExpressionToNNF().doSwitch(expression));
    literals = LinkedHashMultimap.create();
    root = dnf;

    // this fills the literals map
    doSwitch(dnf);

    for (BooleanExpression key : literals.keySet()) {
      Multimap<StateMachine,BooleanExpression> record = LinkedHashMultimap.create();
      for (BooleanExpression element : literals.get(key)) {
        StateMachine sm = getStateMachine(element);
        if (!smColumns.containsKey(sm)) {
          smColumns.put(sm, index++);
          smHeaders.add(sm.getName());
        }
        record.put(sm, element);
      }
      records.add(record);
    }

    return this;
  }

  public Object caseBooleanOperation(BooleanOperation op) {
    for (BooleanExpression child : op.getChildren()) {
      doSwitch(child);
    }
    return this;
  }

  private BooleanExpression findTop(BooleanExpression object) {
    BooleanExpression expr = object;
    while (expr != root) {
      if (expr.eContainer().eClass() == MsPackage.Literals.OR_OPERATION) {
        break;
      }
      expr = (BooleanExpression) expr.eContainer();
    }
    return expr;
  }
  
  @Override
  public Object caseNotOperation(NotOperation object) {
    BooleanExpression top = findTop(object);
    literals.put(top, object);
    return this;
  }

  public Object caseInStateExpression(InStateExpression e) {
    BooleanExpression top = findTop(e);
    literals.put(top, e);
    return this;
  }
  
  public Object caseInSituationExpression(InSituationExpression e) {
    BooleanExpression top = findTop(e);
    literals.put(top, e);
    return this;
  }

}
