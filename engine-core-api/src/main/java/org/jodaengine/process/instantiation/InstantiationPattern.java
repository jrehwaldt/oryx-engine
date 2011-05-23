package org.jodaengine.process.instantiation;

import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.token.BPMNToken;
import org.jodaengine.util.PatternAppendable;

/**
 * The {@link InstantiationPattern} is responsible for creating, modifying {@link AbstractProcessInstance
 * processInstances} and doing after work at the end of a instantiation.
 * 
 * The {@link InstantiationPattern} are designed to be part of a linked list of {@link instantiationPatterns}. So
 * if necessary the method {@link #createProcessInstance(AbstractProcessInstance)} should check if there is a following
 * {@link InstantiationPattern} in order to pass on the processed {@link AbstractProcessInstance processInstance} .
 */
public interface InstantiationPattern extends PatternAppendable<InstantiationPattern> {

    /**
     * Creates a {@link AbstractProcessInstance processInstance}. It gets the previously created
     *
     * @param patternContext the pattern context
     * @param previosProcessInstance - the {@link AbstractProcessInstance processInstances}
     *  from the previous {@link InstantiationPattern
     * patterns}.
     * @return an {@link AbstractProcessInstance}
     * {@link AbstractProcessInstance processInstances} in order to modify it or to do after work. It also can create
     * tokens.
     */
    AbstractProcessInstance<BPMNToken> createProcessInstance(InstantiationPatternContext patternContext,
                                                  AbstractProcessInstance<BPMNToken> previosProcessInstance);
}
