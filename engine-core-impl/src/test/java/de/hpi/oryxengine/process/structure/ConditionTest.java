package de.hpi.oryxengine.process.structure;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * The Class ConditionTest.
 */
public class ConditionTest {
    
    /** The condition. */
    private Condition condition;
    
    /** The instance. */
    private ProcessInstance instance;
  

  /**
   * Test false condition on variable.
   */
  @Test
  public void testFalseConditionOnVariable() {
      assertFalse(condition.evaluate(instance), "Condition was true but should be false.");
  }
  
  /**
  * Test set false.
  */
  @Test
  public void testSetFalse() {
    condition.setFalse();
    assertFalse(condition.evaluate(instance), "Set false didnt happened.");
  }
  
  /**
   * Test true condition on variable.
   */
  @Test
  public void testTrueConditionOnVariable() {
      when(instance.getVariable("a")).thenReturn(1);
      assertTrue(condition.evaluate(instance), "Condition was not true.");
  }
  
  /**
   * Before test.
   */
  @BeforeMethod
  public void beforeMethod() {
      instance = mock(ProcessInstance.class);
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("a", 1);
      condition = new ConditionImpl(map);
  }

  /**
   * After test.
   */
  @AfterMethod
  public void afterMethod() {
      condition = null;
  }

}
