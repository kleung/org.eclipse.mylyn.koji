package org.eclipse.mylyn.koji.client.api;

import java.util.List;

/**
 * Class representing package as returned by getPackage XMLRPC call.
 * 
 * Author: Kiu Kwan Leung (Red Hat)
 */

@SuppressWarnings("rawtypes")
public class KojiPackage implements Comparable {
	
	private int packageID;						//package ID
	private String packageName;					//package name
	private String description;					//description
	private List<KojiBuildInfo> recentBuilds;	//list of recent builds of the package
	
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

	@Override
	public int hashCode() {
		return (31 * this.packageID) + this.packageName.hashCode() + (2 * this.recentBuilds.hashCode());
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
}
