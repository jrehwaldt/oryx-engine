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
      UUID uuid = UUID.randomUUID();
      int version = 0;
      
      ProcessDefinitionID id = new ProcessDefinitionID(uuid, version);
      Assert.assertEquals(id.toString(), uuid.toString() + ":0");
  }
  
  /**
   * Tests fromString().
   */
  @Test
  public void testFromString() {
      UUID uuid = UUID.randomUUID();
      String idString = uuid.toString() + ":0";
      
      ProcessDefinitionID idFromString = ProcessDefinitionID.fromString(idString);
      Assert.assertEquals(idFromString.getUUID(), uuid, "The UUID should be the one as in the string");
      Assert.assertEquals(idFromString.getVersion(), 0, "The version should be the one as in the string");
  }
}
