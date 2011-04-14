package de.hpi.oryxengine.process.structure;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.hpi.oryxengine.activity.Activity;

/**
 * The Class ActivityBlueprintImpl. See {@see ActivityBlueprint}.
 */
public class ActivityBlueprintImpl implements ActivityBlueprint {

    private Class<? extends Activity> clazz;
    private Class<?>[] constructorSignature;
    private Object[] constructorParams;

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
        throws NoSuchMethodException, 
        InstantiationException, 
        IllegalAccessException, 
        InvocationTargetException {

        Constructor<? extends Activity> con = clazz.getConstructor(constructorSignature);

        return con.newInstance(constructorParams);
    }

}
