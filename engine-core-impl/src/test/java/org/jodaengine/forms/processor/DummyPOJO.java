package org.jodaengine.forms.processor;

/**
 * The Class DummyPOJO provides a getter and setter for a string property and is used for testing.
 */
public class DummyPOJO {

    private String value;
    
    /**
     * The setter.
     *
     * @param value the new value
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    /**
     * The getter.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
