package org.jodaengine.factory.worklist;

import org.jodaengine.ServiceFactory;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.factory.resource.ParticipantFactory;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.AbstractRole;
import org.jodaengine.resource.IdentityBuilder;
import org.jodaengine.resource.allocation.CreationPattern;
import org.jodaengine.resource.allocation.pattern.creation.DirectDistributionPattern;
import org.jodaengine.resource.allocation.pattern.creation.RoleBasedDistributionPattern;

/**
 * Little factory for creating Resources. A short cut for the implementation.
 */
public final class CreationPatternFactory {

    public static final String SIMPLE_TASK_SUBJECT = "Subject";
    public static final String SIMPLE_TASK_DESCRIPTION = "Description";

    /**
     * Private Constructor because the CheckStyle want me to do that. Gerardo do what told. Gerardo intelligent. Gerardo
     * checkstyle also want you to comment methods. Gerardo better do that.
     */
    private CreationPatternFactory() {

    }

    /**
     * Creates a new Task object for a given Participant.
     * 
     * @param r
     *            the resource
     * @return the task
     */
    public static CreationPattern createDirectDistributionPattern(AbstractResource<?> r) {

        CreationPattern pattern = new DirectDistributionPattern(SIMPLE_TASK_SUBJECT, SIMPLE_TASK_DESCRIPTION,
            null, r);
        return pattern;
    }

    /**
     * Creates a Task that is offered to a single role that two participants are assigned to.
     * 
     * @return the task
     * @throws ResourceNotAvailableException
     *             if the resource is not available
     */
    public static CreationPattern createRoleBasedDistribution()
    throws ResourceNotAvailableException {

        // The organization structure is already prepared in the factory
        // There is role containing Gerardo and Jannik
        AbstractParticipant gerardo = ParticipantFactory.createGerardo();
        AbstractParticipant jannik = ParticipantFactory.createJannik();

        IdentityBuilder identityBuilder = ServiceFactory.getIdentityService().getIdentityBuilder();
        AbstractRole hamburgGuysRole = identityBuilder.createRole("hamburgGuys");

        // look out all these methods are are called on the identity builder (therefore the format)
        identityBuilder.participantBelongsToRole(jannik.getID(), hamburgGuysRole.getID()).participantBelongsToRole(
            gerardo.getID(), hamburgGuysRole.getID());

        CreationPattern pattern = new RoleBasedDistributionPattern("Clean the office.", "It is very dirty.",
            null, hamburgGuysRole);

        return pattern;
    }

}
