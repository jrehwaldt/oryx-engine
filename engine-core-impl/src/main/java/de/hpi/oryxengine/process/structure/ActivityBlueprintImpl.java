package de.hpi.oryxengine.process.structure;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.hpi.oryxengine.activity.Activity;

/**
 * The Class ActivityBlueprintImpl. See {@see ActivityBlueprint}.
 */
public class ActivityBlueprintImpl implements ActivityBlueprint {

    // these variables should never be altered after instantiation
    private final Class<? extends Activity> clazz;
    private final Class<?>[] constructorSignature;
    private final Object[] constructorParams;

    /**
     * Instantiates a new activity blueprint impl.
     * 
     * @param clazz
     *            the activity clazz to instantiate.
     * @param constructorSignature
     *            the signature of the constructor to use.
     * @param constructorParams
     *            the constructor params
     */
    public ActivityBlueprintImpl(Class<? extends Activity> clazz,
                                 Class<?>[] constructorSignature,
                                 Object[] constructorParams) {

        this.clazz = clazz;
        this.constructorSignature = constructorSignature;
        this.constructorParams = constructorParams;

    }

    /**
     * This is a convenience constructor to allow easy instantiation of the blueprint for activity's that you want to
     * instantiate with the default constructor.
     * 
     * @param clazz
     *            the clazz
     */
    public ActivityBlueprintImpl(Class<? extends Activity> clazz) {

        this.clazz = clazz;
        Class<?>[] sig = {};
        this.constructorSignature = sig;
        Object[] params = {};
        this.constructorParams = params;
    }

    @Override
    public Class<? extends Activity> getActivityClass() {

        return clazz;
    }

    @Override
    public Class<?>[] getConstructorSignature() {

        return constructorSignature;
    }

    @Override
    public Object[] getParameters() {

        return constructorParams;
    }

    @Override
    public Activity instantiate()
    throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        Constructor<? extends Activity> con = clazz.getConstructor(constructorSignature);

        return con.newInstance(constructorParams);
    }

}
