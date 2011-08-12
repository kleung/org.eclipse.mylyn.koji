package org.eclipse.mylyn.koji.client.api.errors;

/**
 * Exception thrown by koji clients.
 *
 */
public class KojiClientException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public KojiClientException() {
		super();
	}
	
	/**
	 * Constructor that takes an error message.
	 * @param message The error message.
	 */
	public KojiClientException(String message) {
		super(message);
	}
	
	/**
	 * Constructor that takes a cause (Throwable).
	 * @param cause The cause.
	 */
	public KojiClientException(Throwable cause) {
		super("Client error", cause);
	}
	
	/**
	 * Constructor that takes a cause (Throwable) and an error message.
	 * @param message The error message.
	 * @param cause The cause.
	 */
	public KojiClientException(String message, Throwable cause) {
		super(message, cause);
	}
}
