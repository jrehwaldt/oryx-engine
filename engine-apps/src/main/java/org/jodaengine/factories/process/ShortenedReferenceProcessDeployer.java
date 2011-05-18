package org.jodaengine.factories.process;

import java.util.UUID;

import org.jodaengine.allocation.Form;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.structure.Condition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.condition.JuelExpressionCondition;
import org.jodaengine.resource.Participant;
import org.jodaengine.resource.Role;
import org.jodaengine.resource.allocation.CreationPatternBuilder;
import org.jodaengine.resource.allocation.CreationPatternBuilderImpl;
import org.jodaengine.resource.allocation.FormImpl;
import org.jodaengine.resource.allocation.pattern.OfferMultiplePattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        human1 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder,
            builder.buildConcreteResourcePattern(), new OfferMultiplePattern());

        // XOR Split, condition is objection existence
        xor1 = BpmnNodeFactory.createBpmnXorGatewayNode(processDefinitionBuilder);
        Condition condition1 = new JuelExpressionCondition("${widerspruch  == \"stattgegeben\"}");
        Condition condition2 = new JuelExpressionCondition("${widerspruch  == \"abgelehnt\"}");

        // human task for objection clerk, task is to check objection
        form = extractForm("form2", "checkForNewClaims.html");
        builder.setItemDescription("Widerspruch erneut prüfen auf neue Ansprüche").setItemSubject("Widerspruch prüfen")
        .setItemForm(form);
        human2 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder,
            builder.buildConcreteResourcePattern(), new OfferMultiplePattern());

        // XOR Split, condition is new relevant aspects existence
        xor2 = BpmnNodeFactory.createBpmnXorGatewayNode(processDefinitionBuilder);
        Condition condition3 = new JuelExpressionCondition("${neueAspekte  == \"ja\"}");
        Condition condition4 = new JuelExpressionCondition("${neueAspekte  == \"nein\"}");

        // human task for objection clerk, task is to create a new report
        form = extractForm("form3", "createReport.html");
        builder.setItemDescription("Anspruchspunkte in neues Gutachten übertragen")
        .setItemSubject("neues Gutachten erstellen").setItemForm(form);
        human3 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder,
            builder.buildConcreteResourcePattern(), new OfferMultiplePattern());

        // intermediate mail event, customer answer
        // needs to be implemented and inserted here

        // XOR Split, condition is existence of objection in answer of customer
        xor3 = BpmnNodeFactory.createBpmnXorGatewayNode(processDefinitionBuilder);
        Condition condition5 = new JuelExpressionCondition("${aufrecht  == \"ja\"}");
        Condition condition6 = new JuelExpressionCondition("${aufrecht  == \"nein\"}");

        // XOR Join
        xor4 = BpmnNodeFactory.createBpmnXorGatewayNode(processDefinitionBuilder);

        // human task for objection clerk, task is to do final work
        form = extractForm("form4", "postEditingClaim.html");
        builder.setItemDescription("abschließende Nachbearbeitung des Falls").setItemSubject("Nachbearbeitung")
        .setItemForm(form);
        human4 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder,
            builder.buildConcreteResourcePattern(), new OfferMultiplePattern());

        // human task for allowance clerk, task is to enforce allowance
        form = extractForm("form5", "enforceAllowance.html");
        builder.flushAssignedResources().setItemDescription("Leistungsansprüche durchsetzen")
        .setItemSubject("Leistungsgewährung umsetzen").setItemForm(form).addResourceAssignedToItem(allowanceClerk);
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
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, xor3, xor5, condition6);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, xor4, human4);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, human4, human5);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, xor5, system2);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, human5, xor5);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, system2, endNode);

        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(processDefinitionBuilder);

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
