package org.jodaengine.process.definition;

import java.io.InputStream;
import java.util.UUID;

import org.jodaengine.util.Identifiable;

/**
 * This represents a resource that is available for a {@link ProcessDefinition}. Such {@link ProcessResource}s can be
 * something like forms for UserTasks, transformation files (.xsl, .xml), property files.
 * 
 * {@link ProcessDefinition}s can access the {@link ProcessResource} through the name. The name of the
 * {@link ProcessResource}s should be unique. In case a {@link ProcessResource} is deployed twice an exception is
 * thrown.
 * 
 * @author Gerardo Navarro Suarez
 */
public abstract class AbstractProcessArtifact implements Identifiable<String> {

    /**
     * Retrieves the content of the {@link ProcessArtifact} as {@link InputStream}.
     * 
     * @return the {@link InputStream} contianing the content of the {@link ProcessArtifact}
     */
    public abstract InputStream getInputStream();
}
