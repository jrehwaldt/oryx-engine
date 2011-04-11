package de.hpi.oryxengine.rest.serialization.proofofconcept;

import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Abstract class A.
 * 
 * @author Jan Rehwaldt
 */
@XmlRootElement
//@XmlDiscriminatorNode("@classifier")
public abstract class AbstractA {

    /**
     * Return an id.
     * 
     * @return an id
     */
    @XmlTransient
    public abstract UUID getId();
    
    /**
     * Return a text.
     * 
     * @return a text
     */
    @XmlTransient
    public abstract String getText();
    
    /**
     * Return a map.
     * 
     * @return a map
     */
    @XmlTransient
    public abstract Map<String, String> getMap();
}
