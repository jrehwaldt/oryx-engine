package de.hpi.oryxengine.process.instantiation;

import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.util.PatternAppendable;

/**
 * The {@link ProcessInstantiationPattern} is responsible for creating, modifying {@link AbstractProcessInstance
 * processInstances} and doing after work at the end of a instantiation.
 * 
 * The {@link ProcessInstantiationPattern} are designed to be part of a linked list of {@link instantiationPatterns}. So
 * if necessary the method {@link #createProcessInstance(AbstractProcessInstance)} should check if there is a following
 * {@link ProcessInstantiationPattern} in order to pass on the processed {@link AbstractProcessInstance processInstance}
 * .
 */
public interface ProcessInstantiationPattern extends InstantionPatternInit,
PatternAppendable<ProcessInstantiationPattern> {

    /**
     * Creates a {@link AbstractProcessInstance processInstance}. It gets the previously created
     * {@link AbstractProcessInstance processInstances} in order to modify it or to do after work. It also can create
     * tokens.
     * 
     * @param previosProcessInstance
     *            - the {@link AbstractProcessInstance processInstances} from the previous
     *            {@link ProcessInstantiationPattern patterns}.
     * 
     * @return an {@link AbstractProcessInstance}
     */
    AbstractProcessInstance createProcessInstance(AbstractProcessInstance previosProcessInstance);
}
