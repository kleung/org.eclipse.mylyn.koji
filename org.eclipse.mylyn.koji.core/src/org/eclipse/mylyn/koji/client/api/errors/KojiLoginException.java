package org.eclipse.mylyn.koji.client.api.errors;

/**
 * Exception thrown on Koji login failure.
 *
 */
public class KojiLoginException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private boolean certificatesMissing = false;
	
	/**
	 * Default constructor
	 */
	public KojiLoginException () {
		super();
	}
	
	/**
	 * Constructor that takes a cause (Throwable).
	 * @param cause The cause.
	 */
	public KojiLoginException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructor that takes a cause (Throwable) and a boolean to determine whether
	 * the certificate is missing.
	 * @param cause The cause.
	 * @param certsMissing {@code true} If certificate is missing.
	 */
	public KojiLoginException(Throwable cause, boolean certsMissing) {
		this(cause);
		this.certificatesMissing = certsMissing;
	}

	/**
	 * Checks if ~/.fedora.cert exists.
	 * @return {@code true} If and only if the certificate was missing.
	 */
	public boolean isCertificatesMissing() {
		return certificatesMissing;
	}
	
}
