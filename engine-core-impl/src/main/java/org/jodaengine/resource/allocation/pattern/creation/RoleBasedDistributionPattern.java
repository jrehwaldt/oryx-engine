package org.jodaengine.resource.allocation.pattern.creation;

import java.util.List;

import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.allocation.pattern.push.OfferMultiplePattern;

/**
 * The Class RoleBasedDistributionPattern.
 */
public class RoleBasedDistributionPattern extends DirectDistributionPattern {

    /**
     * Convenience Constructor.
     * <br/>
     * see also {@link #RoleBasedDistributionPattern(String, String, String, AbstractResource[])}
     *
     * @param subject the subject
     * @param description the description
     * @param formID the form id
     * @param assignedResource the assigned resource
     */
    public RoleBasedDistributionPattern(String subject,
                                        String description,
                                        String formID,
                                        AbstractResource<?> assignedResource) {

        super(subject, description, formID, assignedResource);
    }

    /**
     * Default Constructor that instantiates a new role based distribution pattern.
     * <br/>
     * see also {@link AbstractCreationPattern#AbstractCreationPattern(String, String, String, AbstractResource[])}
     *
     * @param subject the subject
     * @param description the description
     * @param formID the form id
     * @param assignedResources the assigned resources
     */
    public RoleBasedDistributionPattern(String subject,
                                        String description,
                                        String formID,
                                        List<AbstractResource<?>> assignedResources) {

        super(subject, description, formID, assignedResources);
    }

    @Override
    protected void setPushPattern() {
    
        this.pushPattern = new OfferMultiplePattern();
    }
}
