package de.hpi.oryxengine.util.xml;

import org.xml.sax.SAXParseException;

/**
 * Parse error class.
 */
public class Problem {

    protected String errorMessage;
    protected String resource;
    protected int line;
    protected int column;

    public Problem(SAXParseException e, String resource) {

        Throwable exception = e;
        while (exception != null) {
            if (this.errorMessage == null) {
                this.errorMessage = exception.getMessage();
            } else {
                this.errorMessage += ": " + exception.getMessage();
            }
            exception = exception.getCause();
        }
        this.resource = resource;
        this.line = e.getLineNumber();
        this.column = e.getColumnNumber();
    }

    public Problem(String errorMessage, String resourceName, Element element) {

        this.errorMessage = errorMessage;
        this.resource = resourceName;
        if (element != null) {
            this.line = element.getLine();
            this.column = element.getColumn();
        }
    }

    public String toString() {

        return errorMessage + (resource != null ? " | " + resource : "") + " | line " + line + " | column " + column;
    }
}
