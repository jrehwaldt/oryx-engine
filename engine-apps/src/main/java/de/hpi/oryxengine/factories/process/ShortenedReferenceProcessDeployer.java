package de.hpi.oryxengine.factories.process;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.activity.Activity;
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

	private static final String JANNIK = "Jannik";

	private static final String TOBI = "Tobi";

	private static final String GERARDO = "Gerardo";

	private static final String JAN = "Jan";

	private static final String OBJECTION_CLERK = "Objection Clerk";

	private static final String ALLOWANCE_CLERK = "Allowance Clerk";

	private IdentityBuilder identityBuilder;

	private IdentityService identityService;

	private Node startNode;

	private Role objectionClerk;

	private Role allowanceClerk;

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

		// system task, pre-editing of objection
		Node system1 = builder.createNode(createParamBuilderFor(
				PrintingVariableActivity.class, String.class,
				"Widerspruch wird vorbearbeitet").buildNodeParameter());

		// human task for objection clerk, task is to check
		// positions of objection
		Task task = TaskFactory.createRoleTask(
				"Positionen auf Anspruch prüfen",
				"Anspruchspositionen überprüfen", objectionClerk);
		Node human1 = builder.createNode(createParamBuilderFor(
				HumanTaskActivity.class, Task.class, task,
				new SimpleJoinBehaviour(), new XORSplitBehaviour())
				.buildNodeParameter());

		// XOR Split, condition is objection existence
		Node xor1 = builder.createNode(createParamBuilderFor(
				NullActivity.class, null, null, new SimpleJoinBehaviour(),
				new XORSplitBehaviour()).buildNodeParameter());

		// human task for objection clerk, task is to check redress
		task = TaskFactory.createRoleTask("Abhilfebescheid prüfen",
				"Prüfen des Abhilfebescheids", objectionClerk);
		Node human2 = builder
				.createNode(createParamBuilderFor(HumanTaskActivity.class,
						Task.class, task).buildNodeParameter());

		// human task for objection clerk, task is to check objection
		task = TaskFactory.createRoleTask("Widerspruch prüfen",
				"Widerspruch erneut prüfen auf neue Ansprüche", objectionClerk);
		Node human3 = builder.createNode(createParamBuilderFor(
				HumanTaskActivity.class, Task.class, task,
				new SimpleJoinBehaviour(), new XORSplitBehaviour())
				.buildNodeParameter());

		// XOR Split, condition is new relevant aspects existence
		Node xor2 = builder.createNode(createParamBuilderFor(
				NullActivity.class, null, null, new SimpleJoinBehaviour(),
				new XORSplitBehaviour()).buildNodeParameter());

		// human task for objection clerk, task is to create a new report
		task = TaskFactory
				.createRoleTask("neues Gutachten erstellen",
						"Anspruchspunkte in neues Gutachten übertragen",
						objectionClerk);
		Node human4 = builder
				.createNode(createParamBuilderFor(HumanTaskActivity.class,
						Task.class, task).buildNodeParameter());

		// TODO intermediate mail event, customer answer
		// needs to be implemented and inserted here

		// XOR Split, condition is existence of objection in answer of customer
		Node xor3 = builder.createNode(createParamBuilderFor(
				NullActivity.class, null, null, new SimpleJoinBehaviour(),
				new XORSplitBehaviour()).buildNodeParameter());

		// XOR Join
		Node xor4 = builder
				.createNode(createParamBuilderFor(NullActivity.class)
						.buildNodeParameter());

		// human task for objection clerk, task is to do final work
		task = TaskFactory.createRoleTask("Nachbearbeitung",
				"abschließende Nachbearbeitung des Falls", objectionClerk);
		Node human5 = builder
				.createNode(createParamBuilderFor(HumanTaskActivity.class,
						Task.class, task).buildNodeParameter());

		// human task for allowance clerk, task is to enforce allowance
		task = TaskFactory.createRoleTask("Leistungsgewährung umsetzen",
				"Leistungsansprüche durchsetzen", allowanceClerk);
		Node human6 = builder
				.createNode(createParamBuilderFor(HumanTaskActivity.class,
						Task.class, task).buildNodeParameter());

		// final XOR Join
		Node xor5 = builder
				.createNode(createParamBuilderFor(NullActivity.class)
						.buildNodeParameter());

		// system task, close file
		Node system2 = builder.createNode(createParamBuilderFor(
				PrintingVariableActivity.class, String.class,
				"Akte wird geschlossen").buildNodeParameter());

		// end node
		Node endNode = builder.createNode(createParamBuilderFor(
				EndActivity.class).buildNodeParameter());

		// connect the nodes
		builder.createTransition(startNode, system1)
				.createTransition(system1, human1)
				.createTransition(human1, xor1)
				.createTransition(xor1, human2)
				.createTransition(xor1, human3)
				.createTransition(human2, xor5)
				.createTransition(human3, xor2)
				.createTransition(xor2, human4)
				.createTransition(xor2, xor4)
				.createTransition(human4, xor3)
				.createTransition(xor3, xor4)
				.createTransition(xor3, xor5)
				.createTransition(xor4, human5)
				.createTransition(human5, human6)
				.createTransition(human6, xor5)
				.createTransition(xor5, system2)
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
	 * {@link #createParamBuilderFor(Class, Object, IncomingBehaviour, OutgoingBehaviour)}
	 * 
	 * @param activityClass
	 *            the activity class which the node shall be connected to
	 * @param constructorParam
	 *            the parameters that should be passed on to the constructor of
	 *            the avtivity
	 * @return the {@link NodeParameterBuilder} for the given parameters
	 */
	private NodeParameterBuilder createParamBuilderFor(
			Class<? extends AbstractActivity> activityClass,
			Class<? extends Object> paramClass,
			Object constructorParam) {
		return createParamBuilderFor(activityClass, paramClass, constructorParam,
				new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
	}

	/**
	 * Helper method for creating a {@link NodeParameterBuilder}. There also
	 * smaller convenient helper methods for special cases.
	 * 
	 * @param activityClass
	 *            the {@link Activity} class which the node shall be connected
	 *            to
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
			Class<? extends Object> paramClass,
			Object constructorParam, IncomingBehaviour in, OutgoingBehaviour out) {

		NodeParameterBuilder nodeParamBuilder = new NodeParameterBuilderImpl(
				in, out);
		nodeParamBuilder.setActivityBlueprintFor(activityClass);
		if (constructorParam != null) {
			nodeParamBuilder.addConstructorParameter(
					paramClass, constructorParam);
		}
		return nodeParamBuilder;
	}

	@Override
	public void createPseudoHuman() {
		Participant jannik = (Participant) identityBuilder
				.createParticipant(JANNIK);
		Participant tobi = (Participant) identityBuilder
				.createParticipant(TOBI);
		Participant gerardo = (Participant) identityBuilder
				.createParticipant(GERARDO);
		Participant jan = (Participant) identityBuilder.createParticipant(JAN);
		objectionClerk = (Role) identityBuilder.createRole(OBJECTION_CLERK);
		allowanceClerk = (Role) identityBuilder.createRole(ALLOWANCE_CLERK);
		identityBuilder
				.participantBelongsToRole(jannik.getID(),
						objectionClerk.getID())
				.participantBelongsToRole(tobi.getID(), objectionClerk.getID())
				.participantBelongsToRole(gerardo.getID(),
						objectionClerk.getID())
				.participantBelongsToRole(jan.getID(), allowanceClerk.getID());
	}
}
