package de.hpi.oryxengine.repository;

import de.hpi.oryxengine.util.Identifiable;

/**
 * This represents a resource that is available for a {@link ProcessDefinition}. Such {@link ProcessResource}s can be
 * something like forms for UserTasks, transformation files (.xsl, .xml), property files.
 * 
 * {@link ProcessDefinition}s can access the {@link ProcessResource} through the name. The name of the
 * {@link ProcessResource}s should be unique. In case a {@link ProcessResource} is deployed twice a version number is
 * added.
 * 
 * Example: The {@link ProcessResource} with the name "vacationForm.html" is already deployed. Now the same
 * {@link ProcessResource} with the same name is deployed again. Then the name of the resource will be
 * "vacationForm.html:2" .
 * 
 * @author Gerardo Navarro Suarez
 */
public abstract class AbstractProcessArtifacts implements Identifiable {

    /**
     * Retrieves the name of the {@link ProcessResource}.
     * 
     * @return the name of the {@link ProcessResource}
     */
    public abstract String getName();
}
