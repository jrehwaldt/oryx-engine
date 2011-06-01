package org.jodaengine.ext.debugging.listener;

import java.io.InputStream;

import org.jodaengine.deployment.ProcessDefinitionImporter;
import org.jodaengine.deployment.importer.definition.BpmnXmlImporter;
import org.jodaengine.deployment.importer.definition.bpmn.BpmnXmlParseListener;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.shared.DebuggerAttribute;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.util.ReflectionUtil;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * This class tests the {@link DebuggerBpmnXmlParseListener} implementation.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-27
 */
public class DebuggerBpmnXmlParseListenerTest extends AbstractJodaEngineTest {
    
    // TODO @Gerardo(CodeReview) Unbedingt die Xmls auf räumen; teilweise sind da tags drinne die keine Sau braucht
    private static final String RESOURCE_PATH = "org/jodaengine/ext/debugging/listener/";
    private static final String DEBUGGER_ENABLED  = RESOURCE_PATH + "DebuggingEnabledAndSvgAvailable.bpmn.xml";
    private static final String DEBUGGER_DISABLED = RESOURCE_PATH + "DebuggingDisabledAndSvgAvailable.bpmn.xml";
    private static final String SVG_AVAILABLE     = RESOURCE_PATH + "DebuggingEnabledAndSvgAvailable.bpmn.xml";
    private static final String NO_SVG_AVAILABLE  = RESOURCE_PATH + "DebuggingEnabledAndNoSvgAvailable.bpmn.xml";
    private static final String NO_CONTEXT        = RESOURCE_PATH + "NoDebuggingExtension.bpmn.xml";
    private static final String BREAKPOINT_DEFINED = RESOURCE_PATH + "DebuggingEnabledAndBreakpointAvailable.bpmn.xml";

    private static final short WITH_CONDITION = 1;
    private static final short WITHOUT_CONDITION = 2;
    
    private BpmnXmlParseListener listener;
    
    /**
     * Setup.
     */
    @BeforeClass
    public void setUp() {
        this.listener = new DebuggerBpmnXmlParseListener();
    }
    
    /**
     * Tests that the listener correctly extracts the debugger enabled state.
     */
    @Test
    public void testBpmnParseListenerEnablingTheDebugContext() {

        InputStream bpmnXml = ReflectionUtil.getResourceAsStream(DEBUGGER_ENABLED);
        Assert.assertNotNull(bpmnXml);
        
        ProcessDefinitionImporter importer = new BpmnXmlImporter(bpmnXml, this.listener);
        ProcessDefinition definition = importer.createProcessDefinition();
        
        Assert.assertNotNull(definition);
        Assert.assertNotNull(definition.getID());
        
        DebuggerAttribute attribute = DebuggerAttribute.getAttributeIfExists(definition);
        Assert.assertNotNull(attribute);
        
        Assert.assertTrue(attribute.isEnabled());
    }
    
    /**
     * Tests that the listener correctly extracts the debugger disabled state.
     */
    @Test
    public void testBpmnParseListenerDisablingTheDebugContext() {
        
        InputStream bpmnXml = ReflectionUtil.getResourceAsStream(DEBUGGER_DISABLED);
        Assert.assertNotNull(bpmnXml);
        
        ProcessDefinitionImporter importer = new BpmnXmlImporter(bpmnXml, this.listener);
        ProcessDefinition definition = importer.createProcessDefinition();
        
        Assert.assertNotNull(definition);
        Assert.assertNotNull(definition.getID());
        
        DebuggerAttribute attribute = DebuggerAttribute.getAttributeIfExists(definition);
        Assert.assertNotNull(attribute);
        
        Assert.assertFalse(attribute.isEnabled());
    }
    
    /**
     * Tests that the listener correctly extracts the svg artifact name.
     */
    @Test
    public void testBpmnParseListenerExtractsSvgName() {
        
        InputStream bpmnXml = ReflectionUtil.getResourceAsStream(SVG_AVAILABLE);
        Assert.assertNotNull(bpmnXml);
        
        ProcessDefinitionImporter importer = new BpmnXmlImporter(bpmnXml, this.listener);
        ProcessDefinition definition = importer.createProcessDefinition();
        
        Assert.assertNotNull(definition);
        Assert.assertNotNull(definition.getID());
        
        DebuggerAttribute attribute = DebuggerAttribute.getAttributeIfExists(definition);
        Assert.assertNotNull(attribute);
        
        Assert.assertEquals(attribute.getSvgArtifact(), "someArtifactName.svg");
    }
    
    /**
     * Tests that the listener correctly extracts the svg artifact name.
     */
    @Test
    public void testBpmnParseListenerSetsProcessIdIfNoSvgNameSpecified() {
        
        InputStream bpmnXml = ReflectionUtil.getResourceAsStream(NO_SVG_AVAILABLE);
        Assert.assertNotNull(bpmnXml);
        
        ProcessDefinitionImporter importer = new BpmnXmlImporter(bpmnXml, this.listener);
        ProcessDefinition definition = importer.createProcessDefinition();
        
        Assert.assertNotNull(definition);
        Assert.assertNotNull(definition.getID());
        
        DebuggerAttribute attribute = DebuggerAttribute.getAttributeIfExists(definition);
        Assert.assertNotNull(attribute);
        
        Assert.assertEquals(attribute.getSvgArtifact(), definition.getID().getIdentifier() + ".svg");
    }
    
    /**
     * Tests that the listener correctly work if not extension is found.
     */
    @Test
    public void testBpmnParseListenerCreatesDefaultContextIfNoExtensionIsFound() {
        
        InputStream bpmnXml = ReflectionUtil.getResourceAsStream(NO_CONTEXT);
        Assert.assertNotNull(bpmnXml);
        
        ProcessDefinitionImporter importer = new BpmnXmlImporter(bpmnXml, this.listener);
        ProcessDefinition definition = importer.createProcessDefinition();
        
        Assert.assertNotNull(definition);
        Assert.assertNotNull(definition.getID());
        
        DebuggerAttribute attribute = DebuggerAttribute.getAttributeIfExists(definition);
        Assert.assertNotNull(attribute);
        
        Assert.assertFalse(attribute.isEnabled());
    }
    
    /**
     * Tests that breakpoints are extracted correctly.
     */
    @Test
    public void testBreakpointExtraction() {
        
        InputStream bpmnXml = ReflectionUtil.getResourceAsStream(BREAKPOINT_DEFINED);
        Assert.assertNotNull(bpmnXml);
        
        ProcessDefinitionImporter importer = new BpmnXmlImporter(bpmnXml, this.listener);
        
        //
        // parse the process definition, extract the breakpoints
        //
        ProcessDefinition definition = importer.createProcessDefinition();
        
        Assert.assertNotNull(definition);
        Assert.assertNotNull(definition.getID());
        
        DebuggerAttribute attribute = DebuggerAttribute.getAttributeIfExists(definition);
        Assert.assertNotNull(attribute);
        
        Assert.assertNotNull(attribute.getBreakpoints());
        Assert.assertEquals(attribute.getBreakpoints().size(), WITH_CONDITION + WITHOUT_CONDITION);
        
        //
        // correct extraction?
        //
        short withCondition = 0;
        short withoutCondition = 0;
        for (Breakpoint breakpoint: attribute.getBreakpoints()) {
            Assert.assertNotNull(breakpoint);
            
            //
            // we have three breakpoints in the file
            //   a) with condition and COMPLETED
            //   b) without condition and INVALID_STATE_WILL_AUTOGENERATE_READY (-> READY)
            //   b) without condition and auto-generated READY
            //
            if (breakpoint.getCondition() == null) {
                withoutCondition++;
                Assert.assertEquals(breakpoint.getState(), ActivityState.READY);
            } else {
                withCondition++;
                Assert.assertEquals(breakpoint.getState(), ActivityState.COMPLETED);
            }
        }
        Assert.assertEquals(withCondition, WITH_CONDITION);
        Assert.assertEquals(withoutCondition, WITHOUT_CONDITION);
    }
}
