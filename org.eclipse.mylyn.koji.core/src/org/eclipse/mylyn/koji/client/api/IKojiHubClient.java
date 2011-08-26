/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.koji.client.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.mylyn.koji.client.api.errors.KojiClientException;
import org.eclipse.mylyn.koji.client.api.errors.KojiLoginException;

/**
 * Interface for Koji hub client implementations. At the moment there is only
 * a SSL login based implementation.
 *
 */
public interface IKojiHubClient {

	/**
	 * Log in on the remote host as specified by the hub client url and stores
	 * session information into XMLRPC config URL.
	 * 
	 * @return Login session information, which was stored.
	 * @throws KojiHubClientLoginException
	 *             If an error occurred.
	 */
	public HashMap<?, ?> login() throws KojiLoginException;

	/**
	 * Logout from hub url and discard session.
	 * 
	 * @throws KojiHubClientException
	 */
	public void logout() throws KojiClientException;

	/**
	 * Initiate a build on the remote hub host. Checks, if a build with the
	 * given "Name-Release-Version" (NVR) has been built already.
	 * 
	 * @param target
	 *            The dist-tag (see: $ koji list-targets).
	 * @param scmURL
	 * @param nvr
	 *            Name-Version-Release (see: RPM package naming).
	 * @param scratch
	 *            Set to {@code true} for a scratch build.
	 * @return The task ID.
	 * @throws KojiHubClientException
	 *             If some error occurred.
	 */
	public int build(String target, String scmURL, String nvr, boolean scratch)
			throws KojiClientException;
	
	/**
	 * Fetches information related to a name-version-release token.
	 * 
	 * @param nvr
	 *            The name-version-release of the build for which to fetch
	 *            information.
	 * 
	 * @throws KojiHubClientException
	 *             If some error occurred.
	 * 
	 * @return The build information for the given nvr or {@code null} if build
	 *         does not exist
	 */
	public KojiBuildInfo getBuild(String nvr) throws KojiClientException;
	
	/**
	 * @param path 
	 * 				Path to upload to on the koji server.
	 * @param name
	 * 				The name of the file being uploaded. 
	 * @param size
	 * 				The size of the file being uploaded in bytes.
	 * @param md5sum
	 * 				The MD5 sum of the file being uploaded.
	 * @param offset
	 * 				The number of bytes to skip when building the file 
	 * 				from multiple uploads. 
	 * @param data
	 * 				The Base64 representation of the file.
	 * @return True if successful, false if not.
	 * @throws KojiHubClientException
	 */
	public boolean uploadFile(String path, String name, int size, String md5sum, int offset, String data)
		throws KojiClientException;
	
	/**
	 * List all root tasks available on server as an Object array.
	 * 
	 * @return An Object array of all root tasks stored by the Koji server, null
	 *         if nothing is returned.
	 * 
	 * @throws KojiHubClientException
	 */
	public Object[] getAllRootTasksAsObjectArray() throws KojiClientException;
	
	/**
	 * List all root tasks available on server as a KojiTask list.
	 * 
	 * @return A list of all root tasks stored by the Koji server, null if nothing
	 *         is returned.
	 * 
	 * @throws KojiHubClientException
	 */
	public List<KojiTask> getAllRootTasksAsKojiTaskList()throws KojiClientException;
	
	/**
	 * Get a task's information based on its ID. Task information include:
	 * completion time (double), architecture, start time (double), creation
	 * time (double), task status code, channel ID, task ID, host ID, priority,
	 * start time (string), creation time (string) and completion time (string).
	 * 
	 * @param taskID
	 *            The task ID.
	 * @return A map with information about the task, null if nothing is returned.
	 * @throws KojiHubClientException
	 */
	public Map<String, ?> getTaskInfoByIDAsMap(int taskID) throws KojiClientException;
	
	/**
	 * Get a KojiTask object based on its ID. Task information include:
	 * completion time (double), architecture, start time (double), creation
	 * time (double), task status code, channel ID, task ID, host ID, priority,
	 * start time (string), creation time (string) and completion time (string).
	 * 
	 * @param taskID
	 *            The task ID.
	 * @return A KojiTask object with information about the task, null if nothing
	 *         is returned.
	 * @throws KojiHubClientException
	 */
	public KojiTask getTaskInfoByIDAsKojiTask(int taskID) throws KojiClientException;
	
	
	/**
	 * Get a Map<String, ?> of a task and its descendents based on the
	 * task's ID.
	 * 
	 * The key of the map is a String representing the task ID of the descendent and
	 * the value of the map is an Object[] representing the descendent's descendents.
	 * 
	 * NOTE: The map being returned also includes the task with the given task ID.
	 * 
	 * @param taskID The task ID.
	 * @return A map with a task's descendents, null if nothing is returned.
	 * @throws KojiHubClientException
	 */
	public Map<String, ?> getTaskDescendentsByIDAsMap(int taskID) throws KojiClientException;
	
	/**
	 * Get a channel's information based on its ID.
	 * Channel information include: channel ID and name.
	 * 
	 * @param channelID The channel ID.
	 * @return A Map with information about the channel, null if nothing is returned.
	 * @throws KojiHubClientException
	 */
	public Map<String,?> getChannelInfoByIDAsMap(int channelID) throws KojiClientException;
	
	/**
	 * Get a channel's information based on its ID. Channel information include:
	 * channel ID and name.
	 * 
	 * @param channelID
	 *            The channel ID.
	 * @return A KojiChannel object with information about the channel, null if
	 *         nothing is returned.
	 * @throws KojiHubClientException
	 */
	public KojiChannel getChannelInfoByIDAsKojiChannel(int channelID) throws KojiClientException;
	
	/**
	 * Get a user's information based on its ID.
	 * User information include: user ID, user status code, name and user type code.
	 * 
	 * @param userID The user ID.
	 * @return A KojiUser object with information about the user.
	 * @throws KojiHubClientException
	 */
	public Map<String, ?> getUserInfoByIDAsMap(int userID) throws KojiClientException;
	
	/**
	 * Get a user's information based on its ID. User information include: user
	 * ID, user status code, name and user type code.
	 * 
	 * @param userID
	 *            The user ID.
	 * @return A KojiUser object with information about the user, null if
	 *         nothing is returned.
	 * @throws KojiHubClientException
	 */
	public KojiUser getUserInfoByIDAsKojiUser(int userID) throws KojiClientException;
	
	/**
	 * Get a host's information based on its ID. Host information include: host
	 * ID, enable status (boolean), architecture, name, ready status (boolean).
	 * 
	 * @param hostID
	 *            The host ID.
	 * @return A KojiHost object with information about the host, null if
	 *         nothing is returned.
	 * @throws KojiHubClientException
	 */
	public Map<String, ?> getHostInfoByIDAsMap(int hostID) throws KojiClientException;
	
	/**
	 * Get a host's information based on its ID. Host information include: host
	 * ID, enable status (boolean), architecture, name, ready status (boolean).
	 * 
	 * @param hostID
	 *            The host ID.
	 * @return A KojiHost object with information about the host, null if
	 *         nothing is returned.
	 * @throws KojiHubClientException
	 */
	public KojiHost getHostInfoByIDAsKojiHost(int hostID) throws KojiClientException;
	
	/**
	 * Get a task's outputs including the task's log files and rpm files.  The output is
	 * a hash map where the keys of the map are the file names and value of each entry is a
	 * hash map containing the information (such as file size) of the file.  
	 * 
	 * TODO: Also insert the URL of rpm files into the information hash map.
	 * @param taskID The task ID.
	 * @return A hash map with information about the given task's outputs.
	 * @throws KojiClientException
	 */
	public HashMap<String, HashMap<String, Object>> listTaskOutputAsMap(int taskID) throws KojiClientException;
	
	/**
	 * Downloads the file content of an output file of a task, given the task ID and the file name.
	 * @param taskID The task ID.
	 * @param fileName The name of the output file.
	 * @param offset The starting offset of the file, 0 for downloading from the beginning.
	 * @param size The total amount of bytes to download.
	 * @return The string content of the file.
	 * @throws KojiClientException
	 */
	public String downloadTaskOutputAsString(int taskID, String fileName, int offset, int size) throws KojiClientException;
	
	/**
	 * Lists all packages available on the Koji server.
	 * @return An array of Maps containing data of packages (ID and names).
	 * @throws KojiClientException
	 */
	public Object[] listPackagesAsObjectArray() throws KojiClientException;
	
	/**
	 * Lists all packages available on the Koji server, does not query
	 * the packages' recent builds.
	 * @param Limit the amount of builds returned, -1 for no limit.
	 * @return	A list of KojiPackage objects representing all the packages
	 * available on the Koji server.
	 * @throws KojiClientException
	 */
	public List<KojiPackage> listPackagesAsKojiPackageList(int limit) throws KojiClientException, IllegalArgumentException;
	
	/**
	 * Gets a package as map by a given ID.
	 * @param packageID The package ID.
	 * @return	A hash map containing information about the package.
	 * @throws KojiClientException
	 */
	public Map<String, ?> getPackageByIDAsMap(int packageID) throws KojiClientException;
	
	/**
	 * Gets a package as KojiPackage object by a given ID.  Also queries Koji for
	 * its recent builds.  The amount of recent builds can be limited by limit, -1 for no
	 * limit.
	 * @param packageID	The package ID.
	 * @param limit The maximum amount of builds to be queried, -1 for no limit.
	 * @return	A KojiPackage object containing information about the package.
	 * @throws KojiClientException
	 */
	public KojiPackage getPackageByIDAsKojiPackage(int packageID, int limit) throws KojiClientException, IllegalArgumentException;
	
	/**
	 * Lists recent builds of a given package ID, amount can be limited by limit.
	 * @param packageID The ID of the package to be queried.
	 * @param Limit the maximum amount of builds to be queried, -1 for no limit.
	 * @return A list of KojiBuildInfo objects that belongs to the package.
	 * @throws KojiClientException
	 */
	public List<KojiBuildInfo> listBuildByKojiPackageIDAsList(int packageID, int limit) throws KojiClientException, IllegalArgumentException;
	
	/**
	 * Gets a map represent the source rpm by a build ID.  For use with description
	 * retrieving by package ID, which is contained by the map returned here.
	 * @param buildId The build ID.
	 * @return A map representing the source rpm.
	 * @throws KojiClientException
	 */
	public Map<String, Object> getSourceRPMFromBuildIdAsMap(int buildId) throws KojiClientException;
	
	/**
	 * Gets the description of the rpm by its package ID.
	 * @param packageId The package ID.
	 * @return The description string of the rpm.
	 * @throws KojiClientException
	 */
	public String getDescriptionFromPackageIdAsString(int packageId) throws KojiClientException;
	
	/**
	 * Gets the current session's information as a Map.
	 * @return The map containing the current session's information.
	 * @throws KojiClientException
	 */
	public Map<String, ?> getSessionInfoAsMap() throws KojiClientException;
	
	/**
	 * Lists all packages available, owned by the user, on the Koji server.
	 * @return An array of Maps containing data of packages (ID and names).
	 * @throws KojiClientException
	 */
	public Object[] listPackagesOfUserAsObjectArray() throws KojiClientException;
	
	/**
	 * Lists all packages available, owned by the user, on the Koji server, does not query
	 * the packages' recent builds.
	 * @param limit Limit the amount of builds returned, -1 for no limit.
	 * @return	A list of KojiPackage objects representing all the packages
	 * available on the Koji server.
	 * @throws KojiClientException
	 */
	public List<KojiPackage> listPackagesOfUserAsKojiPackageList(int limit) throws KojiClientException, IllegalArgumentException;
	
	/**
	 * Lists recent builds of a given package ID, owned by the user, amount can be limited by limit.
	 * @param packageID The ID of the package to be queried.
	 * @param limit The maximum amount of builds to be queried, -1 for no limit.
	 * @return A list of KojiBuildInfo objects that belongs to the package.
	 * @throws KojiClientException
	 */
	public List<KojiBuildInfo> listBuildOfUserByKojiPackageIDAsList(int packageID, int limit) throws KojiClientException, IllegalArgumentException;
	
	/**
	 * Gets the package by package name as map.
	 * @param name The package name.
	 * @Return A map containing the package ID and name.
	 */
	public Map<String, ?> getPackageByNameAsMap(String name) throws KojiClientException;
	
	/**
	 * Resubmits a canceled or failed task to koji with the same parameter as the original task.
	 * The user must be the owner of the task.
	 * @param taskID The task ID.
	 * @throws KojiClientException
	 */
	public void resubmitTask(int taskID) throws KojiClientException;
}