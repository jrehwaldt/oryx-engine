package de.hpi.oryxengine.rest.serialization;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.factory.resource.ParticipantFactory;
import de.hpi.oryxengine.navigator.NavigatorState;
import de.hpi.oryxengine.navigator.NavigatorStatistic;
import de.hpi.oryxengine.node.activity.AbstractActivity;
import de.hpi.oryxengine.node.activity.Activity;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionImpl;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceContext;
import de.hpi.oryxengine.process.instance.ProcessInstanceContextImpl;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.AbstractCapability;
import de.hpi.oryxengine.resource.AbstractOrganizationUnit;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.AbstractRole;
import de.hpi.oryxengine.resource.Capability;
import de.hpi.oryxengine.resource.IdentityBuilder;
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
import de.hpi.oryxengine.rest.PatchCollectionChangeset;
import de.hpi.oryxengine.util.testing.AbstractJsonServerTest;

/**
 * This class tests the serialization of our resource classes.
 * 
 * @see de.hpi.oryxengine.resource.AbstractResource
 * 
 * @author Jan Rehwaldt
 */
public class SerializationToJsonTest extends AbstractJsonServerTest {
    
    private AbstractResource<?> participantJannik = null;
    private AbstractResource<?> participantBuzyWilli = null;

    /**
     * Setup.
     */
    @BeforeMethod
    public void setUp() {

        this.participantJannik = ParticipantFactory.createJannik();
        this.participantBuzyWilli = ParticipantFactory.createBusyWilli();
    }

    /**
     * Tests the serialization of an abstract resource.
     * 
     * @throws IOException
     *             test fails
     * @throws JAXBException
     *             test fails
     */
    @Test
    public void testSerializationAndDesirializationOfParticipantJannik()
    throws JAXBException, IOException {

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
     * @throws IOException
     *             test fails
     */
    @Test
    public void testSerializationAndDesirializationOfParticipantBuzyWilli()
    throws IOException {

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
     * Tests the serialization of our navigation statistics. This is necessary because occasionally
     * serializing of "boolean x = true" was not correctly deserialized.
     * 
     * @throws IOException test fails
     * @throws JAXBException test fails
     */
    @Test
    public void testSerializationAndDesirializationOfNavigationStatistics() throws JAXBException, IOException {
        File xml = new File(TMP_PATH + "NavigatorStatistics.js");
        if (xml.exists()) {
            Assert.assertTrue(xml.delete());
        }
        
        NavigatorStatistic stats = new NavigatorStatistic(1, 1, 1, true);
        this.mapper.writeValue(xml, stats);
        
        Assert.assertTrue(xml.exists());
        Assert.assertTrue(xml.length() > 0);
        
        NavigatorStatistic desStats = this.mapper.readValue(xml, NavigatorStatistic.class);
        Assert.assertNotNull(desStats);
        
        Assert.assertEquals(desStats.getNumberOfFinishedInstances(), stats.getNumberOfFinishedInstances());
        Assert.assertEquals(desStats.getNumberOfExecutionThreads(), stats.getNumberOfExecutionThreads());
        Assert.assertEquals(desStats.getNumberOfRunningInstances(), stats.getNumberOfRunningInstances());
        Assert.assertEquals(desStats.isNavigatorIdle(), stats.isNavigatorIdle());
    }
    
    /**
     * Tests the serialization of a {@link ProcessInstanceContext}.
     * 
     * @throws IOException test fails
     */
    @Test
    public void testSerializationAndDesirializationOfProcessInstanceContext() throws IOException {
        File xml = new File(TMP_PATH + "ProcessInstanceContext.js");
        if (xml.exists()) {
            Assert.assertTrue(xml.delete());
        }
        
        ProcessInstanceContext context = new ProcessInstanceContextImpl();
        context.setVariable("Harry", "ist ein Harry.");
        context.setVariable("Susi", "ist eine Frau.");
        context.setVariable("Joachim", "ärgert immer alle.");
        
        this.mapper.writeValue(xml, context);
        
        Assert.assertTrue(xml.exists());
        Assert.assertTrue(xml.length() > 0);
        
        ProcessInstanceContext localContext = this.mapper.readValue(xml, ProcessInstanceContext.class);
        Assert.assertNotNull(localContext);
        
        Assert.assertEquals(context.getClass(), localContext.getClass());
        Assert.assertEquals(context, localContext);
    }

    /**
     * It is a bug that the serialization participants with a role doesn't work. Isolate it. Fix it. Go.
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ResourceNotAvailableException 
     */
    @Test
    public void testSerializationOfParticipantsWithRole()
    throws IOException, ResourceNotAvailableException {
        
        // Setting up one Participant with a role
        IdentityBuilder identityBuilder = ServiceFactory.getIdentityService().getIdentityBuilder();
        AbstractRole role = identityBuilder.createRole("Participant with Role-Test");
        
        identityBuilder.participantBelongsToRole(this.participantJannik.getID(), role.getID());
        
        // create a new file to write in
        File xml = new File(TMP_PATH + "ParticipantJannikWithRole.js");
        if (xml.exists()) {
            Assert.assertTrue(xml.delete());
        }
        
        // write Participant Jannik in the file
        this.mapper.writeValue(xml, this.participantJannik);
        
        // do a couple of asserts on this xml.
        Assert.assertTrue(xml.exists());
        Assert.assertTrue(xml.length() > 0);
        
        AbstractResource<?> localConcreteParticipant = this.mapper.readValue(xml, Participant.class);
        Assert.assertNotNull(localConcreteParticipant);
        
        AbstractResource<?> localAbstractParticipant2 = this.mapper.readValue(xml, AbstractResource.class);
        Assert.assertNotNull(localAbstractParticipant2);
        
        AbstractResource<?> localAbstractParticipant = this.mapper.readValue(xml, AbstractParticipant.class);
        Assert.assertNotNull(localAbstractParticipant);
        
        Assert.assertEquals(localAbstractParticipant, localConcreteParticipant);
        Assert.assertEquals(this.participantJannik.getClass(), localAbstractParticipant.getClass());
        Assert.assertEquals(this.participantJannik, localAbstractParticipant);
    }

    /**
     * Serializes and deserializes a role.
     * 
     * @throws IOException
     *             test fails
     * @throws ResourceNotAvailableException 
     */
    @Test
    public void testSerializationOfRole()
    throws IOException, ResourceNotAvailableException {
        
        // Setting up one Participant with a role
        IdentityBuilder identityBuilder = ServiceFactory.getIdentityService().getIdentityBuilder();
        AbstractRole role = identityBuilder.createRole("Role with Participant-Test");
        identityBuilder.participantBelongsToRole(this.participantJannik.getID(), role.getID());
        
        // create a new file to write in
        File xml = new File(TMP_PATH + "RoleWithParticipantJannik.js");
        if (xml.exists()) {
            Assert.assertTrue(xml.delete());
        }
        
        this.mapper.writeValue(xml, role);
        
        Assert.assertTrue(xml.exists());
        Assert.assertTrue(xml.length() > 0);
        
        AbstractResource<?> desRole = this.mapper.readValue(xml, AbstractRole.class);
        Assert.assertNotNull(desRole);
        
        Assert.assertEquals(desRole.getClass(), role.getClass());
        Assert.assertEquals(desRole, role);
    }
    
    /**
     * Tests the serializability of our resource classes.
     * @throws Exception test fails
     */
    @Test
    public void testClassSerializability()
    throws Exception {
        
        // 
        // Use annotation scanner here...
        // 
//        ClassPathScanningCandidateComponentProvider scanner =
//            new ClassPathScanningCandidateComponentProvider(false);
//        
//        scanner.addIncludeFilter(new AnnotationTypeFilter(SharedDatatype.class));
//        
//        Set<BeanDefinition> beans = scanner.findCandidateComponents(BASE_PACKAGE);
//        Set<Class<?>> sharedClasses = new HashSet<Class<?>>();
//        for (BeanDefinition bd: beans) {
//            sharedClasses.add((Class<?>) Class.forName(bd.getBeanClassName()));
//        }
//        
//        for (Class<?> clazz: sharedClasses) {
//            Assert.assertTrue(this.mapper.canSerialize(clazz));
//        }
        
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
        Assert.assertTrue(this.mapper.canSerialize(AbstractProcessInstance.class));
        Assert.assertTrue(this.mapper.canSerialize(ProcessInstanceImpl.class));
        Assert.assertTrue(this.mapper.canSerialize(Token.class));
        Assert.assertTrue(this.mapper.canSerialize(TokenImpl.class));
        Assert.assertTrue(this.mapper.canSerialize(ProcessDefinition.class));
        Assert.assertTrue(this.mapper.canSerialize(ProcessDefinitionImpl.class));
        Assert.assertTrue(this.mapper.canSerialize(ProcessInstanceContext.class));
        Assert.assertTrue(this.mapper.canSerialize(ProcessInstanceContextImpl.class));
        
        Assert.assertTrue(this.mapper.canSerialize(Activity.class));
        Assert.assertTrue(this.mapper.canSerialize(AbstractActivity.class));
        
        //
        // check any concrete implementation of Activity
        //
        ClassPathScanningCandidateComponentProvider scanner =
            new ClassPathScanningCandidateComponentProvider(false);
        
        scanner.addIncludeFilter(new AssignableTypeFilter(Activity.class));
//        scanner.addIncludeFilter(new AssignableTypeFilter(PushPattern.class));
//        scanner.addIncludeFilter(new AssignableTypeFilter(CreationPattern.class));
        
        Set<BeanDefinition> beans = scanner.findCandidateComponents(BASE_PACKAGE);
        Class<?> clazz;
        for (BeanDefinition bd: beans) {
            clazz = (Class<?>) Class.forName(bd.getBeanClassName());
            Assert.assertTrue(this.mapper.canSerialize(clazz), clazz + " should be serializable.");
        }
        
        //
        // util
        //
        Assert.assertTrue(this.mapper.canSerialize(UUID.class));
        Assert.assertTrue(this.mapper.canSerialize(List.class));
        Assert.assertTrue(this.mapper.canSerialize(Map.class));
        Assert.assertTrue(this.mapper.canSerialize(PatchCollectionChangeset.class));
    }

    @Override
    protected Object getResourceSingleton() {

        return null;
    }
}
