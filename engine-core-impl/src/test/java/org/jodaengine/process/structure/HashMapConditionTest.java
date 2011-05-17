package org.jodaengine.process.structure;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.structure.condition.HashMapCondition;
import org.jodaengine.process.token.Token;


/**
 * The Class ConditionTest.
 */
public class HashMapConditionTest {
    
    /** The condition. */
    private Condition condition = null;
    
    /** The instance. */
    private Token token = null;
    
    /** The context. */
    private ProcessInstanceContext context = null;
  

  /**
   * Test false condition on variable.
   */
  @Test
  public void testFalseConditionOnVariable() {
      assertFalse(condition.evaluate(token), "Condition was true but should be false.");
  }
  
  
  /**
   * Test true condition on variable.
   */
  @Test
  public void testTrueConditionOnVariable() {
      when(context.getVariable("a")).thenReturn(1);
      assertTrue(condition.evaluate(token), "Condition was not true.");
  }
  
  /**
   * Before test.
   */
  @BeforeMethod
  public void beforeMethod() {

      token = mock(Token.class);
      AbstractProcessInstance instance = mock(AbstractProcessInstance.class);
      
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("a", 1);
      condition = new HashMapCondition(map, "==");
      context = mock(ProcessInstanceContext.class);
      
      when(token.getInstance()).thenReturn(instance);
      when(instance.getContext()).thenReturn(context);
  }

  /**
   * After test.
   */
  @AfterMethod
  public void afterMethod() {
      condition = null;
  }

}
