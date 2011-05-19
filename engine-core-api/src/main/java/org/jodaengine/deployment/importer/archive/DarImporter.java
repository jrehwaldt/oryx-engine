package org.jodaengine.deployment.importer.archive;

import java.io.File;

import org.jodaengine.deployment.Deployment;

/**
 * The Interface DarImporter. It provides methods to deploy a .dar-File to the engine.
 */
public interface DarImporter {
    
    /**
     * Imports a .dar-file and creates a {@link Deployment} out of it, that contains process definitions, forms, etc.
     *
     * @param file the .dar-file
     * @return the deployment
     */
    Deployment importDarFile(File file);

}
