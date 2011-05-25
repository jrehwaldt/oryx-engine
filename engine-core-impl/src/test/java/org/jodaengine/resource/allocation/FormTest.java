package org.jodaengine.resource.allocation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jodaengine.allocation.Form;
import org.jodaengine.allocation.JodaFormField;
import org.jodaengine.util.io.FileStreamSource;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the conversion of the formFile input to the internal representation.
 */

public class FormTest {
    private static final String FORM_PATH = "src/test/resources/org/jodaengine/resource/allocation/";
    private static final String FORM_LOCATION = FORM_PATH + "testForm.html";
    private static final String PROCESSED_FORM_LOCATION = FORM_PATH + "processedTestForm.html";
    
  @Test
  public void f() {
      FileStreamSource source = new FileStreamSource(new File(FORM_LOCATION));
      Form form = new FormImpl("form", source);
      JodaFormField field1 = form.getFormField("claimPoint1");
      JodaFormField field2 = form.getFormField("claimPoint2");
      
      Assert.assertEquals(field1.getReadExpression(), "#{claimPoint1}");
      Assert.assertEquals(field1.getWriteExpression(), "#{claimPoint1}");
      Assert.assertEquals(field2.getReadExpression(), "#{claimPoint2}");
      Assert.assertEquals(field2.getWriteExpression(), "#{claimPoint2}");
      Assert.assertEquals(form.getFormContentAsHTML().trim(), readFile(PROCESSED_FORM_LOCATION).trim());
  }
  
  /**
   * Reads a file and returns its content as a String.
   * 
   * @param fileName
   *            the file name
   * @return the string
   */
  private static String readFile(String fileName) {

      String fileContent = "";
      File file = new File(fileName);
      FileReader input;
      try {
          input = new FileReader(file);
          BufferedReader reader = new BufferedReader(input);

          String nextLine = reader.readLine();
          while (nextLine != null) {
              fileContent = fileContent.concat(nextLine + "\n");
              nextLine = reader.readLine();
          }

          reader.close();
          input.close();
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }

      fileContent = fileContent.trim();
      return fileContent;
  }
}
