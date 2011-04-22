package de.hpi.oryxengine.process.structure;

import java.util.HashMap;
import java.util.Map;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceContext;
import de.hpi.oryxengine.process.structure.condition.JuelExpressionCondition;
import de.hpi.oryxengine.process.token.Token;

/**
 * It provides several test cases for the {@link JuelExpressionCondition}.
 */
public class JuelExpressionConditionTest {

    private Token token;
    private ProcessInstanceContext context;
    private Map<String, Object> returnMap;
    
    @BeforeMethod
    public void setUp() {
        
        token = Mockito.mock(Token.class);
        AbstractProcessInstance instance = Mockito.mock(AbstractProcessInstance.class);
        context = Mockito.mock(ProcessInstanceContext.class);
        Mockito.when(token.getInstance()).thenReturn(instance);
        Mockito.when(instance.getContext()).thenReturn(context);
        returnMap = new HashMap<String, Object>();
        Mockito.when(context.getVariableMap()).thenReturn(returnMap);
    }
    
    private void addProcessVariable(String variableKey, Object variableValue) {
        
        returnMap.put(variableKey, variableValue);
        Mockito.when(context.getVariableMap()).thenReturn(returnMap);
    }
    
    @Test
    public void testTrueConditionWithVariableBinding() {

        String testVariableId = "testBoolean";
        Object testVariableValue = true;
        
        addProcessVariable(testVariableId, testVariableValue);

        String juelEspression = "${" + testVariableId + "}";
        
        Condition condition = new JuelExpressionCondition(juelEspression);
        
        Assert.assertTrue(condition.evaluate(token));
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
     * This methods tests simple expression like '1 < 2' or '(2+2) == 4' and assert that they become false.
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
