package org.eclipse.mylyn.koji.client.api;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Class representing package as returned by getPackage XMLRPC call.
 * 
 * Author: Kiu Kwan Leung (Red Hat)
 */

@SuppressWarnings("rawtypes")
public class KojiPackage implements Comparable, Serializable, Cloneable {
	
	private static final long serialVersionUID = -468251306704904725L;
	private int packageID;						//package ID
	private String packageName;					//package name
	private String description;					//description
	private List<KojiBuildInfo> recentBuilds = null;	//list of recent builds of the package
	private KojiTask task = null;				//scratch build task.
	
	/**
	 * Default Constructor
	 */
	public KojiPackage() {
		super();
	}

	/**
	 * Gets the package ID.
	 * @return	The package ID.
	 */
	public int getPackageID() {
		return packageID;
	}

	/**
	 * Sets the package ID.
	 * @param packageID	The package ID.
	 */
	public void setPackageID(int packageID) {
		this.packageID = packageID;
	}

	/**
	 * Gets the package name.
	 * @return	The package name.
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * Sets the package name.
	 * @param packageName	The package name.
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * Gets a list of recent builds of the package.
	 * @return	The list of recent builds.
	 */
	public List<KojiBuildInfo> getRecentBuilds() {
		return recentBuilds;
	}

	/**
	 * Sets the list of recent builds of the package.
	 * @param recentBuilds	The list of recent builds.
	 */
	public void setRecentBuilds(List<KojiBuildInfo> recentBuilds) {
		this.recentBuilds = recentBuilds;
	}

	/**
	 * Gets the description of the package.
	 * @return The description of the package.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the package.
	 * @param description The description of the package.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the scratch build task.
	 * @return The scratch build task.
	 */
	public KojiTask getTask() {
		return task;
	}

	/**
	 * Sets the scratch build task.
	 * @param task The scratch build task.
	 */
	public void setTask(KojiTask task) {
		this.task = task;
	}

	@Override
	public int hashCode() {
		return (31 * this.packageID) + this.packageName.hashCode();
	}
	
	@Override
	public boolean equals(Object input){
		if(!(input instanceof KojiPackage))
			return false;
		KojiPackage in = (KojiPackage)input;
		return this.packageID == in.getPackageID();
	}
	
	@Override
	public int compareTo(Object input) throws ClassCastException {
		if(this.equals(input))
			return 0;
		if(!(input instanceof KojiPackage))
			throw new ClassCastException();
		KojiPackage in = (KojiPackage)input;
		return ((this.packageID > in.getPackageID())? 1:-1);
	}
	
	/**
	 * Clones the KojiPackage object.
	 * 
	 * @return A clone of the KojiPackage object.
	 */
	@Override
	public KojiPackage clone() {
		KojiPackage pack = new KojiPackage();
		pack.setPackageID(this.packageID);
		if(this.packageName != null)
			pack.setPackageName(new String(this.packageName));
		else
			pack.setPackageName(null);
		if(this.description != null)
			pack.setDescription(new String(this.description));
		else
			pack.setDescription(null);
		if(this.recentBuilds != null) {
			LinkedList<KojiBuildInfo> buildList = new LinkedList<KojiBuildInfo>();
			for(KojiBuildInfo b : this.recentBuilds)
				buildList.add(b.clone());
			pack.setRecentBuilds(buildList);
		}
		return null;
	}

}
