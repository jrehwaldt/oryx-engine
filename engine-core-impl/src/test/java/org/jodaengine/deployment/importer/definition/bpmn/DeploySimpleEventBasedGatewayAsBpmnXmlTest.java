package org.jodaengine.deployment.importer.definition.bpmn;

import java.lang.reflect.Field;
import java.util.List;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.activity.NullActivity;
import org.jodaengine.node.activity.bpmn.BpmnEndEventActivity;
import org.jodaengine.node.activity.bpmn.BpmnEventBasedXorGateway;
import org.jodaengine.node.activity.bpmn.BpmnStartEvent;
import org.jodaengine.node.activity.bpmn.BpmnTimerIntermediateEventActivity;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.XORSplitBehaviour;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.testng.Assert;

/**
 * It tests the deployment of BPMN processes that where serialized as xml. The xml contains the structure the process
 * and additional information like DI-Information.
 * 
 * The process were modeled and exported as xml using academic signavio. The process contains two pairs of split and
 * join gateways.
 * 
 * The process can be inspected here (authkey important):
 * http://academic.signavio.com/p/model/b3f9ac0d3b4f46ff8f21426c0f641338
 * /png?inline&authkey=eb5d7e6bdadc19a3878bdfb0e31f93844160d2b779efe74d3a64a188a5ce31ff
 * 
 */
public class DeploySimpleEventBasedGatewayAsBpmnXmlTest extends AbstractBPMNDeployerTest {

    public DeploySimpleEventBasedGatewayAsBpmnXmlTest() {

        executableProcessResourcePath = "org/jodaengine/deployment/importer/definition/bpmn/SimpleEventBasedGateway.bpmn.xml";
    }

    @Override
    protected void assertProcessDefintion(ProcessDefinition processDefinition) {

        List<Node> startNodes = processDefinition.getStartNodes();
        Assert.assertEquals(startNodes.size(), 1);

        Node onlyStartNode = startNodes.get(0);
        Assert.assertEquals(extractClass(onlyStartNode), BpmnStartEvent.class);
        Assert.assertEquals(onlyStartNode.getAttribute("name"), "Start");
        Assert.assertEquals(onlyStartNode.getOutgoingControlFlows().size(), 1);

        Node nextNode = onlyStartNode.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(extractClass(nextNode), BpmnEventBasedXorGateway.class);
        Assert.assertNull(nextNode.getAttribute("name"));
        Assert.assertEquals(nextNode.getOutgoingControlFlows().size(), 2);
        Node eventBasedXorGatewayNode = nextNode;

        nextNode = eventBasedXorGatewayNode.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(extractClass(nextNode), BpmnTimerIntermediateEventActivity.class);
        Assert.assertEquals(getTimeIntervall((BpmnTimerIntermediateEventActivity) nextNode.getActivityBehaviour()),
            10000);
        Assert.assertEquals(nextNode.getOutgoingControlFlows().size(), 1);
        Node timerIntermendiateEvent10Sec = nextNode;

        nextNode = eventBasedXorGatewayNode.getOutgoingControlFlows().get(1).getDestination();
        Assert.assertEquals(extractClass(nextNode), BpmnTimerIntermediateEventActivity.class);
        Assert.assertEquals(getTimeIntervall((BpmnTimerIntermediateEventActivity) nextNode.getActivityBehaviour()),
            20000);
        Assert.assertEquals(nextNode.getOutgoingControlFlows().size(), 1);
        Node timerIntermendiateEvent20Sec = nextNode;

        Node xorGatewayJoinNode = timerIntermendiateEvent10Sec.getOutgoingControlFlows().get(0).getDestination();
        nextNode = timerIntermendiateEvent20Sec.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(nextNode, xorGatewayJoinNode);
        Assert.assertEquals(extractClass(nextNode), NullActivity.class);
        Assert.assertEquals(nextNode.getIncomingBehaviour().getClass(), SimpleJoinBehaviour.class);
        Assert.assertEquals(nextNode.getOutgoingBehaviour().getClass(), XORSplitBehaviour.class);

        Node endNode = nextNode.getOutgoingControlFlows().get(0).getDestination();
        Assert.assertEquals(extractClass(endNode), BpmnEndEventActivity.class);
        Assert.assertEquals(endNode.getAttribute("name"), "End");
        Assert.assertEquals(endNode.getOutgoingControlFlows().size(), 0);
    }

    /**
     * Little helper for this test in order make short method call.
     */
    public Class<? extends Activity> extractClass(Node node) {

        return node.getActivityBehaviour().getClass();
    }

    private static long getTimeIntervall(BpmnTimerIntermediateEventActivity timerIntermediateEventActivity) {

        try {
            Field time = timerIntermediateEventActivity.getClass().getDeclaredField("time");
            time.setAccessible(true);
            return (Long) time.get(timerIntermediateEventActivity);
        } catch (SecurityException e) {
            throw new JodaEngineRuntimeException("The field 'time' could not be found in the object "
                + timerIntermediateEventActivity, e);
        } catch (NoSuchFieldException e) {
            throw new JodaEngineRuntimeException("The field 'time' could not be found in the object "
                + timerIntermediateEventActivity, e);
        } catch (IllegalArgumentException e) {
            throw new JodaEngineRuntimeException("The field 'time' could not be found in the object "
                + timerIntermediateEventActivity, e);
        } catch (IllegalAccessException e) {
            throw new JodaEngineRuntimeException("The field 'time' could not be found in the object "
                + timerIntermediateEventActivity, e);
        }
    }
}
