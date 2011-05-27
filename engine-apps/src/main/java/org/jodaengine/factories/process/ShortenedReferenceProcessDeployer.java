package org.jodaengine.factories.process;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.structure.Condition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.condition.JuelExpressionCondition;
import org.jodaengine.resource.Participant;
import org.jodaengine.resource.Role;
import org.jodaengine.resource.allocation.AbstractForm;
import org.jodaengine.resource.allocation.CreationPatternBuilder;
import org.jodaengine.resource.allocation.CreationPatternBuilderImpl;
import org.jodaengine.resource.allocation.FormImpl;
import org.jodaengine.resource.allocation.pattern.creation.RoleBasedDistributionPattern;
import org.jodaengine.util.io.ClassPathResourceStreamSource;

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
     * Gets the allowance clerk.
     * 
     * @return the allowance clerk
     */
    public Role getAllowanceClerk() {

        return allowanceClerk;
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
    public void initializeNodes()
    throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        // start node, blank
        startNode = BpmnNodeFactory.createBpmnStartEventNode(processDefinitionBuilder);

        system1 = BpmnCustomNodeFactory.createBpmnPrintingVariableNode(processDefinitionBuilder,
            "Widerspruch wird vorbearbeitet");

        // human task for objection clerk, task is to check
        // positions of objection
        CreationPatternBuilder builder = new CreationPatternBuilderImpl();
        builder.flushAssignedResources().setItemDescription("Anspruchspositionen überprüfen")
        .setItemSubject("Positionen auf Anspruch prüfen").setItemFormID("form1")
        .addResourceAssignedToItem(objectionClerk);
        human1 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder,
            builder.buildCreationPattern(RoleBasedDistributionPattern.class));

        // XOR Split, condition is objection existence
        xor1 = BpmnNodeFactory.createBpmnXorGatewayNode(processDefinitionBuilder);

        Condition condition1 = new JuelExpressionCondition("${widerspruch == \"stattgegeben\"}");
        Condition condition2 = new JuelExpressionCondition("${widerspruch == \"abgelehnt\"}");

        // human task for objection clerk, task is to check objection
        builder.setItemDescription("Widerspruch erneut prüfen auf neue Ansprüche").setItemSubject("Widerspruch prüfen")
        .setItemFormID("form2");
        human2 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder,
            builder.buildCreationPattern(RoleBasedDistributionPattern.class));

        // XOR Split, condition is new relevant aspects existence
        xor2 = BpmnNodeFactory.createBpmnXorGatewayNode(processDefinitionBuilder);
        Condition condition3 = new JuelExpressionCondition("${neueAspekte == \"ja\"}");
        Condition condition4 = new JuelExpressionCondition("${neueAspekte == \"nein\"}");

        // human task for objection clerk, task is to create a new report
        builder.setItemDescription("Anspruchspunkte in neues Gutachten übertragen")
        .setItemSubject("neues Gutachten erstellen").setItemFormID("form3");
        human3 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder,
            builder.buildCreationPattern(RoleBasedDistributionPattern.class));

        // intermediate mail event, customer answer
        // needs to be implemented and inserted here

        // XOR Split, condition is existence of objection in answer of customer
        xor3 = BpmnNodeFactory.createBpmnXorGatewayNode(processDefinitionBuilder);

        Condition condition5 = new JuelExpressionCondition("${aufrecht == \"ja\"}");
        Condition condition6 = new JuelExpressionCondition("${aufrecht  == \"nein\"}");

        // XOR Join
        xor4 = BpmnNodeFactory.createBpmnXorGatewayNode(processDefinitionBuilder);

        // human task for objection clerk, task is to do final work
        builder.setItemDescription("abschließende Nachbearbeitung des Falls").setItemSubject("Nachbearbeitung")
        .setItemFormID("form4");
        human4 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder,
            builder.buildCreationPattern(RoleBasedDistributionPattern.class));

        // human task for allowance clerk, task is to enforce allowance
        builder.flushAssignedResources().setItemDescription("Leistungsansprüche durchsetzen")
        .setItemSubject("Leistungsgewährung umsetzen").setItemFormID("form5").addResourceAssignedToItem(allowanceClerk);
        // task = createRoleTask("Leistungsgewährung umsetzen", "Leistungsansprüche durchsetzen", form, allowanceClerk);
        human5 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder,
            builder.buildCreationPattern(RoleBasedDistributionPattern.class));

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

    // /**
    // * Extracts a {@link Form} object from a local file.
    // *
    // * @param formName
    // * the form name
    // * @param formFileName
    // * the form path
    // * @return the form
    // */
    // private AbstractProcessArtifact (String formName, String formFileName) {
    //
    // DeploymentBuilder deploymentBuilder = repoService.getDeploymentBuilder();
    // deploymentBuilder.addClasspathResourceArtifact(formName, "forms/" + formFileName);
    // Form form = null;
    // try {
    // form = new FormImpl(repoService.getProcessArtifact(formName));
    // } catch (ProcessArtifactNotFoundException e) {
    // logger.error("The recently deployed artifact is not there. Something critical is going wrong.");
    // e.printStackTrace();
    // }
    // return form;
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

    @Override
    public Set<AbstractForm> getFormsToDeploy() {

        AbstractForm form1 = createClassPathForm("form1", "forms/claimPoints.html");
        AbstractForm form2 = createClassPathForm("form2", "forms/checkForNewClaims.html");
        AbstractForm form3 = createClassPathForm("form3", "forms/createReport.html");
        AbstractForm form4 = createClassPathForm("form4", "forms/postEditingClaim.html");
        AbstractForm form5 = createClassPathForm("form5", "forms/enforceAllowance.html");

        Set<AbstractForm> artifacts = new HashSet<AbstractForm>();
        artifacts.add(form1);
        artifacts.add(form2);
        artifacts.add(form3);
        artifacts.add(form4);
        artifacts.add(form5);
        return artifacts;
    }

    /**
     * Creates a process artifact from a classpath resource.
     * 
     * @param name
     *            the name
     * @param fileName
     *            the file name
     * @return the abstract process artifact
     */
    private AbstractForm createClassPathForm(String name, String fileName) {

        ClassPathResourceStreamSource source = new ClassPathResourceStreamSource(fileName);
        return new FormImpl(name, source);
    }
}
