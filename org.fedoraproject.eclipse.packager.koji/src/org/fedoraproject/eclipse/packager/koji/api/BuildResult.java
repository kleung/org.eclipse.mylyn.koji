package org.fedoraproject.eclipse.packager.koji.api;

import org.fedoraproject.eclipse.packager.api.ICommandResult;

/**
 * Result of a koji build as triggered by KojiBuildCommand.
 */
public class BuildResult implements ICommandResult {

	/**
	 * Flag if build was successful.
	 */
	private boolean successful;
	
	/**
	 * Task id of a successful build.
	 */
	private int taskId;
	
	/**
	 * Set the task ID of the pushed build.
	 * 
	 * @param taskId
	 */
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	
	/**
	 * The task id of the build, which has been pushed.
	 * 
	 * @return The task id of the pushed build.
	 */
	public int getTaskId() {
		return this.taskId;
	}
	
	/**
	 * Invoke if some error occurred during build.
	 */
	public void setFailure() {
		this.successful = false;
	}
	
	/**
	 * Invoke if build was successful.
	 */
	public void setSuccessful() {
		this.successful = true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.fedoraproject.eclipse.packager.api.ICommandResult#wasSuccessful()
	 */
	@Override
	public boolean wasSuccessful() {
		return this.successful;
	}

}
