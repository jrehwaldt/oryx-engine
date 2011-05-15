package org.jodaengine.resource.allocation;


/**
 * The Class TaskTest.
 */
public class TaskTest {

    // TODO @Thorben-Refactoring everything commented out, as tasks do not exist anymore. Remove later
//    /**
//     * It is important that the copy constructor clones all internal datastructures that are modified, when
//     * HumanTaskActivities are executed.
//     */
//    private Task taskToCopy;
//    
//    /**
//     * Tests that the copy constructor produces a new object of assigned resources.
//     */
//    @Test
//    public void testCopyConstructorIdentity() {
//
//        
//        Task copiedTask = new TaskImpl(taskToCopy);
//
//        Assert.assertFalse(taskToCopy.getAssignedResources() == copiedTask.getAssignedResources(),
//            "the copy constructor has to create a new datastructure for assigned "
//                + "resource, as this is modified during activity execution");
//    }
//    
//    /**
//     * Tests that the copied assigned resources have the same content.
//     */
//    @Test
//    public void testCopyConstructorContent() {
//        Task copiedTask = new TaskImpl(taskToCopy);
//        Assert.assertTrue(taskToCopy.getAssignedResources().equals(copiedTask.getAssignedResources()),
//        "The assigned resources should nevertheless have the same content");
//    }
//    
//    /**
//     * Creates a simple task for the test.
//     */
//    @BeforeClass
//    public void setUp() {
//        Set<AbstractResource<?>> assignedResources = new HashSet<AbstractResource<?>>();
//        AbstractResource<?> resource = mock(AbstractResource.class);
//        assignedResources.add(resource);
//        taskToCopy = new TaskImpl("Do some Stuff", "Do a lot of Stuff.", mock(AllocationStrategies.class),
//            assignedResources);
//    }
}
