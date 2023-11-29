/*
 * MAATSystemException.java
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
 * Represents a system exception that requires the intervention of a system
 * administrator.
 */
public class MAATSystemException extends SystemException {

 	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructs an instance of <code>MAATApplicationException</code>.
     */
    public MAATSystemException() {
        super();
    }

    /**
     * Constructs an instance of <code>MAATApplicationException</code> with
     * the specified detail message.
     * @param message The detail message.
     */
    public MAATSystemException(String message) {
        super(message);
    }

    /**
     * Constructs an instance of <code>MAATApplicationException</code> with
     * the specified root cause.
     * @param rootCause The root cause of this exception
     */
    public MAATSystemException(Throwable rootCause) {
        super(rootCause);
    }

    /**
     * Constructs an instance of <code>MAATApplicationException</code> with
     * the specified root cause and detail message.
     * @param message The detail message.
     * @param rootCause The root cause of this exception
     */
    public MAATSystemException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

}
