package org.jodaengine.process.structure;

import java.util.HashMap;
import java.util.Map;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.structure.condition.JuelExpressionCondition;
import org.jodaengine.process.token.Token;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * It provides several test cases for the {@link JuelExpressionCondition}.
 */
public class JuelExpressionConditionTest {

    private Token token;
    private ProcessInstanceContext context;
    private Map<String, Object> returnMap;

    /**
     * Set up.
     */
    @BeforeMethod
    public void setUp() {

        // Preparing all the necessary mocks for the evaluation of the condition
        token = Mockito.mock(Token.class);
        AbstractProcessInstance instance = Mockito.mock(AbstractProcessInstance.class);
        context = Mockito.mock(ProcessInstanceContext.class);
        Mockito.when(token.getInstance()).thenReturn(instance);
        Mockito.when(instance.getContext()).thenReturn(context);

        // The method 'getVariableMap()' should return a map
        returnMap = new HashMap<String, Object>();
        Mockito.when(context.getVariableMap()).thenReturn(returnMap);
    }

    /**
     * Adds a context variable to the process.
     * 
     * @param variableKey the key
     * @param variableValue the value
     */
    private void addProcessVariable(String variableKey, Object variableValue) {

        returnMap.put(variableKey, variableValue);
        Mockito.when(context.getVariable(variableKey)).thenReturn(variableValue);
        Mockito.when(context.getVariableMap()).thenReturn(returnMap);
    }

    /**
     * This method tests a expression that binds a variable that is available in the {@link ProcessInstanceContext
     * ProcessContext}.
     */
    @Test
    public void testTrueConditionWithVariableBinding() {

        addProcessVariable("testBoolean", true);
        String juelEspression = "${testBoolean}";
        Condition condition = new JuelExpressionCondition(juelEspression);

        Assert.assertTrue(condition.evaluate(token));
    }

    /**
     * This method tests a expression that binds a variable that is available in the {@link ProcessInstanceContext
     * ProcessContext}.
     */
    @Test
    public void testFalseConditionWithVariableBinding() {
        
        addProcessVariable("testBoolean", false);
        String juelEspression = "${testBoolean}";
        Condition condition = new JuelExpressionCondition(juelEspression);
        
        Assert.assertFalse(condition.evaluate(token));
    }
    
    /**
     * This method tests a expression that binds a variable that is available in the {@link ProcessInstanceContext
     * ProcessContext}.
     */
    @Test
    public void testTrueMoreComplexConditionWithVariableBinding() {

        addProcessVariable("testInt1", 1);
        addProcessVariable("testInt2", 2);
        String juelEspression = "${testInt1 + testInt2 == 3}";
        Condition condition = new JuelExpressionCondition(juelEspression);

        Assert.assertTrue(condition.evaluate(token));
    }

    /**
     * This method tests a expression that binds a variable that is available in the {@link ProcessInstanceContext
     * ProcessContext}.
     */
    @Test(expectedExceptions = JodaEngineRuntimeException.class)
    public void testErrorInCondition() {
        
        addProcessVariable("testBoolean", true);
        String juelEspression = "${testBoolean2==12312}";
        Condition condition = new JuelExpressionCondition(juelEspression);
        
        Assert.assertTrue(condition.evaluate(token));
        
        Assert.fail("An exception should have been raised.");
    }
    
    
    /**
     * This methods tests simple expression like '1 < 1' or '(2+2) == 5' and assert that they become false.
     */
    @Test
    public void testFalseSimpleCondition() {

        String juelEspression = "${2 == 3}";
        Condition condition = new JuelExpressionCondition(juelEspression);
        Assert.assertFalse(condition.evaluate(token));

        juelEspression = "${3 > 4}";
        condition = new JuelExpressionCondition(juelEspression);
        Assert.assertFalse(condition.evaluate(token));

        juelEspression = "${(2+2) >= 5}";
        condition = new JuelExpressionCondition(juelEspression);
        Assert.assertFalse(condition.evaluate(token));
    }

    /**
     * This methods tests simple expression like '1 < 2' or '(2+2) == 4' and assert that they become true.
     */
    @Test
    public void testTrueSimpleCondition() {

        String juelEspression = "${2+2 == 4}";
        Condition condition = new JuelExpressionCondition(juelEspression);
        Assert.assertTrue(condition.evaluate(token));

        juelEspression = "${3 < 4}";
        condition = new JuelExpressionCondition(juelEspression);
        Assert.assertTrue(condition.evaluate(token));

        juelEspression = "${3 <= 3}";
        condition = new JuelExpressionCondition(juelEspression);
        Assert.assertTrue(condition.evaluate(token));
    }
}
