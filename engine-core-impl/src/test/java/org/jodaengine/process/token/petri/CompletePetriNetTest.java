package org.jodaengine.process.token.petri;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.navigator.schedule.RandomPetriNetScheduler;
import org.jodaengine.node.factory.PetriTransitionFactory;
import org.jodaengine.node.factory.petri.PetriNodeFactory;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.petri.PetriProcessDefinitionBuilder;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mchange.util.AssertException;

/**
 * The Class SimpleExampleProcess. It really is just a simple example process.
 */
public class CompletePetriNetTest {

    private static JodaEngineServices jodaEngineServices;
    private static ProcessDefinitionID sampleProcessUUID;
    private Node startPlace, secondStartPlace, endPlace, firstTransition;

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CompletePetriNetTest.class);

    /**
     * The main method. It starts a a specified number of instances.
     * 
     * @param args
     *            the arguments
     * @throws IllegalStarteventException fails
     * @throws DefinitionNotFoundException fails
     */
    @BeforeMethod
    public void setUp()
    throws IllegalStarteventException, DefinitionNotFoundException {

        jodaEngineServices = JodaEngine.start();
        sampleProcessUUID = deploySampleProcess(jodaEngineServices);

    }
    
    @AfterMethod
    public void setDown() {
        jodaEngineServices = null;
        sampleProcessUUID = null;
        JodaEngine.shutdown();
    }
    
    @Test
    public void testTheProcessExecution() throws DefinitionNotFoundException, InterruptedException {
        
        AbstractProcessInstance processInstance = jodaEngineServices.getNavigatorService().startProcessInstance(
            sampleProcessUUID);

        Thread.sleep(1000);

        assertEquals(processInstance.getAssignedTokens().size(), 1);
        assertEquals(processInstance.getAssignedTokens().get(0).getCurrentNode(), endPlace);
        
        
    }

    /**
     * Deploys the sample process.
     *
     * @param jodaEngineServices the joda engine services
     * @return the process definition id
     * @throws IllegalStarteventException the illegal startevent exception
     */
    private ProcessDefinitionID deploySampleProcess(JodaEngineServices jodaEngineServices)
    throws IllegalStarteventException {

        DeploymentBuilder deploymentBuilder = jodaEngineServices.getRepositoryService().getDeploymentBuilder();

        ProcessDefinition processDefinition = buildSampleProcessDefinition(PetriProcessDefinitionBuilder.newBuilder());

        ProcessDefinitionID sampleProcessUUID = processDefinition.getID();
        deploymentBuilder.addProcessDefinition(processDefinition);
        
        jodaEngineServices.getRepositoryService().deployInNewScope(deploymentBuilder.buildDeployment());

        return sampleProcessUUID;

    }

    /**
     * Builds the {@link ProcessDefinition} for the sample petri net.
     * <p>
     * @param definitionBuilder the definition builder
     * @return the process definition
     * @throws IllegalStarteventException the illegal startevent exception
     * </p>
     */
    private ProcessDefinition buildSampleProcessDefinition(PetriProcessDefinitionBuilder definitionBuilder)
    throws IllegalStarteventException {

        String sampleProcessName = "Sample petri net";
        String sampleProcessDescription = "A simple petri net.";

        startPlace = PetriNodeFactory.createPlace();
        startPlace.setAttribute("name", "S0");
        secondStartPlace = PetriNodeFactory.createPlace();
        secondStartPlace.setAttribute("name", "S1");
        endPlace = PetriNodeFactory.createPlace();
        endPlace.setAttribute("name", "S3");
        firstTransition = PetriNodeFactory.createPetriTransition();
        firstTransition.setAttribute("name", "T2");


        PetriTransitionFactory.createTransitionFromTo(definitionBuilder, startPlace, firstTransition);
        PetriTransitionFactory.createTransitionFromTo(definitionBuilder, secondStartPlace, firstTransition);
        PetriTransitionFactory.createTransitionFromTo(definitionBuilder, firstTransition, endPlace);
        List<Node> startNodes = new ArrayList<Node>();
        startNodes.add(startPlace);
        startNodes.add(secondStartPlace);

        definitionBuilder.setName(sampleProcessName).setDescription(sampleProcessDescription).setStartNodes(startNodes);

        return definitionBuilder.buildDefinition();
    }
}
