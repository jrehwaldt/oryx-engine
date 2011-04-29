package de.hpi.oryxengine.process.definition;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.process.structure.ActivityBlueprint;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeBuilder;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * The implementation of the {@link NodeBuilder}.
 */
public class NodeBuilderImpl implements NodeBuilder {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected Class<? extends Activity> blueprintClazz;
    private List<Class<?>> blueprintConstructorSignature;
    private List<Object> blueprintConstructorParameters;
    protected OutgoingBehaviour outgoingBehaviour;
    protected IncomingBehaviour incomingBehaviour;

    @Override
    public NodeBuilder setActivityBlueprintFor(Class<? extends Activity> bluePrintClazz) {

        this.blueprintClazz = bluePrintClazz;
        return this;
    }

    @Override
    public NodeBuilder addConstructorParameter(Class<?> parameterClazz, Object parameterInstance) {

        getBlueprintConstructorSignature().add(parameterClazz);
        getBlueprintConstructorParameters().add(parameterInstance);

        return this;
    }

    @Override
    public NodeBuilder setIncomingBehaviour(IncomingBehaviour incomingBehaviour) {

        this.incomingBehaviour = incomingBehaviour;
        return this;
    }

    @Override
    public NodeBuilder setOutgoingBehaviour(OutgoingBehaviour outgoingBehaviour) {

        this.outgoingBehaviour = outgoingBehaviour;
        return this;
    }

    @Override
    public Node buildNode() {

        return getResultNode();
    }

    protected Node getResultNode() {

        checkingNodeConstraints();

        if (getBlueprintConstructorParameters().isEmpty() & getBlueprintConstructorSignature().isEmpty()) {
            ActivityBlueprintImpl activityBlueprint = new ActivityBlueprintImpl(blueprintClazz);
            return new NodeImpl(activityBlueprint, incomingBehaviour, outgoingBehaviour);
        }

        List<Class<?>> tempList = getBlueprintConstructorSignature();
        Class<?>[] constructorSignature = (Class<?>[]) tempList.toArray(new Class<?>[tempList.size()]);
        Object[] constructorParameter = getBlueprintConstructorParameters().toArray();
        ActivityBlueprint activityBlueprint = new ActivityBlueprintImpl(blueprintClazz, constructorSignature,
            constructorParameter);

        return new NodeImpl(activityBlueprint, incomingBehaviour, outgoingBehaviour);
    }

    protected void checkingNodeConstraints() {

        if (blueprintClazz == null) {

            String errorMessage = "The ActivityClass for the ActivityBlueprint needs to be set."
                + "Perform setActivityBlueprintFor(...) before.";

            logger.error(errorMessage);
            throw new DalmatinaRuntimeException(errorMessage);
        }
    }

    protected List<Object> getBlueprintConstructorParameters() {

        if (this.blueprintConstructorParameters == null) {
            this.blueprintConstructorParameters = new ArrayList<Object>();
        }
        return blueprintConstructorParameters;
    }

    protected List<Class<?>> getBlueprintConstructorSignature() {

        if (this.blueprintConstructorSignature == null) {
            this.blueprintConstructorSignature = new ArrayList<Class<?>>();
        }
        return blueprintConstructorSignature;
    }
}
