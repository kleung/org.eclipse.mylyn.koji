package org.eclipse.mylyn.koji.client.api;


/**
 * Class representing host as returned by getHost XMLRPC call.
 * 
 * Author: Kiu Kwan Leung (Red Hat)
 */
public class KojiHost{
	private int id;					//host ID
	private String name;			//host name
	private boolean enabled;		//host enabled flag
	private double taskLoad;		//current workload of the host
	private String description;		//host description
	private String architecture;	//architecture
	private double capacity;		//host work capacity
	private int userID;				//owner user ID
	private KojiUser user;			//owner
	private String comment;			//comment
	private boolean ready;			//host ready flag
	
	/**
	 * Default constructor - Java bean class style.
	 */
	public KojiHost() {
		super();
	}

	/**
	 * Gets the host ID.
	 * @return	The host ID.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the host ID.
	 * @param id	The host ID.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the host name.
	 * @return	The host name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the host name.
	 * @param name The host name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the host enabled flag.
	 * @return	The host enabled flag.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets the host enabled flag.
	 * @param enabled	The host enabled flag.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Gets the current task load of the host.
	 * @return	The current task load.
	 */
	public double getTaskLoad() {
		return taskLoad;
	}

	/**
	 * Sets the current task load of the host.
	 * @param taskLoad	The current task load of the host.
	 */
	public void setTaskLoad(double taskLoad) {
		this.taskLoad = taskLoad;
	}

	/**
	 * Gets the host description.
	 * @return	The host description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the host description.
	 * @param description	The host description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the host architecture.
	 * @return	The host architecture.
	 */
	public String getArchitecture() {
		return architecture;
	}

	/**
	 * Sets the host architecture.
	 * @param architecture	The host architecture.
	 */
	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}

	/**
	 * Gets the host workload capacity.
	 * @return	The host workload capacity.
	 */
	public double getCapacity() {
		return capacity;
	}

	/**
	 * Sets the host workload capacity.
	 * @param capacity	The host workload capacity.
	 */
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	/**
	 * Gets the user ID.
	 * @return	The user ID.
	 */
	public int getUserID() {
		return userID;
	}

	/**
	 * Sets the user ID.
	 * @param userID The user ID.
	 */
	public void setUserID(int userID) {
		this.userID = userID;
	}

	/**
	 * Gets the user.
	 * @return	The user.
	 */
	public KojiUser getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 * @param user	The user.
	 */
	public void setUser(KojiUser user) {
		this.user = user;
	}

	/**
	 * Gets the comment for the host.
	 * @return	The comment for the host.
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the comment for the host.
	 * @param comment	The comment of the host.
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Gets the host ready flag.
	 * @return	The host ready flag.
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * Sets the host ready flag.
	 * @param ready	The host ready flag.
	 */
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
}
