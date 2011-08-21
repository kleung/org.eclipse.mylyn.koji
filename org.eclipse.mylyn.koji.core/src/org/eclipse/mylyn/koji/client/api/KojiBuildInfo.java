package org.eclipse.mylyn.koji.client.api;

import java.io.Serializable;
import java.util.Map;

/**
 * Class representing build info as returned by
 * getBuild XMLRPC call.
 *
 */


/*
 * getBuild(buildInfo, strict=False)
 *  description: Return information about a build.  buildID may be either
    a int ID, a string NVR, or a map containing 'name', 'version'
    and 'release.  A map will be returned containing the following
    keys:
      id: build ID
      package_id: ID of the package built
      package_name: name of the package built
      version
      release
      epoch
      nvr
      state
      task_id: ID of the task that kicked off the build
      owner_id: ID of the user who kicked off the build
      owner_name: name of the user who kicked off the build
      creation_event_id: id of the create_event
      creation_time: time the build was created (text)
      creation_ts: time the build was created (epoch)
      completion_time: time the build was completed (may be null)
      completion_ts: time the build was completed (epoch, may be null)

    If there is no build matching the buildInfo given, and strict is specified,
    raise an error.  Otherwise return None.
 */
@SuppressWarnings({ "unused", "rawtypes" })
public class KojiBuildInfo implements Comparable, Cloneable, Serializable {
	
	private static final long serialVersionUID = 6790855090981769141L;
	private int state;
	private Integer taskId;
	private String release;
	private int epoch;
	private String nvr;
	private String version;
	private String packageName;
	private int packageId;
	private String ownerName;
	private KojiTask task;
	private int buildId;
	private long createTime;	//POSIX timestamp
	private long completeTime;	//POSIX timestamp
	
	/**
	 * Default constructor.
	 */
	public KojiBuildInfo() {
		super();
		this.taskId = null;
		this.task = null;
	}

	/**
	 * @return {@code true} if the associated build of this build info is
	 *         complete.
	 */
	public boolean isComplete() {
		return state == 1;
	}
	
	/**
	 * @return The task id this build.
	 */
	public int getTaskId() {
		return this.taskId;
	}
	
	/**
	 * @return The release associated with this build info.
	 */
	public String getRelease() {
		return release;
	}
	
	/**
	 * @return The version associated with this build info.
	 */
	public String getVersion() {
		return this.version;
	}
	
	/**
	 * @return The name of the package associated with this build info.
	 */
	public String getPackageName() {
		return this.packageName;
	}
	
	/**
	 * @return the state of the associated build.
	 */
	public int getState() {
		return state;
	}

	/**
	 * @return the epoch of the associated build.
	 */
	public int getEpoch() {
		return epoch;
	}

	/**
	 * @return the name-version-release of the associated build
	 */
	public String getNvr() {
		return nvr;
	}

	/**
	 * @return the packageId
	 */
	public int getPackageId() {
		return packageId;
	}

	/**
	 * Gets the owner name.	
	 * @return The owner name.
	 */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * Sets the owner name.
	 * @param ownerName The owner name.
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	/**
	 * Sets the build state.
	 * @param state	The build state.
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * Sets the task ID.
	 * @param taskId	The task ID.
	 */
	public void setTaskId(int taskId) {
		this.taskId = new Integer(taskId);
	}
	
	/**
	 * Sets the task ID.
	 * @param taskId	The task ID.
	 */
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	/**
	 * Sets the release string.
	 * @param release The release string.
	 */
	public void setRelease(String release) {
		this.release = release;
	}

	/**
	 * Sets the epoch.
	 * @param epoch	The epoch.
	 */
	public void setEpoch(int epoch) {
		this.epoch = epoch;
	}

	/**
	 * Sets the NVR string.
	 * @param nvr The NVR string. 
	 */
	public void setNvr(String nvr) {
		this.nvr = nvr;
	}

	/**
	 * Sets the version string.
	 * @param version The version string.
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Sets the name of the package.
	 * @param packageName The name of the package.
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * Sets the package ID.
	 * @param packageId The package ID.
	 */
	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}

	/**
	 * Gets the task associated with the build.
	 * @return The task associated with the build.
	 */
	public KojiTask getTask() {
		return task;
	}

	/**
	 * Sets the task associated with the build.
	 * @param task The task associated with the build.
	 */
	public void setTask(KojiTask task) {
		this.task = task;
	}

	/**
	 * Gets the build ID.
	 * @return	The build ID.
	 */
	public int getBuildId() {
		return buildId;
	}

	/**
	 * Sets the build ID.
	 * @param buildId	The build ID.
	 */
	public void setBuildId(int buildId) {
		this.buildId = buildId;
	}
	
	/**
	 * Gets the creation time of the build.
	 * @return The creation time.
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * Sets the creation time of the build.
	 * @param createTime Creation time of the build.
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * Gets the completion time of the build.
	 * @return The completion time of the build.
	 */
	public long getCompleteTime() {
		return completeTime;
	}

	/**
	 * Sets the completion time of the build.
	 * @param completeTime Completion time of the build.
	 */
	public void setCompleteTime(long completeTime) {
		this.completeTime = completeTime;
	}

	/**
	 * Computes and returns the hashcode of this.
	 */
	@Override
	public int hashCode() {
		return (31 * this.state) + this.release.hashCode() +
				(2 * this.epoch) + (3 * this.nvr.hashCode()) + (4 * this.version.hashCode()) +
				(5 * this.packageName.hashCode()) + (6 * this.packageId) + (7 * this.ownerName.hashCode());
	}
	
	/**
	 * Checks whether a given object has the same task ID as this.
	 * 
	 * @throws ClassCastException
	 * @return boolean signifying whether this has the same task ID as task.
	 */
	@Override
	public boolean equals(Object build) throws ClassCastException {
		if (!(build instanceof KojiBuildInfo))
			throw new ClassCastException();
		KojiBuildInfo compare = (KojiBuildInfo) build;
		return this.taskId == compare.getTaskId();
	}
	
	/**
	 * Checks whether the given object has a task ID that is smaller than, equal
	 * to or bigger than this's task ID and return (-1, 0, 1) respectively.
	 * 
	 * @throws ClassCastException
	 * @return int determining whether this has a task Id that is smaller than,
	 *         equal to or bigger than task's.
	 */
	@Override
	public int compareTo(Object build) throws ClassCastException {
		boolean equals = this.equals(build);
		if (equals)
			return 0;
		else {
			KojiBuildInfo compare = (KojiBuildInfo) build;
			return ((this.taskId > compare.getTaskId()) ? 1 : -1);
		}
	}
	
	/**
	 * Clones the KojiBuildInfo object.
	 * 
	 * @return a clone of the KojiBuildInfo object.
	 */
	@Override
	public KojiBuildInfo clone() {
		KojiBuildInfo build = new KojiBuildInfo();
		build.setState(this.state);
		if(this.taskId != null)
			build.setTaskId(new Integer(this.taskId.intValue()));
		else
			build.setTaskId(null);
		if(this.release != null)
			build.setRelease(new String(this.release));
		else
			build.setRelease(null);
		build.setEpoch(this.epoch);
		if(this.nvr != null)
			build.setNvr(new String(this.nvr));
		else
			build.setNvr(null);
		if(this.version != null)
			build.setVersion(new String(this.version));
		else
			build.setVersion(null);
		if(this.packageName != null)
			build.setPackageName(new String(this.packageName));
		else
			build.setPackageName(null);
		build.setPackageId(this.packageId);
		if(this.ownerName != null)
			build.setOwnerName(new String(this.ownerName));
		else
			build.setOwnerName(null);
		if(this.task != null)
			build.setTask(this.task.clone());
		else
			build.setTask(null);
		build.setBuildId(this.buildId);
		build.setCreateTime(this.createTime);
		build.setCompleteTime(this.completeTime);
		return null;
	}

}
