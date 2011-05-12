package de.hpi.oryxengine.factory.worklist;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.allocation.CreationPattern;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.factory.resource.ParticipantFactory;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.AbstractRole;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.allocation.pattern.DirectDistributionPattern;
import de.hpi.oryxengine.resource.allocation.pattern.RoleDistributionPattern;

/**
 * Little factory for creating Resources. A short cut for the implementation.
 */
public final class CreationPatternFactory {

    public static final String SIMPLE_TASK_SUBJECT = "Get Gerardo a cup of coffee!";
    public static final String SIMPLE_TASK_DESCRIPTION = "You know what I mean.";

    /**
     * Private Constructor because the CheckStyle want me to do that. Gerardo do what told. Gerardo intelligent. Gerardo
     * checkstyle also want you to comment methods. Gerardo better do that.
     */
    private CreationPatternFactory() {

    }

    /**
     * Creates a new Task object where Jannik shall get coffee for Gerardo. Note that the Participant who shall complete
     * this task (Jannik) is also created in the course of this method(side effect).
     * 
     * @return the task
     */
    public static CreationPattern createJannikServesGerardoCreator() {

        // creates the participant Jannik
        AbstractResource<?> resource = ParticipantFactory.createJannik();

        return createParticipantTask(resource);
    }

    /**
     * Creates a new Task object for a given Participant.
     * 
     * @param r
     *            the resource
     * @return the task
     */
    public static CreationPattern createParticipantTask(AbstractResource<?> r) {

        
        CreationPattern pattern = new DirectDistributionPattern(SIMPLE_TASK_SUBJECT, SIMPLE_TASK_DESCRIPTION, null, r);
        return pattern;
    }

    /**
     * Creates a Task that is offered to a single role that two participants are assigned to.
     * 
     * @return the task
     * @throws ResourceNotAvailableException 
     */
    public static CreationPattern createRoleTask() throws ResourceNotAvailableException {

        // The organization structure is already prepared in the factory
        // There is role containing Gerardo and Jannik
        AbstractParticipant gerardo = ParticipantFactory.createGerardo();
        AbstractParticipant jannik = ParticipantFactory.createJannik();

        IdentityBuilder identityBuilder = ServiceFactory.getIdentityService().getIdentityBuilder();
        AbstractRole hamburgGuysRole = identityBuilder.createRole("hamburgGuys");

        // look out all these methods are are called on the identity builder (therefore the format)
        identityBuilder.participantBelongsToRole(jannik.getID(), hamburgGuysRole.getID()).participantBelongsToRole(
            gerardo.getID(), hamburgGuysRole.getID());

        CreationPattern pattern = new RoleDistributionPattern("Clean the office.", "It is very dirty.", null, hamburgGuysRole);

        return pattern;
    }

}
