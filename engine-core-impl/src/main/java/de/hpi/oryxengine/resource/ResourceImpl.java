package de.hpi.oryxengine.resource;

import java.util.HashMap;
import java.util.Map;

import de.hpi.oryxengine.identity.Position;
import de.hpi.oryxengine.identity.Resource;

/**
 * 
 * @author Gery
 * 
 * @param <R>
 */
public class ResourceImpl<R extends Resource<?>> implements Resource<R> {

    protected String resourceId;
    protected String resourceName;
    protected Map<String, Object> propertyTable;

    protected ResourceImpl() {

    }

    protected ResourceImpl(String id) {

        resourceId = id;
    }

    public String getId() {

        return resourceId;
    }

    /**
     * @return the current instance of the resource {@inheritDoc}
     */
    @Override
    public R setId(String id) {

        resourceId = id;
        return extractedThis();
    }

    public String getName() {

        return resourceName;
    }

    public R setName(String name) {

        resourceName = name;
        return extractedThis();
    }

    public Object getProperty(String propertyId) {

        return getPropertyTable().get(propertyId);
    }

    protected Map<String, Object> getPropertyTable() {

        if (propertyTable == null)
            propertyTable = new HashMap<String, Object>();
        return propertyTable;
    }

    public R setProperty(String propertyKey, Object propertyValue) {

        getPropertyTable().put(propertyKey, propertyValue);
        return extractedThis();
    }

    @SuppressWarnings("unchecked")
    private R extractedThis() {

        return (R) this;
    }
    
    /**
     * A Position object is equal if their id is the same.
     * 
     * @param objectToCompare - if the object is not an Position object then it is treated like any other object
     */
    @Override
    public boolean equals(Object objectToCompare) {
        
        if (!(objectToCompare instanceof Resource<?>)) {
            return super.equals(objectToCompare);
        }

        Resource<?> positionToCompare = (Resource<?>) objectToCompare;
        return this.getId().equals(positionToCompare.getId());
    }
}
