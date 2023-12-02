/**
 * 
 */
package gov.uk.courtdata.validator;

/**
 * @author SWAN-D
 *
 */
public class GlobalSearchOperatorException extends MAATApplicationException {

	
	/**
	 * @param message
	 * @param rootCause
	 */
	public GlobalSearchOperatorException(String message, Throwable rootCause) {
		super(message, rootCause);
	}

	/**
	 * @param message
	 */
	public GlobalSearchOperatorException(String message) {
		super(message);
	}

	/**
	 * @param rootCause
	 */
	public GlobalSearchOperatorException(Throwable rootCause) {
		super(rootCause);
	}

	public GlobalSearchOperatorException()
	{
		super();		
	}
}
