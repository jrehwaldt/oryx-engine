package org.jodaengine.process.definition;

import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the toString and fromString methods of {@link ProcessDefinitionID}.
 */
public class ProcessDefinitionIDTest {
    
  /**
   * Tests toString().
   */
  @Test
  public void testToString() {
      String identifier = UUID.randomUUID().toString();
      int version = 0;
      
      ProcessDefinitionID id = new ProcessDefinitionID(identifier, version);
      Assert.assertEquals(id.toString(), identifier.toString() + ":0");
  }
  
  /**
   * Tests fromString().
   */
  @Test
  public void testFromString() {
      String identifier = UUID.randomUUID().toString();
      String idString = identifier + ":0";
      
      ProcessDefinitionID idFromString = ProcessDefinitionID.fromString(idString);
      Assert.assertEquals(idFromString.getIdentifier(), identifier, "The UUID should be the one as in the string");
      Assert.assertEquals(idFromString.getVersion(), 0, "The version should be the one as in the string");
  }
}
