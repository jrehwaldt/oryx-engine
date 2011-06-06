package org.jodaengine.example.petri;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.node.factory.PetriTransitionFactory;
import org.jodaengine.node.factory.petri.PetriNodeFactory;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.petri.PetriProcessDefinitionBuilder;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class SimpleExampleProcess. It really is just a simple example process.
 */
public final class CompletePetriNetTest {

    /** Hidden constructor. */
    private CompletePetriNetTest() {

    }

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
    public static void main(String[] args)
    throws IllegalStarteventException, DefinitionNotFoundException {


        JodaEngineServices jodaEngineServices = JodaEngine.start();
        ProcessDefinitionID sampleProcessUUID = deploySampleProcess(jodaEngineServices);

        AbstractProcessInstance processInstance = jodaEngineServices.getNavigatorService().startProcessInstance(
            sampleProcessUUID);
        System.out.println(processInstance.getAssignedTokens().size());
        
        

    }

    /**
     * Deploys the sample process.
     *
     * @param jodaEngineServices the joda engine services
     * @return the process definition id
     * @throws IllegalStarteventException the illegal startevent exception
     */
    private static ProcessDefinitionID deploySampleProcess(JodaEngineServices jodaEngineServices)
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
    private static ProcessDefinition buildSampleProcessDefinition(PetriProcessDefinitionBuilder definitionBuilder)
    throws IllegalStarteventException {

        String sampleProcessName = "Sample petri net";
        String sampleProcessDescription = "A simple petri net.";

        Node startPlace, secondStartPlace, endPlace, firstTransition;

        startPlace = PetriNodeFactory.createPlace();
        secondStartPlace = PetriNodeFactory.createPlace();
        endPlace = PetriNodeFactory.createPlace();
        firstTransition = PetriNodeFactory.createPetriTransition();


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
