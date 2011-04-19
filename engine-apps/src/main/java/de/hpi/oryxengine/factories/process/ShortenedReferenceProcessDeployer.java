package de.hpi.oryxengine.factories.process;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.HumanTaskActivity;
import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.activity.impl.PrintingVariableActivity;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.factories.worklist.TaskFactory;
import de.hpi.oryxengine.process.definition.NodeParameterBuilder;
import de.hpi.oryxengine.process.definition.NodeParameterBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Role;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.XORSplitBehaviour;

/**
 * The Class ShortenedReferenceProcessDeployer. This is the implementation of
 * the shortened version of the AOK reference process.
 */
public class ShortenedReferenceProcessDeployer extends AbstractProcessDeployer {

	// configuration constants
	private static final String JANNIK = "Jannik";
	private static final String TOBI = "Tobi";
	private static final String GERARDO = "Gerardo";
	private static final String JAN = "Jan";
	private static final String OBJECTION_CLERK = "Objection Clerk";
	private static final String ALLOWANCE_CLERK = "Allowance Clerk";
	
	private IdentityBuilder identityBuilder;

	private IdentityService identityService;

	// Nodes
	private Node startNode;
	private Node system1;
	private Node system2;
	private Node human1;
	private Node human2;
	private Node human3;
	private Node human4;
	private Node human5;
	private Node human6;
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
	 * Gets the sixth human task.
	 * 
	 * @return the human6
	 */
	public Node getHuman6() {
		return human6;
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

	/**
	 * Default constructor.
	 */
	public ShortenedReferenceProcessDeployer() {
		identityService = ServiceFactory.getIdentityService();
		builder = new ProcessBuilderImpl();
		identityBuilder = identityService.getIdentityBuilder();
	}

	@Override
	public void initializeNodes() {

		// start node, blank
		startNode = builder.createStartNode(createParamBuilderFor(
				NullActivity.class).buildNodeParameter());

		system1 = builder.createNode(createParamBuilderFor(
				PrintingVariableActivity.class, String.class,
				"Widerspruch wird vorbearbeitet").buildNodeParameter());

		// human task for objection clerk, task is to check
		// positions of objection
		Task task = TaskFactory.createRoleTask(
				"Positionen auf Anspruch prüfen",
				"Anspruchspositionen überprüfen", objectionClerk);
		human1 = builder.createNode(createParamBuilderFor(
				HumanTaskActivity.class, Task.class, task,
				new SimpleJoinBehaviour(), new XORSplitBehaviour())
				.buildNodeParameter());

		// XOR Split, condition is objection existence
		xor1 = builder.createNode(createParamBuilderFor(NullActivity.class,
				null, null, new SimpleJoinBehaviour(), new XORSplitBehaviour())
				.buildNodeParameter());

		// human task for objection clerk, task is to check redress
		task = TaskFactory.createRoleTask("Abhilfebescheid prüfen",
				"Prüfen des Abhilfebescheids", objectionClerk);
		human2 = builder
				.createNode(createParamBuilderFor(HumanTaskActivity.class,
						Task.class, task).buildNodeParameter());

		// human task for objection clerk, task is to check objection
		task = TaskFactory.createRoleTask("Widerspruch prüfen",
				"Widerspruch erneut prüfen auf neue Ansprüche", objectionClerk);
		human3 = builder.createNode(createParamBuilderFor(
				HumanTaskActivity.class, Task.class, task,
				new SimpleJoinBehaviour(), new XORSplitBehaviour())
				.buildNodeParameter());

		// XOR Split, condition is new relevant aspects existence
		xor2 = builder.createNode(createParamBuilderFor(NullActivity.class,
				null, null, new SimpleJoinBehaviour(), new XORSplitBehaviour())
				.buildNodeParameter());

		// human task for objection clerk, task is to create a new report
		task = TaskFactory
				.createRoleTask("neues Gutachten erstellen",
						"Anspruchspunkte in neues Gutachten übertragen",
						objectionClerk);
		human4 = builder
				.createNode(createParamBuilderFor(HumanTaskActivity.class,
						Task.class, task).buildNodeParameter());

		// TODO intermediate mail event, customer answer
		// needs to be implemented and inserted here

		// XOR Split, condition is existence of objection in answer of customer
		xor3 = builder.createNode(createParamBuilderFor(NullActivity.class,
				null, null, new SimpleJoinBehaviour(), new XORSplitBehaviour())
				.buildNodeParameter());

		// XOR Join
		xor4 = builder.createNode(createParamBuilderFor(NullActivity.class)
				.buildNodeParameter());

		// human task for objection clerk, task is to do final work
		task = TaskFactory.createRoleTask("Nachbearbeitung",
				"abschließende Nachbearbeitung des Falls", objectionClerk);
		human5 = builder
				.createNode(createParamBuilderFor(HumanTaskActivity.class,
						Task.class, task).buildNodeParameter());

		// human task for allowance clerk, task is to enforce allowance
		task = TaskFactory.createRoleTask("Leistungsgewährung umsetzen",
				"Leistungsansprüche durchsetzen", allowanceClerk);
		human6 = builder
				.createNode(createParamBuilderFor(HumanTaskActivity.class,
						Task.class, task).buildNodeParameter());

		// final XOR Join
		xor5 = builder.createNode(createParamBuilderFor(NullActivity.class)
				.buildNodeParameter());

		// system task, close file
		system2 = builder.createNode(createParamBuilderFor(
				PrintingVariableActivity.class, String.class,
				"Akte wird geschlossen").buildNodeParameter());

		// end node
		endNode = builder.createNode(createParamBuilderFor(EndActivity.class)
				.buildNodeParameter());

		// connect the nodes
		builder.createTransition(startNode, system1)
				.createTransition(system1, human1)
				.createTransition(human1, xor1).createTransition(xor1, human2)
				.createTransition(xor1, human3).createTransition(human2, xor5)
				.createTransition(human3, xor2).createTransition(xor2, human4)
				.createTransition(xor2, xor4).createTransition(human4, xor3)
				.createTransition(xor3, xor4).createTransition(xor3, xor5)
				.createTransition(xor4, human5)
				.createTransition(human5, human6)
				.createTransition(human6, xor5).createTransition(xor5, system2)
				.createTransition(system2, endNode);
	}

	/**
	 * Helper method for creating a {@link NodeParameterBuilder}. See also
	 * {@link #createParamBuilderFor(Class, Object, IncomingBehaviour, OutgoingBehaviour)}
	 * 
	 * @param activityClass
	 *            the activity class which the node shall be connected to
	 * @return the {@link NodeParameterBuilder} for the given parameters
	 */
	private NodeParameterBuilder createParamBuilderFor(
			Class<? extends AbstractActivity> activityClass) {
		return createParamBuilderFor(activityClass, null, null,
				new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
	}

	/**
	 * Helper method for creating a {@link NodeParameterBuilder}. See also
	 * 
	 * @param activityClass
	 *            the activity class which the node shall be connected to
	 * @param paramClass
	 *            the class of the parameter
	 * @param constructorParam
	 *            the parameters that should be passed on to the constructor of
	 *            the activity
	 * @return the {@link NodeParameterBuilder} for the given parameters
	 *         {@link #createParamBuilderFor(Class, Object, IncomingBehaviour, OutgoingBehaviour)}
	 */
	private NodeParameterBuilder createParamBuilderFor(
			Class<? extends AbstractActivity> activityClass,
			Class<? extends Object> paramClass, Object constructorParam) {
		return createParamBuilderFor(activityClass, paramClass,
				constructorParam, new SimpleJoinBehaviour(),
				new TakeAllSplitBehaviour());
	}

	/**
	 * Helper method for creating a {@link NodeParameterBuilder}. There are also
	 * smaller convenient helper methods for special cases.
	 * 
	 * @param activityClass
	 *            the {@link Activity} class which the node shall be connected
	 *            to
	 * @param paramClass
	 *            class of the parameter
	 * @param constructorParam
	 *            the parameters that should be passed on to the constructor of
	 *            the {@link Activity}
	 * @param in
	 *            the {@link IncomingBehaviour} of the node
	 * @param out
	 *            the {@link OutgoingBehaviour} of the node
	 * @return the {@link NodeParameterBuilder} for the given parameters
	 */
	private NodeParameterBuilder createParamBuilderFor(
			Class<? extends AbstractActivity> activityClass,
			Class<? extends Object> paramClass, Object constructorParam,
			IncomingBehaviour in, OutgoingBehaviour out) {

		NodeParameterBuilder nodeParamBuilder = new NodeParameterBuilderImpl(
				in, out);
		nodeParamBuilder.setActivityBlueprintFor(activityClass);
		if (constructorParam != null) {
			nodeParamBuilder.addConstructorParameter(paramClass,
					constructorParam);
		}
		return nodeParamBuilder;
	}

	@Override
	public void createPseudoHuman() {
		jannik = (Participant) identityBuilder
				.createParticipant(JANNIK);
		tobi = (Participant) identityBuilder
				.createParticipant(TOBI);
		gerardo = (Participant) identityBuilder
				.createParticipant(GERARDO);
		jan = (Participant) identityBuilder
				.createParticipant(JAN);
		objectionClerk = (Role) identityBuilder.createRole(OBJECTION_CLERK);
		allowanceClerk = (Role) identityBuilder.createRole(ALLOWANCE_CLERK);
		identityBuilder
				.participantBelongsToRole(jannik.getID(),
						objectionClerk.getID())
				.participantBelongsToRole(tobi.getID(), 
						objectionClerk.getID())
				.participantBelongsToRole(gerardo.getID(),
						objectionClerk.getID())
				.participantBelongsToRole(jan.getID(), 
						allowanceClerk.getID());
	}
}
