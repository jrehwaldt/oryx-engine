package de.hpi.oryxengine.process.definition;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.process.structure.ActivityBlueprint;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * Implementation of the {@link NodeParameterBuilder}. See {@link NodeParameterBuilder here} for more Information.
 */
public class NodeParameterBuilderImpl implements NodeParameterBuilder {

    private Class<? extends Activity> blueprintClazz;
    private List<Class<?>> blueprintConstructorSignature;
    private List<Object> blueprintConstructorParameters;
    private OutgoingBehaviour outgoingBehaviour;
    private IncomingBehaviour incomingBehaviour;

    /**
     * Instantiates a new {@link NodeParameterBuilderImpl} with defined {@link IncomingBehaviour} and
     * {@link OutgoingBehaviour}.
     * 
     * @param incomingBehaviour
     *            the {@link IncomingBehaviour}
     * @param outgoingBehaviour
     *            the {@link OutgoingBehaviour}
     */
    public NodeParameterBuilderImpl(IncomingBehaviour incomingBehaviour, OutgoingBehaviour outgoingBehaviour) {

        this.incomingBehaviour = incomingBehaviour;
        this.outgoingBehaviour = outgoingBehaviour;
    }

    /** Default Constructor. */
    public NodeParameterBuilderImpl() {

    }

    @Override
    public NodeParameterBuilder setActivityBlueprintFor(Class<? extends Activity> activityClazz) {

        this.blueprintClazz = activityClazz;

        return this;
    }

    @Override
    public NodeParameterBuilder addConstructorParameter(Class<?> parameterClazz, Object parameterInstance) {

        getBlueprintConstructorSignature().add(parameterClazz);
        getBlueprintConstructorParameters().add(parameterInstance);

        return this;
    }

    @Override
    public NodeParameterBuilder setIncomingBehaviour(IncomingBehaviour behaviour) {

        this.incomingBehaviour = behaviour;

        return this;
    }

    @Override
    public NodeParameterBuilder setOutgoingBehaviour(OutgoingBehaviour behaviour) {

        this.outgoingBehaviour = behaviour;

        return this;
    }

    @Override
    public NodeParameter buildNodeParameter() {

        if (blueprintClazz == null) {
            String errorMessage = "The ActivityClass for the ActivityBlueprint needs to be set. \n"
                + "Perform setActivityBlueprintFor(...) before.";
            throw new DalmatinaRuntimeException(errorMessage);
        }

        if (getBlueprintConstructorParameters().isEmpty() & getBlueprintConstructorSignature().isEmpty()) {
            return new NodeParameterImpl(blueprintClazz, incomingBehaviour, outgoingBehaviour);
        }

        List<Class<?>> tempList = getBlueprintConstructorSignature();
        Class<?>[] constructorSignature = (Class<?>[]) tempList.toArray(new Class<?>[tempList.size()]);
        Object[] constructorParameter = getBlueprintConstructorParameters().toArray();
        ActivityBlueprint activityBlueprint = new ActivityBlueprintImpl(blueprintClazz, constructorSignature,
            constructorParameter);

        return new NodeParameterImpl(activityBlueprint, incomingBehaviour, outgoingBehaviour);
    }

    @Override
    public NodeParameter buildNodeParameterAndClear() {

        NodeParameter nodeParameterToReturn = buildNodeParameter();

        // Cleanup
        setActivityBlueprintFor(null);
        getBlueprintConstructorSignature().clear();
        getBlueprintConstructorParameters().clear();

        return nodeParameterToReturn;
    }

    private List<Object> getBlueprintConstructorParameters() {

        if (this.blueprintConstructorParameters == null) {
            this.blueprintConstructorParameters = new ArrayList<Object>();
        }
        return blueprintConstructorParameters;
    }

    private List<Class<?>> getBlueprintConstructorSignature() {

        if (this.blueprintConstructorSignature == null) {
            this.blueprintConstructorSignature = new ArrayList<Class<?>>();
        }
        return blueprintConstructorSignature;
    }
}
