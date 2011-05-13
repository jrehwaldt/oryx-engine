package de.hpi.oryxengine.process.instantiation;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.navigator.NavigatorInside;
import de.hpi.oryxengine.process.definition.ProcessDefinitionInside;

/**
 * This class is only the shared foundation of classes {@link StartInstantiationPattern} and
 * {@link InstantiationPattern}.
 * 
 * Both patterns need to have an initialization method in order to setup Variables.
 * 
 */
public interface InstantionPatternInit {

    /**
     * Setups the variables for the {@link InstantionPatternInit}.
     * 
     * @param correlationManager
     *            - a {@link CorrelationManager}
     * @param navigator
     *            - a {@link NavigatorInside navigator}
     * @param processDefinition
     *            - the {@link ProcessDefinitionInside} the method belongs to
     * @return the current {@link InstantionPatternInit} which in most cases will be a {@link StartInstantiationPattern}
     *         or a {@link InstantiationPattern}
     */
    InstantionPatternInit init(CorrelationManager correlationManager,
                               NavigatorInside navigator,
                               ProcessDefinitionInside processDefinition);
}
