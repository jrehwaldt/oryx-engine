package de.hpi.oryxengine.util.io;

import java.io.InputStream;


// TODO [@Gerado] Javadoc should be added
public class InputStreamSource implements StreamSource {
  
  InputStream inputStream;
  
  public InputStreamSource(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public InputStream getInputStream() {
    return inputStream;
  }
  
  public String toString() {
    return "InputStream";
  }
}
