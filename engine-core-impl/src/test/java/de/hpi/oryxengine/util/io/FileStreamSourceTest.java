package de.hpi.oryxengine.util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;

/**
 * Tests {@link FileStreamSource}. I hate Checkstyle ;-D.
 */
public class FileStreamSourceTest {

    private static final String TEST_FILE_PATH = "src/test/resources/de/hpi/oryxengine/util/io/filestreamsource-test.file";

    @Test
    public void testCorrectInputStream() throws IOException {

        StreamSource fileStreamSource = new FileStreamSource(TEST_FILE_PATH);

        InputStream inputStream = fileStreamSource.getInputStream();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = bufferedReader.readLine();
        Assert.assertEquals(line, "Hello Joda-Engine");
        Assert.assertEquals(line, "Hello Joda-Engine");
    }

    @Test(expectedExceptions = DalmatinaRuntimeException.class)
    public void testIntantiationWithNoExistingFile() {

        File file = new File("123" + TEST_FILE_PATH); 
        StreamSource fileStreamSource = new FileStreamSource(file);
        
        Assert.fail("The 'DalmatinaRuntinmeException' should already been thrown.'");
    }
}
