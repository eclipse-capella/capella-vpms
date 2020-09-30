package org.polarsys.capella.vp.ms.expression.ag;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.polarsys.capella.vp.ms.Situation;
import org.polarsys.capella.vp.ms.expression.parser.MsExpressionUnparser;
import org.polarsys.capella.vp.ms.expression.transfo.ExpressionToDNF;
import org.polarsys.capella.vp.ms.expression.transfo.ExpressionToNNF;
import org.polarsys.capella.vp.ms.util.MsSwitch;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

public class ExcelExporter extends MsSwitch<Object> {

  List<String> smHeaders = new ArrayList<String>();
  
  Map<StateMachine, Integer> smColumns = new HashMap<StateMachine, Integer>();
  
  Map<Situation, List<Multimap<StateMachine, BooleanExpression>>> exportmap = new LinkedHashMap<>();
  int index = 0;
  
  // this maps the root disjunctions to a collection of literals that make up the conjunctions
  Multimap<BooleanExpression, BooleanExpression> literals;
  BooleanExpression root;
  
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

  public void finish(OutputStream out) throws IOException {

    Workbook wb = new XSSFWorkbook();  // or new XSSFWorkbook();

    Sheet sheet1 = wb.createSheet();

    // create the headers
    int rowIndex = 0;
    Row row = sheet1.createRow(rowIndex++);
    for (int i = 0; i < smHeaders.size(); i++) {
      Cell cell = row.createCell(i+1);
      cell.setCellValue(smHeaders.get(i));
    }

    MsExpressionUnparser unparser = new MsExpressionUnparser(MsExpressionUnparser.Mode.NAME);

    for (Situation situation : exportmap.keySet()) {

      row = sheet1.createRow(rowIndex++);
      Cell sitCell = row.createCell(0);
      sitCell.setCellValue(situation.getName());

      StringBuilder[] printableRecord = new StringBuilder[smColumns.size() + 1]; // +1 for the situation column (first column)
      for (int i = 0; i < printableRecord.length; i++) {
        printableRecord[i] = new StringBuilder();
      }
      printableRecord[0].append(situation.getName());

      for (Multimap<StateMachine, BooleanExpression> r : exportmap.get(situation)) {
        for (StateMachine sm : r.keySet()) {
          int colIndex = smColumns.get(sm) + 1;
          Cell cell = row.createCell(colIndex);
          cell.setCellValue(r.get(sm).stream().map(be -> unparser.unparse(be)).collect(Collectors.joining(", ")));
        }
        row = sheet1.createRow(rowIndex++);
      }
    }
    for (int i = 0; i < smColumns.size() + 1; i++) {
      sheet1.autoSizeColumn(i);
    }
    wb.write(out);
  }


  public ExcelExporter export(Situation situation) {

    BooleanExpression dnf = new ExpressionToDNF().doSwitch(new ExpressionToNNF().doSwitch(situation.getExpression()));
    literals = LinkedHashMultimap.create();
    root = dnf;

    // this fills the literals map
    doSwitch(dnf);
    List<Multimap<StateMachine, BooleanExpression>> records = new ArrayList<>();
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
    exportmap.put(situation, records);
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
