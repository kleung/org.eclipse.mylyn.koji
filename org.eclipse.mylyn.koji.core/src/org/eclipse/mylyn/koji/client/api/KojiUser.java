package org.eclipse.mylyn.koji.client.api;

import java.io.Serializable;

/**
 * Class representing user as returned by getUser XMLRPC call.
 * 
 * Author: Kiu Kwan Leung (Red Hat)
 */
public class KojiUser implements Cloneable, Serializable {

	private static final long serialVersionUID = -8484040325688944007L;
	private int id;				//user ID
	private int statusCode;		//user status code
	private String status;		//String representation of status code
	private String name;		//user name
	private int userTypeCode;	//user type code
	private String userType;	//String representation of user type code
	
	/**
	 * Default constructor - Java Bean class style.
	 */
	public KojiUser() {
		super();
	}

	/**
	 * Gets the user ID.
	 * @return user ID.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the user ID.
	 * @param id user ID.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the user status code.
	 * @return The user status code.
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * Sets the user status code.
	 * @param statusCode The user status code.
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Sets the user status.
	 * @return	The user status.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the user status.
	 * @param status	The user status.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the user name.
	 * @return	The user name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the user name.
	 * @param name	The user name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the user type code.
	 * @return	The user type code.
	 */
	public int getUserTypeCode() {
		return userTypeCode;
	}

	/**
	 * Sets the user type code.
	 * @param userTypeCode	The user type code.
	 */
	public void setUserTypeCode(int userTypeCode) {
		this.userTypeCode = userTypeCode;
	}

	/**
	 * Gets the user type.
	 * @return	The user type.
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * Sets the user type.
	 * @param userType	The user type.
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}
	/**
	 * Clones the KojiUser object.
	 * 
	 * @return A clone of the KojiUser object.
	 */
	@Override
	public KojiUser clone() {
		KojiUser user = new KojiUser();
		user.setId(this.id);
		user.setStatusCode(this.statusCode);
		user.setUserTypeCode(this.userTypeCode);
		if(this.name != null)
			user.setName(new String(this.name));
		else
			user.setName(null);
		if(this.status != null)
			user.setStatus(new String(this.status));
		else
			user.setStatus(null);
		if(this.userType != null)
			user.setUserType(new String(this.userType));
		else
			user.setUserType(null);
		return user;
	}
	
}
