package org.jodaengine.node.activity.bpmn;


/**
 * It test the {@link BpmnEventBasedGateway}. But this is more critical because the two triggers are only 5 ms apart.
 */
public class BpmnEventBasedXorGatewayCriticalTest /* extends BpmnEventBasedXorGatewayTest */ {

//    private final static int NEW_WAITING_TIME_1 = 300;
//    private final static int NEW_WAITING_TIME_2 = 305;
//
//    private static final int LONG_WAITING_TIME_TEST = 600;
//
//    /**
//     * Builds the graph structure of the little test definition that should be tested.
//     */
//    protected void buildProcessGraph() {
//
//        // Setting the time defining how long the time events should wait
//        waiting_time_1 = NEW_WAITING_TIME_1;
//        waiting_time_2 = NEW_WAITING_TIME_2;
//
//        super.buildProcessGraph();
//    }

    // TODO @Gerardo: Schau dir bitte Hudson-Builds 436, 437 an zum fixen.
//    /**
//     * The normal rounting behavior.
//     * 
//     * @throws Exception
//     */
////    @Test
//    public void testRouting()
//    throws Exception {
//
//        token.executeStep();
//
//        Thread.sleep(LONG_WAITING_TIME_TEST);
//
//        Assert.assertEquals(token.getCurrentNode(), endNode1, errorMessage());
//
//        Mockito.verify(token).resume(Mockito.any(ProcessIntermediateEvent.class));
//    }
}
