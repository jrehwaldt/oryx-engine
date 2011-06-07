package org.jodaengine.deployment.importer.definition.bpmn;

import java.util.List;

import org.jodaengine.node.activity.bpmn.BpmnJavaServiceActivity;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.testng.Assert;

/**
 * Tests the import of a BPMN-xml-file that has a script task defined that refers to a java class.
 * 
 * You can inspect the test process here:
 * http://academic.signavio.com/p/model/14e78858a7484bf5886ae89ebb909c0c/png?inline&authkey=
 * fb977a7c40ec4784e8e53d7cbd2d89c2c561396d74c3649ea15645ece65bd
 */
public class DeploySimpleJavaTaskAsBpmnXmlTest extends AbstractBPMNDeployerTest {

    public DeploySimpleJavaTaskAsBpmnXmlTest() {

        executableProcessResourcePath = 
            "org/jodaengine/deployment/importer/definition/bpmn/SimpleJavaServiceTask.bpmn.xml";
    }

    @Override
    protected void assertProcessDefintion(ProcessDefinition processDefinition) {

        List<Node> startNodes = processDefinition.getStartNodes();
        Node startNode = startNodes.get(0);
        Node scriptNode = startNode.getOutgoingControlFlows().get(0).getDestination();

        Assert.assertEquals(scriptNode.getActivityBehaviour().getClass(), BpmnJavaServiceActivity.class,
            "The node should have the correct activity behaviour class assigned.");

        BpmnJavaServiceActivity activity = (BpmnJavaServiceActivity) scriptNode.getActivityBehaviour();
        Assert.assertEquals(activity.getServiceClassName(), "org.jodaengine.an.example.class",
            "The script class should be the one as specified in the process model.");

    }
}
