/*******************************************************************************
 * Copyright (c) 2020, 2024 THALES GLOBAL SERVICES.
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
package org.polarsys.capella.vp.ms.expression.ag;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
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

  List<StateMachine> statemachines = new ArrayList<>();
  Map<Situation, List<Multimap<StateMachine, BooleanExpression>>> exportmap = new LinkedHashMap<>();

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

  private void finish(OutputStream out, Comparator<StateMachine> columnOrder) throws IOException {

    statemachines.sort(columnOrder);

    Workbook wb = new XSSFWorkbook();  // or new XSSFWorkbook();

    Sheet sheet1 = wb.createSheet();

    // create the headers
    int rowIndex = 0;
    Row row = sheet1.createRow(rowIndex++);
    row.createCell(0).setCellValue("situation");
    for (int i = 0; i < statemachines.size(); i++) {
      Cell cell = row.createCell(i+1);
      cell.setCellValue(statemachines.get(i).getName());
    }

    // filling the table
    MsExpressionUnparser unparser = new MsExpressionUnparser(MsExpressionUnparser.Mode.NAME);

    for (Situation situation : exportmap.keySet()) {
      for (Multimap<StateMachine, BooleanExpression> r : exportmap.get(situation)) {
        row = sheet1.createRow(rowIndex++);
        row.createCell(0).setCellValue(situation.getName());
        for (StateMachine sm : r.keySet()) {
          int colIndex = statemachines.indexOf(sm) + 1;
          Cell cell = row.createCell(colIndex);
          cell.setCellValue(r.get(sm).stream().map(be -> unparser.unparse(be)).collect(Collectors.joining(", ")));
        }
      }
      sheet1.createRow(rowIndex++); // empty line between each situation's segment
    }

    // autoresize every column
    for (int i = 0; i < statemachines.size() + 1; i++) {
      sheet1.autoSizeColumn(i);
    }

    // save & close
    wb.write(out);
    wb.close();
  }

  /**
   * Export a collection of situations. This method may only be called once for every exporter.
   * 
   * @param situations
   * @param out
   * @throws IOException
   */
  public void export(Collection<? extends Situation> situations, Comparator<StateMachine> columnOrder, OutputStream out) throws IOException {
    for (Situation s : situations) {
      export(s);
    }
    finish(out, columnOrder);
  }

  private ExcelExporter export(Situation situation) {

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
        if (!statemachines.contains(sm)) {
          statemachines.add(sm);
        }
        record.put(sm, element);
      }
      records.add(record);
    }
    exportmap.put(situation, records);
    return this;
  }

  @Override
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

  @Override
  public Object caseInStateExpression(InStateExpression e) {
    BooleanExpression top = findTop(e);
    literals.put(top, e);
    return this;
  }
  
  @Override
  public Object caseInSituationExpression(InSituationExpression e) {
    BooleanExpression top = findTop(e);
    literals.put(top, e);
    return this;
  }

}
