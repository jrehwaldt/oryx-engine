package de.hpi.oryxengine.deploy;

import java.util.UUID;

import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.process.definition.ProcessDefinition;

/**
 * The Interface Deployer. Use this class to deploy processes in the engine, not the repository itself. You can deploy
 * processes to a specific navigator.
 */
public interface Deployer {

    /**
     * Deploys a process: Adds it to the repository and does some registration stuff.
     * 
     * @param def
     *            the def
     */
    void deploy(ProcessDefinition def);

    /**
     * Deactivate.
     * 
     * @param id
     *            the id
     * @param nav
     *            the nav
     */
    void deactivate(UUID id, Navigator nav);

    /**
     * Delete.
     * 
     * @param id
     *            the id
     * @param nav
     *            the nav
     */
    void delete(UUID id, Navigator nav);
}
