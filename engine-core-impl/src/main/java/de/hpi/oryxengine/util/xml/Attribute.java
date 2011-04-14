package de.hpi.oryxengine.util.xml;

/**
 * This class represents an Attribute in a XML tag.
 */
public class Attribute {
  
  protected String name;
  
  protected String value;

  protected String uri;
  
  public Attribute(String name, String value) {
    this.name = name;
    this.value = value;
  }
  
  public Attribute(String name, String value, String uri) {
    this(name, value);
    this.uri = uri;
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }
}
