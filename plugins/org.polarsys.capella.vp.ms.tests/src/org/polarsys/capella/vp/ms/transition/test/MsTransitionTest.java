/*******************************************************************************
 * Copyright (c) 2020 THALES GLOBAL SERVICES.
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
package org.polarsys.capella.vp.ms.transition.test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.polarsys.capella.common.data.modellingcore.ModelElement;
import org.polarsys.capella.core.data.la.LogicalComponent;
import org.polarsys.capella.test.transition.ju.TopDownTransitionTestCase;
import org.polarsys.capella.vp.ms.CSConfiguration;

public class MsTransitionTest extends TopDownTransitionTestCase {

  @Override
  public List<String> getRequiredTestModels() {
    return Collections.singletonList("MsTransitionTest");
  }

  @BeforeEach
  public void j5setup() throws Exception {
    setUp();
  }
  
  @AfterEach
  public void j5teadrown() throws Exception {
    tearDown();
  }
  
  @Test
  public void junit5testTransition() throws Exception {
  // TODO Auto-generated method stub
    
    LogicalComponent lc = getObject("8d465cd0-5a81-4b15-85f6-9315fe026a90");
    
    CSConfiguration c = getObject("a234fcac-821c-46dc-a6f1-1a5a73a4bf18");
    Collection<ModelElement> excluded = c.getExcluded();
    Collection<ModelElement> included = c.getIncluded();

    assertFalse(excluded.isEmpty());
    assertFalse(included.isEmpty());
    
    performLCtoPCTransition(Collections.singleton(lc));

    CSConfiguration cstar = mustBeMonoTransitioned(c);
    assertEquals(cstar.getExcluded().size(), excluded.size());
    assertEquals(cstar.getIncluded().size(), included.size());
    for (ModelElement e : cstar.getExcluded()) {
      for (EObject traced : getAllocatedElements(e)) {
        assertTrue(c.getExcluded().contains(traced));
      }
    }
    for (ModelElement e : cstar.getIncluded()) {
      for (EObject traced : getAllocatedElements(e)) {
        assertTrue(c.getIncluded().contains(traced));
      }
    }
    
    assertEquals(c.getName(), cstar.getName());
    assertEquals(c.getKind(), cstar.getKind());

  }

  @Override
  public void performTest() throws Exception {
    junit5testTransition();
  }

}
