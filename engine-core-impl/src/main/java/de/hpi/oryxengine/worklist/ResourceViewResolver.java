package de.hpi.oryxengine.worklist;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.resource.OrganizationUnit;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Position;
import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.resource.ResourceType;
import de.hpi.oryxengine.resource.Role;

/**
 * Knows how to resolve the resources that might get a task.
 */
// TODO Name ändern
public final class ResourceViewResolver {

    public List<Resource<?>> resolveResource(Resource<?> resource) {

        // switch (resource.getType()) {
        // case Resources:
        //
        // break;
        //
        // default:
        // break;
        // }
        //
        return null;
    }

    /**
     *  Expect for itself, it extracts all {@link Resource}s that might contain WorklistItems
     *  that can be seen by the given {@link Resource}. 
     * 
     * @param resource - {@link Resource} where the all other {@link Resource}s needs to be found
     * @return a list of {@link Resource}s the might contain a worklist.
     *         The result does not contain the given {@link Resource}. 
     */
    public static List<Resource<?>> extractResourcesFor(Resource<?> resource) {

        if (resource.getType() == ResourceType.PARTICIPANT) {

            return extractResourcesForParticipant((Participant) resource);

        } else if (resource.getType() == ResourceType.ORGANIZATION_UNIT) {

        
        } else if (resource.getType() == ResourceType.ROLE) {
            
            return extractResourcesForRole((Role) resource); 

        
        } else if (resource.getType() == ResourceType.POSITION) {

        }
        
        return null;
    }

    private static List<Resource<?>> extractResourcesForOrganizationUnit(OrganizationUnit organizationUnit) {

        List<Resource<?>> resultResourceList = new ArrayList<Resource<?>>();
        return resultResourceList;
    }

    private static List<Resource<?>> extractResourcesForRole(Role role) {
        
        List<Resource<?>> resultResourceList = new ArrayList<Resource<?>>();
        return resultResourceList;
    }

    private static List<Resource<?>> extractResourcesForPosition(Position position) {

        List<Resource<?>> resultResourceList = new ArrayList<Resource<?>>();
        return resultResourceList;
    }

    private static List<Resource<?>> extractResourcesForParticipant(Participant participant) {

        List<Resource<?>> resultResourceList = new ArrayList<Resource<?>>();

        resultResourceList.addAll(participant.getMyRoles());

        for (Position position : participant.getMyPositions()) {
            resultResourceList.add(position);
            resultResourceList.add(position.belongstoOrganization());
        }

        return resultResourceList;
    }
}
