package org.jodaengine.deployment.importer.definition.bpmn;

import java.util.List;

import org.jodaengine.node.activity.bpmn.BpmnJavaClassScriptingActivity;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.testng.Assert;

/**
 * Tests the import of a BPMN-xml-file that has a script task defined that refers to a java class.
 * 
 * You can inspect the test process here:
 * http://academic.signavio.com/p/model/71aab663a24140bebf1c3632a86086bf/png?inline&authkey=
 * d9af67a8aa85add848d649e087387231daadc81b66c9fcd535e6679ef57abd
 */
public class DeploySimpleScriptTaskAsBpmnXmlTest extends AbstractBPMNDeployerTest {

    public DeploySimpleScriptTaskAsBpmnXmlTest() {

        executableProcessResourcePath = "org/jodaengine/deployment/importer/definition/bpmn/SimpleScriptTask.bpmn.xml";
    }

    @Override
    protected void assertProcessDefintion(ProcessDefinition processDefinition) {

        List<Node> startNodes = processDefinition.getStartNodes();
        Node startNode = startNodes.get(0);
        Node scriptNode = startNode.getOutgoingTransitions().get(0).getDestination();

        Assert.assertEquals(scriptNode.getActivityBehaviour().getClass(), BpmnJavaClassScriptingActivity.class,
            "The node should have the correct activity behaviour class assigned.");

        BpmnJavaClassScriptingActivity activity = (BpmnJavaClassScriptingActivity) scriptNode.getActivityBehaviour();
        Assert.assertEquals(activity.getScriptingClassName(), "org.jodaengine.an.example.class",
            "The script class should be the one as specified in the process model.");

    }
}
