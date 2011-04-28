package de.hpi.oryxengine.process.definition;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

public interface NodeBuilder {

    @Nonnull
    NodeBuilder setActivityBlueprintFor(Class<? extends Activity> clazz);
    
    @Nonnull
    NodeBuilder addConstructorParameter(Class<?> parameterClazz, Object parameterInstance);

    @Nonnull
    NodeBuilder setIncomingBehaviour(IncomingBehaviour behaviour);

    @Nonnull
    NodeBuilder setOutgoingBehaviour(OutgoingBehaviour behaviour);
    
    @Nonnull
    Node buildNode();
}
