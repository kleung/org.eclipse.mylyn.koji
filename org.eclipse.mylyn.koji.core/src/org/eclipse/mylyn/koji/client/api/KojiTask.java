package org.eclipse.mylyn.koji.client.api;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Class representing task as returned by getUser XMLRPC call.
 * (For performance reason, the fields for KojiChannel, KojiUser and KojiHost were commented out.
 * Author: Kiu Kwan Leung (Red Hat)
 */
@SuppressWarnings("rawtypes")
public class KojiTask implements Comparable, Cloneable, Serializable { 	// implements comparable for
																		// sorting task IDs.
	private static final long serialVersionUID = -3541912875472785302L;
	private int id; 							// task ID
	private double completionTime; 				// completion time in double, POSIX timestamp
	private double weight; 						// task weight
	private String architecture; 				// architecture
	private double startTime; 					// start time in double, POSIX timestamp
	private int taskStateCode; 					// task state code
	private String taskState; 					// string representation of task state
	private int parentTaskID; 					// parent task ID
	private KojiTask parentTask; 				// parent task
	private String label; 						// label
	private double creationTime; 				// creation time in double, POSIX timestamp
	private int channelID; 						// channel ID
	//private KojiChannel channel; 				// channel
	private int hostID; 						// host ID
	//private KojiHost host; 						// host
	private int priority; 						// priority
	private int ownerID; 						// owner ID
	//private KojiUser owner; 					// owner
	private String method; 						// method
	private String rpm;							// name of rpm file
	private String buildTarget;					// build target
	private List<Integer> descendentIDs;		// task descendent IDs
	private List<KojiTask> descendents;			// task decendents

	/**
	 * Default constructor - Java bean style.
	 */
	public KojiTask() {
		super();
	}

	/**
	 * Gets the task ID.
	 * 
	 * @return The task ID.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the task ID.
	 * 
	 * @param id
	 *            The task ID.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the completion time in double.
	 * 
	 * @return The completion time.
	 */
	public double getCompletionTime() {
		return completionTime;
	}

	/**
	 * Sets the completion time in double.
	 * 
	 * @param completionTime
	 *            The completion time.
	 */
	public void setCompletionTime(double completionTime) {
		this.completionTime = completionTime;
	}

	/**
	 * Gets the weight of the task.
	 * 
	 * @return The weight of the task.
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Sets the weight of the task.
	 * 
	 * @param weight
	 *            The weight of the task.
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * Gets the target architecture.
	 * 
	 * @return The target architecture.
	 */
	public String getArchitecture() {
		return architecture;
	}

	/**
	 * Sets the target architecture.
	 * 
	 * @param architecture
	 *            The target architecture.
	 */
	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}

	/**
	 * Gets the start time of the task in double.
	 * 
	 * @return The start time of the task.
	 */
	public double getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time of the task in double.
	 * 
	 * @param startTime
	 *            The start time of the task.
	 */
	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the state code of the task.
	 * 
	 * @return The state code of the task.
	 */
	public int getTaskStateCode() {
		return taskStateCode;
	}

	/**
	 * Sets the state code of the task.
	 * 
	 * @param taskStateCode
	 *            The state code of the task.
	 */
	public void setTaskStateCode(int taskStateCode) {
		this.taskStateCode = taskStateCode;
	}

	/**
	 * Sets the state string of the task.
	 * 
	 * @return The state string of the task.
	 */
	public String getTaskState() {
		return taskState;
	}

	/**
	 * Sets the state string of the task.
	 * 
	 * @param taskState
	 *            The state string of the task.
	 */
	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	/**
	 * Gets the parent task's ID.
	 * 
	 * @return The parent task's ID.
	 */
	public int getParentTaskID() {
		return parentTaskID;
	}

	/**
	 * Sets the parent task's ID.
	 * 
	 * @param parentTaskID
	 *            The parent task's ID.
	 */
	public void setParentTaskID(int parentTaskID) {
		this.parentTaskID = parentTaskID;
	}

	/**
	 * Gets the parent task.
	 * 
	 * @return The parent task.
	 */
	public KojiTask getParentTask() {
		return parentTask;
	}

	/**
	 * Sets the parent task.
	 * 
	 * @param parentTask
	 *            The parent task.
	 */
	public void setParentTask(KojiTask parentTask) {
		this.parentTask = parentTask;
	}

	/**
	 * Gets the task label.
	 * 
	 * @return The task label.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the task label.
	 * 
	 * @param label
	 *            The task label.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Gets the task creation time in double.
	 * 
	 * @return The task creation time.
	 */
	public double getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the task creation time in double
	 * 
	 * @param creationTime
	 *            The task creation time.
	 */
	public void setCreationTime(double creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Gets the channel ID.
	 * 
	 * @return The channel ID.
	 */
	public int getChannelID() {
		return channelID;
	}

	/**
	 * Sets the channel ID.
	 * 
	 * @param channelID
	 *            The channel ID.
	 */
	public void setChannelID(int channelID) {
		this.channelID = channelID;
	}

//	/**
//	 * Gets the channel.
//	 * 
//	 * @return The channel.
//	 */
//	public KojiChannel getChannel() {
//		return channel;
//	}

//	/**
//	 * Sets the channel.
//	 * 
//	 * @param channel
//	 *            The channel.
//	 */
//	public void setChannel(KojiChannel channel) {
//		this.channel = channel;
//	}

	/**
	 * Gets the host ID.
	 * 
	 * @return The host ID.
	 */
	public int getHostID() {
		return hostID;
	}

	/**
	 * Sets the host ID.
	 * 
	 * @param hostID
	 *            The host ID.
	 */
	public void setHostID(int hostID) {
		this.hostID = hostID;
	}

//	/**
//	 * Gets the host.
//	 * 
//	 * @return The host.
//	 */
//	public KojiHost getHost() {
//		return host;
//	}

//	/**
//	 * Sets the host.
//	 * 
//	 * @param host
//	 *            The host.
//	 */
//	public void setHost(KojiHost host) {
//		this.host = host;
//	}

	/**
	 * Gets the task priority.
	 * 
	 * @return The task priority.
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Sets the task priority.
	 * 
	 * @param priority
	 *            The task priority.
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * Gets the task owner ID.
	 * 
	 * @return The task owner ID.
	 */
	public int getOwnerID() {
		return ownerID;
	}

	/**
	 * Sets the task owner ID.
	 * 
	 * @param ownerID
	 *            The task owner ID.
	 */
	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

//	/**
//	 * Gets the task owner.
//	 * 
//	 * @return The task owner.
//	 */
//	public KojiUser getOwner() {
//		return owner;
//	}

//	/**
//	 * Sets the task owner.
//	 * 
//	 * @param owner
//	 *            The task owner.
//	 */
//	public void setOwner(KojiUser owner) {
//		this.owner = owner;
//	}

	/**
	 * Gets the task method.
	 * 
	 * @return The task method.
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Sets the task method.
	 * 
	 * @param method
	 *            The task method.
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	
	/**
	 * Gets a list of task decendent IDs.
	 * @return	The list of task decendent IDs.
	 */
	public List<Integer> getDescendentIDs() {
		return descendentIDs;
	}

	/**
	 * Sets a list of task decendent IDs.
	 * @param descendentIDs	The list of task decendent IDs.
	 */
	public void setDescendentIDs(List<Integer> descendentIDs) {
		this.descendentIDs = descendentIDs;
	}

	/**
	 * Gets a list of task decendents.
	 * @return	The list of task decendents.
	 */
	public List<KojiTask> getDescendents() {
		return descendents;
	}

	/**
	 * Sets a list of task decendents.
	 * @param descendents	The list of task decendents.
	 */
	public void setDescendents(List<KojiTask> descendents) {
		this.descendents = descendents;
	}

	/**
	 * Gets the name of the associated RPM file.
	 * @return The name of the associated RPM file.
	 */
	public String getRpm() {
		return rpm;
	}

	/**
	 * Sets the name of the associated RPM file.
	 * @param rpm The name of the associated RPM file.
	 */
	public void setRpm(String rpm) {
		this.rpm = rpm;
	}

	/**
	 * Gets the release string.
	 * @return The release string.
	 */
	public String getBuildTarget() {
		return buildTarget;
	}

	/**
	 * Sets the release string.
	 * @param buildTarget The release string.
	 */
	public void setBuildTarget(String buildTarget) {
		this.buildTarget = buildTarget;
	}

	/**
	 * Computes and returns the hashcode of this.
	 */
	@Override
	public int hashCode() {
		return (31 * this.id) + this.architecture.hashCode()
				+ (2 * this.taskState.hashCode())
				+ (3 * this.ownerID);
	}

	/**
	 * Checks whether a given object has the same task ID as this.
	 * 
	 * @throws ClassCastException
	 * @return boolean signifying whether this has the same task ID as task.
	 */
	@Override
	public boolean equals(Object task) {
		if (!(task instanceof KojiTask))
			return false;
		KojiTask compare = (KojiTask) task;
		return this.id == compare.getId();
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
	public int compareTo(Object task) throws ClassCastException {
		boolean equals = this.equals(task);
		if (equals)
			return 0;
		else {
			KojiTask compare = (KojiTask) task;
			return ((this.id > compare.getId()) ? 1 : -1);
		}
	}
	
	@Override
	public KojiTask clone() {
		KojiTask task = new KojiTask();
		task.setId(this.id);
		task.setCompletionTime(this.completionTime);
		task.setWeight(this.weight);
		if(this.architecture != null)
			task.setArchitecture(new String(this.architecture));
		else
			task.setArchitecture(null);
		task.setStartTime(this.startTime);
		task.setTaskStateCode(this.taskStateCode);
		if(this.taskState != null)
			task.setTaskState(new String(this.taskState));
		else
			task.setTaskState(null);
		task.setParentTaskID(this.parentTaskID);
		if(this.parentTask != null)
			task.setParentTask(this.parentTask.clone());
		if(this.label != null)
			task.setLabel(new String(this.label));
		else
			task.setLabel(null);
		task.setCreationTime(this.creationTime);
		task.setChannelID(this.channelID);
//		if(this.channel != null)
//			task.setChannel(this.channel.clone());
//		else
//			task.setChannel(null);
		task.setHostID(this.hostID);
//		if(this.host != null)
//			task.setHost(this.host.clone());
//		else
//			task.setHost(null);
		task.setPriority(this.priority);
		task.setOwnerID(this.ownerID);
//		if(this.owner != null)
//			task.setOwner(this.owner.clone());
//		else
//			task.setOwner(null);
		if(this.method != null)
			task.setMethod(new String(this.method));
		else
			task.setMethod(null);
		if(this.rpm != null)
			task.setRpm(new String(this.rpm));
		else
			task.setRpm(null);
		if(this.buildTarget != null)
			task.setBuildTarget(new String(this.buildTarget));
		else
			task.setBuildTarget(null);
		if(this.descendentIDs != null) {
			LinkedList<Integer> idList = new LinkedList<Integer>();
			for(Integer i : this.descendentIDs)
				idList.add(new Integer(i.intValue()));
			task.setDescendentIDs(idList);
		} else
			task.setDescendentIDs(null);
		if(this.descendents != null) {
			LinkedList<KojiTask> taskList = new LinkedList<KojiTask>();
			for(KojiTask t : this.descendents)
				taskList.add(t.clone());
			task.setDescendents(taskList);
		} else
			task.setDescendents(null);
		return null;
	}
	
}
