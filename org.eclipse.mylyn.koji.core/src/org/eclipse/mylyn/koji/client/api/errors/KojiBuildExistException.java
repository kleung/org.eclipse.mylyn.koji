package org.eclipse.mylyn.koji.client.api.errors;

import org.eclipse.mylyn.koji.messages.KojiText;
import org.eclipse.osgi.util.NLS;

/**
 * Thrown if some build already existed, when another one was attempted to be
 * pushed to koji.
 *
 */
public class KojiBuildExistException extends KojiClientException {

	private static final long serialVersionUID = 1L;
	
	//existing task ID
	private int taskId;
	
	/**
	 * Constructor that takes the task ID.
	 * @param taskId The task ID.
	 */
	public KojiBuildExistException(int taskId) {
		super();
		this.taskId = taskId;
	}
	
	/**
	 * Gets the error message.
	 * @return The error message.
	 */
	public String getMessage() {
		return NLS.bind(KojiText.KojiBuildExistException_msg, this.taskId);
	}
	
	/**
	 * Gets the task ID.
	 * @return The task ID of the existing build.
	 */
	public int getTaskId() {
		return this.taskId;
	}

}
