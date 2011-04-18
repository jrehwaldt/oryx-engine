package de.hpi.oryxengine.rest.serialization;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.factory.resource.ParticipantFactory;
import de.hpi.oryxengine.navigator.NavigatorState;
import de.hpi.oryxengine.navigator.NavigatorStatistic;
import de.hpi.oryxengine.resource.AbstractCapability;
import de.hpi.oryxengine.resource.AbstractOrganizationUnit;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.AbstractRole;
import de.hpi.oryxengine.resource.Capability;
import de.hpi.oryxengine.resource.OrganizationUnit;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Role;
import de.hpi.oryxengine.resource.worklist.AbstractDefaultWorklist;
import de.hpi.oryxengine.resource.worklist.AbstractWorklist;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;
import de.hpi.oryxengine.resource.worklist.EmptyWorklist;
import de.hpi.oryxengine.resource.worklist.ParticipantWorklist;
import de.hpi.oryxengine.resource.worklist.RoleWorklist;
import de.hpi.oryxengine.resource.worklist.WorklistItemImpl;
import de.hpi.oryxengine.rest.AbstractJsonServerTest;

/**
 * This class tests the serialization of our resource classes.
 * 
 * @see de.hpi.oryxengine.resource.AbstractResource
 * 
 * @author Jan Rehwaldt
 */
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SerializationToJsonTest extends AbstractJsonServerTest {

    private AbstractResource<?> participantJannik = null;
    private AbstractResource<?> participantBuzyWilli = null;
    
    /**
     * Setup.
     */
    @BeforeClass
    public void setUp() {
        this.participantJannik = ParticipantFactory.createJannik();
        this.participantBuzyWilli = ParticipantFactory.createBusyWilli();
    }
    
    /**
     * Tests the serialization of an abstract resource.
     * 
     * @throws IOException test fails
     * @throws JAXBException test fails
     */
    @Test
    public void testSerializationAndDesirializationOfParticipantJannik() throws JAXBException, IOException {
        File xml = new File(TMP_PATH + "ParticipantJannik.js");
        if (xml.exists()) {
            Assert.assertTrue(xml.delete());
        }
        
        this.mapper.writeValue(xml, this.participantJannik);
        
        Assert.assertTrue(xml.exists());
        Assert.assertTrue(xml.length() > 0);
        
        AbstractResource<?> localConcreteParticipantHarry = this.mapper.readValue(xml, Participant.class);
        Assert.assertNotNull(localConcreteParticipantHarry);
        
        AbstractResource<?> localAbstractParticipantHarry2 = this.mapper.readValue(xml, AbstractResource.class);
        Assert.assertNotNull(localAbstractParticipantHarry2);
        
        AbstractResource<?> localAbstractParticipantHarry = this.mapper.readValue(xml, AbstractParticipant.class);
        Assert.assertNotNull(localAbstractParticipantHarry);
        
        Assert.assertEquals(localAbstractParticipantHarry, localConcreteParticipantHarry);
        Assert.assertEquals(this.participantJannik.getClass(), localAbstractParticipantHarry.getClass());
        Assert.assertEquals(this.participantJannik, localAbstractParticipantHarry);
    }
    
    /**
     * Tests the serialization of an abstract resource.
     * 
     * @throws IOException test fails
     */
    @Test
    public void testSerializationAndDesirializationOfParticipantBuzyWilli() throws IOException {
        File xml = new File(TMP_PATH + "ParticipantWilli.js");
        if (xml.exists()) {
            Assert.assertTrue(xml.delete());
        }
        
        this.mapper.writeValue(xml, this.participantBuzyWilli);
        
        Assert.assertTrue(xml.exists());
        Assert.assertTrue(xml.length() > 0);
        
        AbstractResource<?> localParticipant = this.mapper.readValue(xml, AbstractResource.class);
        Assert.assertNotNull(localParticipant);
        
        Assert.assertEquals(this.participantBuzyWilli.getClass(), localParticipant.getClass());
        Assert.assertEquals(this.participantBuzyWilli, localParticipant);
    }
    
    /**
     * Tests the serializability of our resource classes.
     */
    @Test
    public void testClassSerializability() {
        
        //
        // resources
        //
        Assert.assertTrue(this.mapper.canSerialize(AbstractResource.class));
        
        Assert.assertTrue(this.mapper.canSerialize(AbstractParticipant.class));
        Assert.assertTrue(this.mapper.canSerialize(Participant.class));
        
        Assert.assertTrue(this.mapper.canSerialize(AbstractRole.class));
        Assert.assertTrue(this.mapper.canSerialize(Role.class));
        
        Assert.assertTrue(this.mapper.canSerialize(AbstractOrganizationUnit.class));
        Assert.assertTrue(this.mapper.canSerialize(OrganizationUnit.class));
        
        Assert.assertTrue(this.mapper.canSerialize(AbstractCapability.class));
        Assert.assertTrue(this.mapper.canSerialize(Capability.class));
        
        //
        // worklist
        //
        Assert.assertTrue(this.mapper.canSerialize(AbstractWorklistItem.class));
        Assert.assertTrue(this.mapper.canSerialize(WorklistItemImpl.class));
        Assert.assertTrue(this.mapper.canSerialize(AbstractWorklist.class));
        Assert.assertTrue(this.mapper.canSerialize(AbstractDefaultWorklist.class));
        Assert.assertTrue(this.mapper.canSerialize(ParticipantWorklist.class));
        Assert.assertTrue(this.mapper.canSerialize(RoleWorklist.class));
        Assert.assertTrue(this.mapper.canSerialize(EmptyWorklist.class));
        
        //
        // navigator
        //
        Assert.assertTrue(this.mapper.canSerialize(NavigatorStatistic.class));
        Assert.assertTrue(this.mapper.canSerialize(NavigatorState.class));
        
        //
        // util
        //
        Assert.assertTrue(this.mapper.canSerialize(UUID.class));
        Assert.assertTrue(this.mapper.canSerialize(List.class));
        Assert.assertTrue(this.mapper.canSerialize(Map.class));
    }

    @Override
    protected Class<?> getResource() {
        return null;
    }
}
