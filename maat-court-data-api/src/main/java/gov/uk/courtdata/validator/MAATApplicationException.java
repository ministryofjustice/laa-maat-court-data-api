/*
 * MAATApplicationException.java
 * 
 * ---------------------------------------------------------------------------------------------------------------------------------------------------------------------<p>
 * Version History<p>
 * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------<p>
 * Version     Date             Author      SIR     Description<p>
 * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------<p>
 *    1.0      28/04/2009       DS                  initial
 * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------<p>
 */
package gov.uk.courtdata.validator;

/**
 * Represents an application exception.
 */
public class MAATApplicationException extends ApplicationException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructs an instance of <code>MAATSystemException</code>.
     */
    public MAATApplicationException() {
        super();
    }

    /**
     * Constructs an instance of <code>MAATApplicationException</code> with
     * the specified detail message.
     * @param message The detail message.
     */
    public MAATApplicationException(String message) {
        super(message);
    }

    /**
     * Constructs an instance of <code>MAATApplicationException</code> with
     * the specified root cause.
     * @param rootCause The root cause of this exception
     */
    public MAATApplicationException(Throwable rootCause) {
        super(rootCause);
    }

    /**
     * Constructs an instance of <code>MAATApplicationException</code> with
     * the specified root cause and detail message.
     * @param message The detail message.
     * @param rootCause The root cause of this exception
     */
    public MAATApplicationException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

}
