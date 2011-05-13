package de.hpi.oryxengine.factories.process;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.allocation.Form;
import de.hpi.oryxengine.deployment.DeploymentBuilder;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.node.factory.bpmn.BpmnCustomNodeFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnNodeFactory;
import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.condition.HashMapCondition;
import de.hpi.oryxengine.process.structure.condition.JuelExpressionCondition;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Role;
import de.hpi.oryxengine.resource.allocation.CreationPatternBuilder;
import de.hpi.oryxengine.resource.allocation.CreationPatternBuilderImpl;
import de.hpi.oryxengine.resource.allocation.FormImpl;
import de.hpi.oryxengine.resource.allocation.pattern.OfferMultiplePattern;

/**
 * The Class ShortenedReferenceProcessDeployer. This is the implementation of the shortened version of the AOK reference
 * process.
 */
public class ShortenedReferenceProcessDeployer extends AbstractProcessDeployer {

    // configuration constants
    private static final String JANNIK = "Jannik";
    private static final String TOBI = "Tobi";
    private static final String GERARDO = "Gerardo";
    private static final String JAN = "Jan";
    private static final String OBJECTION_CLERK = "Objection Clerk";
    private static final String ALLOWANCE_CLERK = "Allowance Clerk";

    private static final String PATH_TO_WEBFORMS = "src/main/resources/forms";
    private Logger logger = LoggerFactory.getLogger(getClass());

    // Nodes
    private Node startNode;
    private Node system1;
    private Node system2;
    private Node human1;
    private Node human2;
    private Node human3;
    private Node human4;
    private Node human5;
    private Node xor1;
    private Node xor2;
    private Node xor3;
    private Node xor4;
    private Node xor5;
    private Node endNode;

    public ShortenedReferenceProcessDeployer() {
    }

    /**
     * Gets the start node.
     * 
     * @return the start node
     */
    public Node getStartNode() {

        return startNode;
    }

    /**
     * Gets the first system task.
     * 
     * @return the system1
     */
    public Node getSystem1() {

        return system1;
    }

    /**
     * Gets the second system task.
     * 
     * @return the system2
     */
    public Node getSystem2() {

        return system2;
    }

    /**
     * Gets the first human task.
     * 
     * @return the human1
     */
    public Node getHuman1() {

        return human1;
    }

    /**
     * Gets the second human task.
     * 
     * @return the human2
     */
    public Node getHuman2() {

        return human2;
    }

    /**
     * Gets the third human task.
     * 
     * @return the human3
     */
    public Node getHuman3() {

        return human3;
    }

    /**
     * Gets the fourth human task.
     * 
     * @return the human4
     */
    public Node getHuman4() {

        return human4;
    }

    /**
     * Gets the fifth human task.
     * 
     * @return the human5
     */
    public Node getHuman5() {

        return human5;
    }

    /**
     * Gets first the xor.
     * 
     * @return the xor1
     */
    public Node getXor1() {

        return xor1;
    }

    /**
     * Gets the second xor.
     * 
     * @return the xor2
     */
    public Node getXor2() {

        return xor2;
    }

    /**
     * Gets the third xor.
     * 
     * @return the xor3
     */
    public Node getXor3() {

        return xor3;
    }

    /**
     * Gets the fourth xor.
     * 
     * @return the xor4
     */
    public Node getXor4() {

        return xor4;
    }

    /**
     * Gets the fifth xor.
     * 
     * @return the xor5
     */
    public Node getXor5() {

        return xor5;
    }

    /**
     * Gets the end node.
     * 
     * @return the end node
     */
    public Node getEndNode() {

        return endNode;
    }

    // roles and participants
    private Role objectionClerk;
    private Role allowanceClerk;
    private Participant jan;
    private Participant gerardo;
    private Participant tobi;
    private Participant jannik;

    /**
     * Gets the objection clerk.
     * 
     * @return the objection clerk
     */
    public Role getObjectionClerk() {

        return objectionClerk;
    }

    /**
     * Gets the participant "Jan".
     * 
     * @return the participant "Jan"
     */
    public Participant getJan() {

        return jan;
    }

    /**
     * Gets the participant "Gerardo".
     * 
     * @return the participant "Gerardo"
     */
    public Participant getGerardo() {

        return gerardo;
    }

    /**
     * Gets the participant "Tobi".
     * 
     * @return the participant "Tobi"
     */
    public Participant getTobi() {

        return tobi;
    }

    /**
     * Gets the participant "Jannik".
     * 
     * @return the participant "Jannik"
     */
    public Participant getJannik() {

        return jannik;
    }

    @Override
    public void initializeNodes() {

        // start node, blank
        startNode = BpmnNodeFactory.createBpmnStartEventNode(processDefinitionBuilder);

        system1 = BpmnCustomNodeFactory.createBpmnPrintingVariableNode(processDefinitionBuilder,
            "Widerspruch wird vorbearbeitet");

        // human task for objection clerk, task is to check
        // positions of objection
        Form form = extractForm("form1", "claimPoints.html");
        CreationPatternBuilder builder = new CreationPatternBuilderImpl();
        builder.setItemDescription("Anspruchspositionen überprüfen").setItemSubject("Positionen auf Anspruch prüfen")
        .setItemForm(form).addResourceAssignedToItem(objectionClerk);
        // Task task = createRoleTask("Positionen auf Anspruch prüfen", "Anspruchspositionen überprüfen", form,
        // objectionClerk);
        human1 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder,
            builder.buildConcreteResourcePattern(), new OfferMultiplePattern());

        // XOR Split, condition is objection existence
        xor1 = BpmnNodeFactory.createBpmnXorGatewayNode(processDefinitionBuilder);

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("widerspruch", "stattgegeben");
        // Condition condition1 = new HashMapCondition(map1, "==");
        Condition condition1 = new JuelExpressionCondition("${widerspruch  == \"stattgegeben\"}");

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("widerspruch", "abgelehnt");
        // Condition condition2 = new HashMapCondition(map2, "==");
        Condition condition2 = new JuelExpressionCondition("${widerspruch  == \"abgelehnt\"}");

        // human task for objection clerk, task is to check objection
        form = extractForm("form2", "checkForNewClaims.html");
        builder.setItemDescription("Widerspruch erneut prüfen auf neue Ansprüche").setItemSubject("Widerspruch prüfen")
        .setItemForm(form);
        // task = createRoleTask("Widerspruch prüfen", "Widerspruch erneut prüfen auf neue Ansprüche", form,
        // objectionClerk);
        human2 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder,
            builder.buildConcreteResourcePattern(), new OfferMultiplePattern());

        // XOR Split, condition is new relevant aspects existence
        xor2 = BpmnNodeFactory.createBpmnXorGatewayNode(processDefinitionBuilder);
        map1 = new HashMap<String, Object>();
        map1.put("neue Aspekte", "ja");
        Condition condition3 = new HashMapCondition(map1, "==");
        map2 = new HashMap<String, Object>();
        map2.put("neue Aspekte", "nein");
        Condition condition4 = new HashMapCondition(map1, "==");

        // human task for objection clerk, task is to create a new report
        form = extractForm("form3", "createReport.html");
        builder.setItemDescription("Anspruchspunkte in neues Gutachten übertragen")
        .setItemSubject("neues Gutachten erstellen").setItemForm(form);
        // task = createRoleTask("neues Gutachten erstellen", "Anspruchspunkte in neues Gutachten übertragen", form,
        // objectionClerk);
        human3 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder,
            builder.buildConcreteResourcePattern(), new OfferMultiplePattern());

        // intermediate mail event, customer answer
        // needs to be implemented and inserted here

        // XOR Split, condition is existence of objection in answer of customer
        xor3 = BpmnNodeFactory.createBpmnXorGatewayNode(processDefinitionBuilder);
        map1 = new HashMap<String, Object>();
        map1.put("aufrecht", "ja");
        // Condition condition5 = new HashMapCondition(map1, "==");
        Condition condition5 = new JuelExpressionCondition("${aufrecht  == \"ja\"}");
        map2 = new HashMap<String, Object>();
        map2.put("aufrecht", "nein");

        // XOR Join
        xor4 = BpmnNodeFactory.createBpmnXorGatewayNode(processDefinitionBuilder);

        // human task for objection clerk, task is to do final work
        form = extractForm("form4", "postEditingClaim.html");
        builder.setItemDescription("abschließende Nachbearbeitung des Falls").setItemSubject("Nachbearbeitung")
        .setItemForm(form);
        // task = createRoleTask("Nachbearbeitung", "abschließende Nachbearbeitung des Falls", form, objectionClerk);
        human4 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder,
            builder.buildConcreteResourcePattern(), new OfferMultiplePattern());

        // human task for allowance clerk, task is to enforce allowance
        form = extractForm("form5", "enforceAllowance.html");
        builder.flushAssignedResources().setItemDescription("Leistungsansprüche durchsetzen")
        .setItemSubject("Leistungsgewährung umsetzen").setItemForm(form).addResourceAssignedToItem(allowanceClerk);
        // task = createRoleTask("Leistungsgewährung umsetzen", "Leistungsansprüche durchsetzen", form, allowanceClerk);
        human5 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder,
            builder.buildConcreteResourcePattern(), new OfferMultiplePattern());

        // final XOR Join
        xor5 = BpmnNodeFactory.createBpmnXorGatewayNode(processDefinitionBuilder);

        // system task, close file
        system2 = BpmnCustomNodeFactory.createBpmnPrintingVariableNode(processDefinitionBuilder,
            "Akte wird geschlossen");

        // end node
        endNode = BpmnNodeFactory.createBpmnEndEventNode(processDefinitionBuilder);

        // connect the nodes
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, startNode, system1);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, system1, human1);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, human1, xor1);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, xor1, human2, condition2);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, xor1, human5, condition1);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, human2, xor2);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, xor2, human3, condition3);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, xor2, xor4, condition4);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, human3, xor3);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, xor3, xor4, condition5);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, xor4, human4);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, human4, human5);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, xor5, system2);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, human5, xor5);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, system2, endNode);

        processDefinitionBuilder.setName("Shortened Reference Process").setDescription("Shortened Reference Process");
    }

    /**
     * Extracts a {@link Form} object from a local file.
     * 
     * @param formName
     *            the form name
     * @param formFileName
     *            the form path
     * @return the form
     */
    private Form extractForm(String formName, String formFileName) {

        DeploymentBuilder deploymentBuilder = repoService.getDeploymentBuilder();
        UUID processArtifactID = deploymentBuilder.deployArtifactAsClasspathResource(formName, "forms/" + formFileName);
        Form form = null;
        try {
            form = new FormImpl(repoService.getProcessResource(processArtifactID));
        } catch (DefinitionNotFoundException e) {
            logger.error("The recently deployed artifact is not there. Something critical is going wrong.");
            e.printStackTrace();
        }
        return form;
    }

    //
    // /**
    // * Helper method for creating a {@link NodeParameterBuilder}. See also
    // * {@link #createParamBuilderFor(Class, Object, IncomingBehaviour, OutgoingBehaviour)}
    // *
    // * @param activityClass
    // * the activity class which the node shall be connected to
    // * @return the {@link NodeParameterBuilder} for the given parameters
    // */
    // private NodeParameterBuilder createParamBuilderFor(Class<? extends AbstractActivity> activityClass) {
    //
    // return createParamBuilderFor(activityClass, null, null, new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
    // }
    //
    // /**
    // * Helper method for creating a {@link NodeParameterBuilder}. See also
    // *
    // * @param activityClass
    // * the activity class which the node shall be connected to
    // * @param paramClass
    // * the class of the parameter
    // * @param constructorParam
    // * the parameters that should be passed on to the constructor of the activity
    // * @return the {@link NodeParameterBuilder} for the given parameters
    // * {@link #createParamBuilderFor(Class, Object, IncomingBehaviour, OutgoingBehaviour)}
    // */
    // private NodeParameterBuilder createParamBuilderFor(Class<? extends AbstractActivity> activityClass,
    // Class<? extends Object> paramClass,
    // Object constructorParam) {
    //
    // return createParamBuilderFor(activityClass, paramClass, constructorParam, new SimpleJoinBehaviour(),
    // new TakeAllSplitBehaviour());
    // }
    //
    // /**
    // * Helper method for creating a {@link NodeParameterBuilder}. There are also smaller convenient helper methods for
    // * special cases.
    // *
    // * @param activityClass
    // * the {@link Activity} class which the node shall be connected to
    // * @param paramClass
    // * class of the parameter
    // * @param constructorParam
    // * the parameters that should be passed on to the constructor of the {@link Activity}
    // * @param in
    // * the {@link IncomingBehaviour} of the node
    // * @param out
    // * the {@link OutgoingBehaviour} of the node
    // * @return the {@link NodeParameterBuilder} for the given parameters
    // */
    // private NodeParameterBuilder createParamBuilderFor(Class<? extends AbstractActivity> activityClass,
    // Class<? extends Object> paramClass,
    // Object constructorParam,
    // IncomingBehaviour in,
    // OutgoingBehaviour out) {
    //
    // NodeParameterBuilder nodeParamBuilder = new NodeParameterBuilderImpl(in, out);
    // nodeParamBuilder.setActivityBlueprintFor(activityClass);
    // if (constructorParam != null) {
    // nodeParamBuilder.addConstructorParameter(paramClass, constructorParam);
    // }
    // return nodeParamBuilder;
    // }

    @Override
    public void createPseudoHuman()
    throws ResourceNotAvailableException {

        jannik = (Participant) identityBuilder.createParticipant(JANNIK);
        tobi = (Participant) identityBuilder.createParticipant(TOBI);
        gerardo = (Participant) identityBuilder.createParticipant(GERARDO);
        jan = (Participant) identityBuilder.createParticipant(JAN);
        objectionClerk = (Role) identityBuilder.createRole(OBJECTION_CLERK);
        allowanceClerk = (Role) identityBuilder.createRole(ALLOWANCE_CLERK);
        identityBuilder.participantBelongsToRole(jannik.getID(), objectionClerk.getID())
        .participantBelongsToRole(tobi.getID(), objectionClerk.getID())
        .participantBelongsToRole(gerardo.getID(), objectionClerk.getID())
        .participantBelongsToRole(jan.getID(), allowanceClerk.getID());
    }
}
