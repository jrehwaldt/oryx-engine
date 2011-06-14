package org.jodaengine.process.instantiation;

import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.util.PatternAppendable;

/**
 * The {@link ProcessInstantiationPattern} is responsible for creating, modifying {@link AbstractProcessInstance
 * processInstances} and doing after work at the end of a instantiation.
 * 
 * The {@link ProcessInstantiationPattern} is designed to be part of a linked list of {@link ProcessInstantiationPattern
 * instantiationPatterns}. So if necessary the method {@link #createProcessInstance(AbstractProcessInstance)} should
 * check if there is a following {@link ProcessInstantiationPattern} in order to pass on the processed
 * {@link AbstractProcessInstance processInstance}.
 */
public interface ProcessInstantiationPattern extends PatternAppendable<ProcessInstantiationPattern> {

    /**
     * Creates a {@link AbstractProcessInstance processInstance}. It gets the previously created
     *
     * @param patternContext the pattern context
     * @param previosProcessInstance - the {@link AbstractProcessInstance processInstances} from the previous {@link ProcessInstantiationPattern
     * patterns}.
     * @return an {@link AbstractProcessInstance}
     * {@link AbstractProcessInstance processInstances} in order to modify it or to do after work. It also can create
     * tokens.
     */
    AbstractProcessInstance createProcessInstance(InstantiationPatternContext patternContext,
                                                  AbstractProcessInstance previosProcessInstance);
}
