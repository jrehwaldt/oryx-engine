package de.hpi.oryxengine.rest.serialization;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.rest.serialization.proofofconcept.ConcreteA;

import junit.framework.Assert;

/**
 * 
 * @author Jan.Rehwaldt
 * 
 */
public class GsonTest {
    public static final String TMP_PATH = "./target/";
    private AbstractResource<?> participantHarry = null;
    
    @Test
    // TODO @Jan pack Mal hier einen Namen hin
    public void f() throws IOException {
        // serialize
        File jsonFile = new File(TMP_PATH + "AbstractParticipantHarry2.txt");
        FileWriter writer = new FileWriter(jsonFile);
        
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(participantHarry);
        writer.write(json);
        
        writer.close();
        
        
        // deserialize
        AbstractResource<?> deserializedHarry = gson.fromJson(json, Participant.class);
        
        Assert.assertEquals(this.participantHarry.getClass(), deserializedHarry.getClass());
        Assert.assertEquals(this.participantHarry, deserializedHarry);
    }
    
    @Test
    public void testConcreteA() throws IOException {
        // serialize
        File jsonFile = new File(TMP_PATH + "concreteA.txt");
        FileWriter writer = new FileWriter(jsonFile);
        
        Map<String, String> map = new HashMap<String, String>();
//        map.put("horst", "kevin");
        ConcreteA a = new ConcreteA("asd", map);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(a);
        writer.write(json);
        
        writer.close();
        
        
        // deserialize
        ConcreteA deserializedA = gson.fromJson(json, ConcreteA.class);
        
        Assert.assertEquals(a.getClass(), deserializedA.getClass());
//        Assert.assertEquals(a, deserializedA);
    }

    /**
     * Setup.
     * 
     * @throws JAXBException setup fails
     */
    @BeforeClass
    public void setUp()
    throws JAXBException {
        
        this.participantHarry = new Participant("Harry");

    }
}
