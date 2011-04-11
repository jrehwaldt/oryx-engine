package de.hpi.oryxengine.rest.serialization;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.testng.annotations.BeforeClass;

import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.worklist.AbstractWorklist;
import de.hpi.oryxengine.resource.worklist.EmptyWorklist;
import de.hpi.oryxengine.resource.worklist.ParticipantWorklist;
import de.hpi.oryxengine.resource.worklist.RoleWorklist;

/**
 * This class tests the serialization of our resource classes.
 * 
 * @see de.hpi.oryxengine.resource.AbstractResource
 * 
 * @author Jan Rehwaldt
 */
public class SerializationToXmlTest {
    
    public static final String TMP_PATH = "./target/";
    
    private AbstractResource<?> participantHarry = null;
    
    private JAXBContext context = null;
    private Marshaller marshaller = null;
    private Unmarshaller unmarshaller = null;
    
    private static final Class<?>[] CLASSES = new Class[] {
        AbstractResource.class, AbstractParticipant.class, Participant.class,
        AbstractWorklist.class, EmptyWorklist.class, ParticipantWorklist.class, RoleWorklist.class
    };
    
    /**
     * Setup.
     * 
     * @throws JAXBException initialization fails
     */
    @BeforeClass
    public void setUp() throws JAXBException {
        this.participantHarry = new Participant("Harry");
        
        this.context = JAXBContext.newInstance(CLASSES);
        
        this.marshaller = context.createMarshaller();
        this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        
        this.unmarshaller = context.createUnmarshaller();
    }
    
//    /**
//     * Tests the serialization of an abstract resource.
//     * 
//     * @throws IOException test fails
//     * @throws JAXBException test fails
//     */
//    @Test
//    public void testSerializationOfAbstractResource() throws JAXBException, IOException {
//        File xml = new File(TMP_PATH + "AbstractParticipantHarry.xml");
//        
//        this.marshaller.marshal(this.participantHarry, xml);
//        AbstractResource<?> localParticipantHarry = (AbstractResource<?>) this.unmarshaller.unmarshal(xml);
//        
//        Assert.assertEquals(this.participantHarry.getClass(), localParticipantHarry.getClass());
//        Assert.assertEquals(this.participantHarry, localParticipantHarry);
//    }
}
