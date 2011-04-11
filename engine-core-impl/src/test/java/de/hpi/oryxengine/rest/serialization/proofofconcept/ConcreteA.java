package de.hpi.oryxengine.rest.serialization.proofofconcept;

import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.hpi.oryxengine.rest.provider.UUIDXmlAdapter;


/**
 * Concrete class A.
 * 
 * Tests map and uuid creation as well as inheritance.
 * 
 * @author Jan Rehwaldt
 */
@XmlRootElement
//@XmlDiscriminatorValue("concrete-a-classifier")
public class ConcreteA extends AbstractA {
    
    @XmlElement
    private String text;
    
    @XmlID
    @XmlJavaTypeAdapter(UUIDXmlAdapter.class)
    private UUID id;
    
    private Map<String, String> map;
    
    private ConcreteB ref;
    
    /**
     * Hidden jaxb constructor.
     */
    public ConcreteA() { }
    
    /**
     * Default constructor.
     * 
     * @param text a text
     * @param map a map
     */
    public ConcreteA(String text,
                     Map<String, String> map) {
        this.id = UUID.randomUUID();
        this.text = text;
        this.map = map;
        this.ref = new ConcreteB(this);
    }
    
    @Override
    public String getText() {
        return text;
    }
    
    @Override
    public Map<String, String> getMap() {
        return map;
    }

    @Override
    public UUID getId() {
        return id;
    }
    
    /**
     * Getter.
     * 
     * @return a cuircular ref
     */
    public ConcreteB getRef() {
        return ref;
    }

}
