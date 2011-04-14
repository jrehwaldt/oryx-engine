package de.hpi.oryxengine.rest.serialization.proofofconcept;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.rest.serialization.SerializationToJsonTest;

import junit.framework.Assert;

/**
 * Proof of concept tests for abstract classes serialization to XML.
 * 
 * @author Jan Rehwaldt
 */
public class ConceptTest {
    
    private AbstractA abstractA = null;
    private ConcreteA concreteA = null;
    
    private Map<String, String> map = null;
    
    private JAXBContext context = null;
    private Marshaller marshaller = null;
    private Unmarshaller unmarshaller = null;
    
    /**
     * Setup.
     * 
     * @throws JAXBException initialization fails
     */
    @BeforeClass
    public void setUp() throws JAXBException {
        
        this.map = new HashMap<String, String>();
        this.map.put("Harry", "ist doof.");
        
        this.abstractA = new ConcreteA("ABSTRACT", this.map);
        this.concreteA = new ConcreteA("CONCRETE", this.map);
        
        this.context = JAXBContext.newInstance(AbstractA.class, ConcreteA.class, UUID.class);
        
        this.marshaller = context.createMarshaller();
        this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        
        this.unmarshaller = context.createUnmarshaller();
    }
    
    
//    /**
//     * Tests the marshalling with concrete context.
//     * 
//     * @throws JAXBException test fails
//     * @throws IOException test fails
//     */
//    @Test
//    public void testConcreteMarshalling() throws JAXBException, IOException {
//        
//        JAXBContext context = JAXBContext.newInstance(ConcreteA.class);
//        Marshaller marshaller = context.createMarshaller();
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//        marshaller.marshal(this.abstractA, new FileWriter(SerializationToXmlTest.TMP_PATH + "co_AbstractA.xml"));
//        marshaller.marshal(this.concreteA, new FileWriter(SerializationToXmlTest.TMP_PATH + "co_ConcreteA.xml"));
//        
//    }
//    
//    /**
//     * Tests the unmarshalling with concrete context.
//     * 
//     * @throws JAXBException test fails
//     */
//    @Test(dependsOnMethods = "testConcreteMarshalling")
//    public void testConcreteUnmarshalling() throws JAXBException {
//        JAXBContext context = JAXBContext.newInstance(ConcreteA.class);
//        Unmarshaller unmarshaller = context.createUnmarshaller();
//
//        Object test = unmarshaller.unmarshal(
//            new File(SerializationToXmlTest.TMP_PATH + "co_AbstractA.xml"));
//        AbstractA localAbstractA = (AbstractA) unmarshaller.unmarshal(
//            new File(SerializationToXmlTest.TMP_PATH + "co_AbstractA.xml"));
//        ConcreteA localConcreteA = (ConcreteA) unmarshaller.unmarshal(
//            new File(SerializationToXmlTest.TMP_PATH + "co_ConcreteA.xml"));
//        
//        Assert.assertEquals(this.abstractA.getText(), localAbstractA.getText());
//        Assert.assertEquals(this.concreteA.getText(), localConcreteA.getText());
//    }

//    
//    Test fails as long as abstract imlementation is not annotated with @XmlRootElement and @XmlElement.
//    
  /**
   * Tests the marshalling with abstract context.
   * 
   * @throws JAXBException test fails
   * @throws IOException test fails
   */
  @Test
  public void testAbstractMarshalling() throws JAXBException, IOException {

      this.marshaller.marshal(this.abstractA, new FileWriter(SerializationToJsonTest.TMP_PATH + "ab_AbstractA.xml"));
      this.marshaller.marshal(this.concreteA, new FileWriter(SerializationToJsonTest.TMP_PATH + "ab_ConcreteA.xml"));
      
  }
  
  /**
   * Tests the unmarshalling with abstract context.
   * 
   * @throws JAXBException test fails
   */
  @Test(dependsOnMethods = "testAbstractMarshalling")
  public void testAbstractUnmarshalling() throws JAXBException {
      
      AbstractA localAbstractA = (AbstractA) this.unmarshaller.unmarshal(new File(
                SerializationToJsonTest.TMP_PATH + "ab_AbstractA.xml"));
      ConcreteA localConcreteA = (ConcreteA) this.unmarshaller.unmarshal(
                new File(SerializationToJsonTest.TMP_PATH + "ab_ConcreteA.xml"));
      
      Assert.assertEquals(this.abstractA.getText(), localAbstractA.getText());
      Assert.assertEquals(this.concreteA.getText(), localConcreteA.getText());
  }

}
