package de.hpi.oryxengine.process.definition;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;

public interface TransitionBuilder {

    @Nonnull
    TransitionBuilder transitionGoesFromTo(Node source, Node destination);
    
    @Nonnull
    TransitionBuilder setCondition(Condition condition);
    
    @Nonnull
    Transition buildTransition();
}
