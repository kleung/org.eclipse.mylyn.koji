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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.eclipse.mylyn.koji.client.api.errors.KojiBuildExistException;
import org.eclipse.mylyn.koji.client.api.errors.KojiClientException;
import org.eclipse.mylyn.koji.client.api.errors.KojiLoginException;
import org.eclipse.mylyn.koji.client.internal.utils.KojiBuildInfoParsingUtility;
import org.eclipse.mylyn.koji.client.internal.utils.KojiChannelParsingUtility;
import org.eclipse.mylyn.koji.client.internal.utils.KojiHostParsingUtility;
import org.eclipse.mylyn.koji.client.internal.utils.KojiPackageParsingUtility;
import org.eclipse.mylyn.koji.client.internal.utils.KojiSessionInfoParsingUtility;
import org.eclipse.mylyn.koji.client.internal.utils.KojiTaskParsingUtility;
import org.eclipse.mylyn.koji.client.internal.utils.KojiTypeFactory;
import org.eclipse.mylyn.koji.client.internal.utils.KojiUserParsingUtility;
import org.eclipse.mylyn.koji.messages.KojiText;
import org.apache.commons.codec.binary.Base64;

/**
 * Koji Base client.
 */
public abstract class AbstractKojiHubBaseClient implements IKojiHubClient {

	protected Integer userID = null;
	
	/**
	 * Default constructor to set up a basic client.
	 * 
	 * @param kojiHubUrl
	 *            The koji hub URL.
	 * @throws MalformedURLException
	 *             If the hub URL was invalid.
	 */
	public AbstractKojiHubBaseClient(String kojiHubUrl)
			throws MalformedURLException {
		this.kojiHubUrl = new URL(kojiHubUrl);
		setupXmlRpcConfig();
		setupXmlRpcClient();
	}

	/**
	 * URL of the Koji Hub/XMLRPC interface
	 */
	protected URL kojiHubUrl;
	protected XmlRpcClientConfigImpl xmlRpcConfig;
	protected XmlRpcClient xmlRpcClient;

	/**
	 * Store session info in XMLRPC configuration.
	 * 
	 * @param sessionKey
	 * @param sessionID
	 */
	protected void saveSessionInfo(String sessionKey, String sessionID) {
		try {
			xmlRpcConfig.setServerURL(new URL(this.kojiHubUrl.toString()
					+ "?session-key=" + sessionKey //$NON-NLS-1$
					+ "&session-id=" + sessionID)); //$NON-NLS-1$
		} catch (MalformedURLException e) {
			// ignore, URL should be valid
		}
	}

	/**
	 * Discard session info previously stored in server URL via
	 * {@link AbstractKojiHubBaseClient#saveSessionInfo(String, String)}.
	 */
	protected void discardSession() {
		xmlRpcConfig.setServerURL(this.kojiHubUrl);
		this.userID = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fedoraproject.eclipse.packager.IKojiHubClient#sslLogin()
	 */
	@Override
	public abstract HashMap<?, ?> login() throws KojiLoginException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fedoraproject.eclipse.packager.IKojiHubClient#logout()
	 */
	@Override
	public void logout() throws KojiClientException {
		ArrayList<String> params = new ArrayList<String>();
		try {
			xmlRpcClient.execute("logout", params); //$NON-NLS-1$
		} catch (XmlRpcException e) {
			throw new KojiClientException(e);
		}
		discardSession();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.fedoraproject.eclipse.packager.koji.api.IKojiHubClient#build(java
	 * .lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public int build(String target, String scmURL, String nvr, boolean scratch)
			throws KojiClientException {
		if(this.userID == null)
			this.userID = KojiSessionInfoParsingUtility.getUserID(this.getSessionInfoAsMap());
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(scmURL);
		params.add(target);
		if (scratch) {
			Map<String, Boolean> scratchParam = new HashMap<String, Boolean>();
			scratchParam.put("scratch", true); //$NON-NLS-1$
			params.add(scratchParam);
		} else if (nvr != null) {
			KojiBuildInfo buildInfo = getBuild(nvr);
			if (buildInfo != null && buildInfo.isComplete()) {
				throw new KojiBuildExistException(buildInfo.getTaskId());
			}
		}
		Object result;
		try {
			result = xmlRpcClient.execute("build", params); //$NON-NLS-1$
		} catch (XmlRpcException e) {
			throw new KojiClientException(e);
		}
		int taskId;
		try {
			taskId = Integer.parseInt(result.toString());
		} catch (NumberFormatException e) {
			// no task ID returned, some other error must have happened.
			throw new KojiClientException(result.toString());
		}
		return taskId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.fedoraproject.eclipse.packager.koji.api.IKojiHubClient#getBuild(java
	 * .lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public KojiBuildInfo getBuild(String nvr) throws KojiClientException {
		if(this.userID == null)
			this.userID = KojiSessionInfoParsingUtility.getUserID(this.getSessionInfoAsMap());
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(nvr);
		Map<String, Object> rawBuildInfo;
		try {
			rawBuildInfo = (Map<String, Object>) xmlRpcClient.execute(
					"getBuild", params); //$NON-NLS-1$
		} catch (XmlRpcException e) {
			throw new KojiClientException(e);
		}
		if (rawBuildInfo != null) {
			return KojiBuildInfoParsingUtility.parseBuild(rawBuildInfo, this);
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.fedoraproject.eclipse.packager.koji.api.IKojiHubClient#uploadFile(java.lang.String, java.lang.String, int, java.lang.String, int, java.lang.String)
	 */
	@Override
	public boolean uploadFile(String path, String name, int size, String md5sum, int offset, String data)
		throws KojiClientException {
		if(this.userID == null)
			this.userID = KojiSessionInfoParsingUtility.getUserID(this.getSessionInfoAsMap());
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(path);
		params.add(name);
		params.add(size);
		params.add(md5sum);
		params.add(offset);
		params.add(data);
		Object result;
		try {
			result = xmlRpcClient.execute("uploadFile", params); //$NON-NLS-1$
		} catch (XmlRpcException e) {
			throw new KojiClientException(e);
		}
		boolean success = Boolean.parseBoolean(result.toString());
		return success;
	}
	
	/**
	 * Configure XMLRPC connection
	 */
	protected void setupXmlRpcConfig() {
		xmlRpcConfig = new XmlRpcClientConfigImpl();
		xmlRpcConfig.setServerURL(this.kojiHubUrl);
		xmlRpcConfig.setEnabledForExtensions(true);
		xmlRpcConfig.setConnectionTimeout(30000);
	}

	/**
	 * Set up XMLRPC client.
	 * 
	 * @throws IllegalStateException
	 *             If XMLRPC configuration hasn't been properly set up.
	 */
	protected void setupXmlRpcClient() throws IllegalStateException {
		if (xmlRpcConfig == null) {
			throw new IllegalStateException(KojiText.xmlRPCconfigNotInitialized);
		}
		xmlRpcClient = new XmlRpcClient();
		xmlRpcClient.setTypeFactory(new KojiTypeFactory(this.xmlRpcClient));
		xmlRpcClient.setConfig(this.xmlRpcConfig);
	}

	/**
	 * List all root tasks available on server as an Object array.
	 * 
	 * @return An Object array of all root tasks stored by the Koji server, null
	 *         if nothing is returned.
	 * 
	 * @throws KojiClientException
	 */
	@Override
	public Object[] getAllRootTasksAsObjectArray()
			throws KojiClientException {
		if(this.userID == null)
			this.userID = KojiSessionInfoParsingUtility.getUserID(this.getSessionInfoAsMap());
		ArrayList<Object> params = new ArrayList<Object>();
		HashMap<String, Object> optsParam = new HashMap<String, Object>();
		HashMap<String, Object> opts = new HashMap<String, Object>();
		optsParam.put("__starstar", new Boolean(true)); //$NON-NLS-1$
		opts.put("method", "build"); //$NON-NLS-1$ //$NON-NLS-2$
		opts.put("decode", new Boolean(true)); //$NON-NLS-1$
		optsParam.put("opts", opts); //$NON-NLS-1$
		params.add(optsParam);
		ArrayList<Object> taskMapList = new ArrayList<Object>();
		Object[] webMethodResult = null;
		try {
			webMethodResult = (Object[]) xmlRpcClient.execute("listTasks", //$NON-NLS-1$
					params);
		} catch (XmlRpcException e) {
			throw new KojiClientException(e);
		}
		if ((webMethodResult != null) && (webMethodResult.length > 0)){
			// only getting task roots
			for (Object o : webMethodResult) {
				@SuppressWarnings("unchecked")
				Map<String, ?> map = (Map<String, ?>) o;
				if (map.get("parent") == null) //$NON-NLS-1$
					taskMapList.add(map);
			}
			webMethodResult = taskMapList.toArray();
			return webMethodResult;
		} else {
			return null;
		}
	}

	/**
	 * List all root tasks available on server as a KojiTask list.
	 * 
	 * @return A list of all root tasks stored by the Koji server, null if
	 *         nothing is returned.
	 * 
	 * @throws KojiClientException
	 */
	@Override
	public List<KojiTask> getAllRootTasksAsKojiTaskList()
			throws KojiClientException {
		Object[] taskArray = this.getAllRootTasksAsObjectArray();
		if (taskArray == null)
			return null;
		else
			return KojiTaskParsingUtility.parseTaskArray(taskArray, this);
	}

	/**
	 * Get a task's information based on its ID. Task information include:
	 * completion time (double), architecture, start time (double), creation
	 * time (double), task status code, channel ID, task ID, host ID, priority,
	 * start time (string), creation time (string) and completion time (string).
	 * 
	 * @param taskID
	 *            The task ID.
	 * @return A map with information about the task, null if nothing is
	 *         returned.
	 * @throws KojiClientException
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, ?> getTaskInfoByIDAsMap(int taskID)
			throws KojiClientException {
		if(this.userID == null)
			this.userID = KojiSessionInfoParsingUtility.getUserID(this.getSessionInfoAsMap());
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new Integer(taskID));
		HashMap<String, Object> optParam = new HashMap<String, Object>();
		optParam.put("__starstar", new Boolean(true)); //$NON-NLS-1$
		optParam.put("request", new Boolean(true)); //$NON-NLS-1$
		params.add(optParam);
		Map<String, ?> webMethodResult = null;
		try {
			webMethodResult = (Map<String, ?>) xmlRpcClient.execute(
					"getTaskInfo", params); //$NON-NLS-1$
		} catch (XmlRpcException e) {
			if(e.getMessage().contains("query returned no rows"))
				webMethodResult = null;
			else
				throw new KojiClientException(e);
		}
		if((webMethodResult != null) && (webMethodResult.size() > 0))
			return webMethodResult;
		else
			return null;
	}

	/**
	 * Get a KojiTask object based on its ID. Task information include:
	 * completion time (double), architecture, start time (double), creation
	 * time (double), task status code, channel ID, task ID, host ID, priority,
	 * start time (string), creation time (string) and completion time (string).
	 * 
	 * @param taskID
	 *            The task ID.
	 * @return A KojiTask object with information about the task, null if
	 *         nothing is returned.
	 * @throws KojiClientException
	 */
	@Override
	public KojiTask getTaskInfoByIDAsKojiTask(int taskID)
			throws KojiClientException {
		Map<String, ?> map = this.getTaskInfoByIDAsMap(taskID);
		if (map != null)
			return KojiTaskParsingUtility.parseTask(map, this, true);
		else
			return null;
	}

	/**
	 * Get a Map<String, Object[]> of a task and its descendents based on the
	 * task's ID.
	 * 
	 * The key of the map is a String representing the task ID of the descendent
	 * and the value of the map is an Object[] representing the descendent's
	 * descendents.
	 * 
	 * NOTE: The map being returned also includes the task with the given task
	 * ID.
	 * 
	 * @param taskID
	 *            The task ID.
	 * @return A map with a task's descendents, null if nothing is returned.
	 * @throws KojiClientException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, ?> getTaskDescendentsByIDAsMap(int taskID)
			throws KojiClientException {
		if(this.userID == null)
			this.userID = KojiSessionInfoParsingUtility.getUserID(this.getSessionInfoAsMap());
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new Integer(taskID));
		HashMap<String, Object> optsParam = new HashMap<String, Object>();
		optsParam.put("__starstar", new Boolean(true)); //$NON-NLS-1$
		optsParam.put("request", new Boolean(true)); //$NON-NLS-1$
		params.add(optsParam);
		Map<String, ?> webMethodResult = null;
		try {
			webMethodResult = (Map<String, ?>) xmlRpcClient.execute(
					"getTaskDescendents", params);//$NON-NLS-1$
		} catch (XmlRpcException e) {
			throw new KojiClientException(e);
		}
		if ((webMethodResult != null) && (webMethodResult.size() > 0)) {
			String targetParentTaskID = Integer.toString(taskID);
			// Since Koji hub returns a hash map of taskID/task descendents data
			// hash map pairs,
			// we can simply remove every key/value pair in the webmethod result
			// exception the
			// the one with the given task ID key.
			Set<String> keySet = webMethodResult.keySet();
			ArrayList<String> keyList = new ArrayList<String>();
			for (String id : keySet) {
				if (!id.equalsIgnoreCase(targetParentTaskID))
					keyList.add(id);
			}
			for(String id : keyList)
				webMethodResult.remove(id);
			if(((Object[])webMethodResult.get(targetParentTaskID)).length == 0)
				webMethodResult = null;
		} else
			webMethodResult = null;
		return webMethodResult;
	}

	/**
	 * Get a channel's information based on its ID. Channel information include:
	 * channel ID and name.
	 * 
	 * @param channelID
	 *            The channel ID.
	 * @return A map with information about the channel, null if
	 *         nothing is returned.
	 * @throws KojiClientException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, ?> getChannelInfoByIDAsMap(int channelID)
			throws KojiClientException {
		if(this.userID == null)
			this.userID = KojiSessionInfoParsingUtility.getUserID(this.getSessionInfoAsMap());
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new Integer(channelID));
		Map<String, ?> webMethodResult = null;
		try {
			webMethodResult = (Map<String, ?>) xmlRpcClient.execute(
					"getChannel", params);//$NON-NLS-1$
		} catch (XmlRpcException e) {
			throw new KojiClientException(e);
		}
		if ((webMethodResult != null) && (webMethodResult.size() > 0))
			return webMethodResult;
		else
			return null;
	}

	/**
	 * Get a channel's information based on its ID. Channel information include:
	 * channel ID and name.
	 * 
	 * @param channelID
	 *            The channel ID.
	 * @return A KojiChannel object with information about the channel, null if
	 *         nothing is returned.
	 * @throws KojiClientException
	 */
	@Override
	public KojiChannel getChannelInfoByIDAsKojiChannel(int channelID)
			throws KojiClientException {
		Map<String, ?> channelMap = this.getChannelInfoByIDAsMap(channelID);
		if (channelMap != null)
			return KojiChannelParsingUtility.parseChannel(channelMap);
		else
			return null;
	}

	/**
	 * Get a user's information based on its ID. User information include: user
	 * ID, user status code, name and user type code.
	 * 
	 * @param userID
	 *            The user ID.
	 * @return A map with information about the user, null if
	 *         nothing is returned.
	 * @throws KojiClientException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, ?> getUserInfoByIDAsMap(int userID) throws KojiClientException {
		if(this.userID == null)
			this.userID = KojiSessionInfoParsingUtility.getUserID(this.getSessionInfoAsMap());
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new Integer(userID));
		Map<String, ?> webMethodResult = null;
		try {
			webMethodResult = (Map<String, ?>) xmlRpcClient.execute(
					"getUser", params);//$NON-NLS-1$
		} catch (XmlRpcException e) {
			throw new KojiClientException(e);
		}
		if ((webMethodResult != null) && (webMethodResult.size() > 0))
			return webMethodResult;
		else
			return null;
	}
	
	/**
	 * Get a user's information based on its ID. User information include: user
	 * ID, user status code, name and user type code.
	 * 
	 * @param userID
	 *            The user ID.
	 * @return A KojiUser object with information about the user, null if
	 *         nothing is returned.
	 * @throws KojiClientException
	 */
	@Override
	public KojiUser getUserInfoByIDAsKojiUser(int userID) throws KojiClientException {
		Map<String, ?> userMap = this.getUserInfoByIDAsMap(userID);
		if(userMap != null)
			return KojiUserParsingUtility.parseUser(userMap);
		else
			return null;
	}

	/**
	 * Get a host's information based on its ID. Host information include: host
	 * ID, enable status (boolean), architecture, name, ready status (boolean).
	 * 
	 * @param hostID
	 *            The host ID.
	 * @return A map with information about the host, null if nothing is returned.
	 * @throws KojiClientException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, ?> getHostInfoByIDAsMap(int hostID) throws KojiClientException {
		if(this.userID == null)
			this.userID = KojiSessionInfoParsingUtility.getUserID(this.getSessionInfoAsMap());
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new Integer(hostID));
		Map<String, ?> webMethodResult = null;
		try {
			webMethodResult = (Map<String, ?>) xmlRpcClient.execute(
					"getHost", params);//$NON-NLS-1$
		} catch (XmlRpcException e) {
			throw new KojiClientException(e);
		}
		if ((webMethodResult != null) && (webMethodResult.size() > 0))
			return webMethodResult;
		else
			return null;
	}
	
	/**
	 * Get a host's information based on its ID. Host information include: host
	 * ID, enable status (boolean), architecture, name, ready status (boolean).
	 * 
	 * @param hostID
	 *            The host ID.
	 * @return A KojiHost object with information about the host, null if
	 *         nothing is returned.
	 * @throws KojiClientException
	 */
	@Override
	public KojiHost getHostInfoByIDAsKojiHost(int hostID) throws KojiClientException {
		Map<String, ?> hostMap = this.getHostInfoByIDAsMap(hostID);
		if(hostMap != null)
			return KojiHostParsingUtility.parseHost(hostMap, this);
		else
			return null;
	}
	
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
	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, HashMap<String, Object>> listTaskOutputAsMap(int taskID) throws KojiClientException {
		if(this.userID == null)
			this.userID = KojiSessionInfoParsingUtility.getUserID(this.getSessionInfoAsMap());
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new Integer(taskID));
		HashMap<String, Object> optsParam = new HashMap<String,Object>();
		optsParam.put("__starstar", new Boolean(true));//$NON-NLS-1$
		optsParam.put("stat", new Boolean(true));//$NON-NLS-1$
		params.add(optsParam);
		HashMap<String, HashMap<String, Object>> webMethodResult = null;
		try {
			webMethodResult = (HashMap<String, HashMap<String, Object>>) xmlRpcClient.execute(
					"listTaskOutput", params);//$NON-NLS-1$
		} catch (XmlRpcException e) {
			throw new KojiClientException(e);
		}
		if((webMethodResult != null) && (webMethodResult.size() > 0))
			return webMethodResult;
		else
			return null;
	}
	
	/**
	 * Downloads the file content of an output file of a task, given the task ID and the file name.
	 * @param taskID The task ID.
	 * @param fileName The name of the output file.
	 * @return The string content of the file.
	 * @throws KojiClientException
	 * @param offset The starting offset of the file, 0 for downloading from the beginning.
	 * @param size The total amount of bytes to download.
	 */
	public String downloadTaskOutputAsString(int taskID, String fileName, int offset, int size) throws KojiClientException {
		if(this.userID == null)
			this.userID = KojiSessionInfoParsingUtility.getUserID(this.getSessionInfoAsMap());
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new Integer(taskID));
		params.add(new String(fileName));
		params.add(new Integer(offset));
		params.add(new Integer(size));
		String webMethodResult = null;
		try {
			webMethodResult = (String)xmlRpcClient.execute(
					"downloadTaskOutput", params);//$NON-NLS-1$
		} catch (XmlRpcException e) {
			if(e.getMessage().contains("no file"))
				webMethodResult = null;
			else
				throw new KojiClientException(e);
		}
		if(webMethodResult != null)
			return new String(Base64.decodeBase64(webMethodResult.getBytes()));
		else
			return null;
	}
	
	/**
	 * Lists all packages available on the Koji server.
	 * @return An array of Maps containning data of packages (ID and names).
	 * @throws KojiClientException
	 */
	public Object[] listPackagesAsObjectArray() throws KojiClientException {
		if(this.userID == null)
			this.userID = KojiSessionInfoParsingUtility.getUserID(this.getSessionInfoAsMap());
		Object[] webMethodResult = null;
		ArrayList<Object> filterParams = new ArrayList<Object>();
		filterParams.add("listPackages");
		HashMap<String, Object> optsParam = new HashMap<String, Object>();
		optsParam.put("__starstar", new Boolean(true));
		HashMap<String, Object> filterOpts = new HashMap<String, Object>();
		filterOpts.put("order", "package_name");
		optsParam.put("filterOpts", filterOpts);
		filterParams.add(optsParam);
		try {
			webMethodResult = (Object[])xmlRpcClient.execute("filterResults", filterParams);	
		} catch (XmlRpcException e) {
			throw new KojiClientException();
		}
		if((webMethodResult != null) && (webMethodResult.length > 0))
			return webMethodResult;
		else
			return null;
	}
	
	/**
	 * Lists all packages available on the Koji server, does not query
	 * the packages' recent builds.
	 * @return	A list of KojiPackage objects representing all the packages
	 * available on the Koji server.
	 * @throws KojiClientException
	 */
	public List<KojiPackage> listPackagesAsKojiPackageList() throws KojiClientException {
		Object[] packageArray = this.listPackagesAsObjectArray();
		if(packageArray != null)
			return KojiPackageParsingUtility.parsePackageArrayAsKojiPackageList(packageArray);
		else
			return null;
	}
	
	
	/**
	 * Gets a package as map by a given ID.
	 * @param packageID The package ID.
	 * @return	A hash map containing information about the package.
	 * @throws KojiClientException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, ?> getPackageByIDAsMap(int packageID) throws KojiClientException {
		if(this.userID == null)
			this.userID = KojiSessionInfoParsingUtility.getUserID(this.getSessionInfoAsMap());
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new Integer(packageID));
		Map<String, ?> webMethodResult = null;
		try {
			webMethodResult = (HashMap<String, ?>)xmlRpcClient.execute(
					"getPackage", params);//$NON-NLS-1$
		} catch (XmlRpcException e) {
			throw new KojiClientException(e);
		}
		if((webMethodResult != null) && (webMethodResult.size() > 0))
			return webMethodResult;
		else
			return null;
	}
	
	/**
	 * Gets a package as KojiPackage object by a given ID.  Also queries Koji for
	 * its recent builds.  The amount of recent builds can be limited by limit, -1 for no
	 * limit.
	 * @param packageID	The package ID.
	 * @param limit The maximum amount of builds to be queried, -1 for no limit.
	 * @return	A KojiPackage object containing information about the package.
	 * @throws KojiClientException
	 */
	public KojiPackage getPackageByIDAsKojiPackage(int packageID, int limit) throws KojiClientException, IllegalArgumentException {
		if((limit < -1) || (limit == 0))
			throw new IllegalArgumentException();
		Map<String, ?> packageMap = this.getPackageByIDAsMap(packageID);
		if(packageMap != null)
			return KojiPackageParsingUtility.parsePackage(packageMap, true, this, limit, false);
		else
			return null;
	}
	/**
	 * Lists recent builds of a given package ID, amount can be limited by limit.
	 * @param packageID The ID of the package to be queried.
	 * @param limit The maximum amount of builds to be queried, -1 for no limit.
	 * @return A list of KojiBuildInfo objects that belongs to the package.
	 * @throws KojiClientException
	 * @throws IllegalArgumentException Thrown if limit is not bigger than 0 or -1.
	 */
	@SuppressWarnings("unchecked")
	public List<KojiBuildInfo> listBuildByKojiPackageIDAsList(int packageID, int limit) throws KojiClientException, IllegalArgumentException {
		if(this.userID == null)
			this.userID = KojiSessionInfoParsingUtility.getUserID(this.getSessionInfoAsMap());
		if((limit == 0) || (limit < -1))
			throw new IllegalArgumentException();
		LinkedList<KojiBuildInfo> buildList = new LinkedList<KojiBuildInfo>();
		ArrayList<Object>params = new ArrayList<Object>();
		HashMap<String, Object> optsParam = new HashMap<String, Object>();
		optsParam.put("__starstar", new Boolean(true));
		optsParam.put("packageID", new Integer(packageID));
		HashMap<String, Object> queryOpts = new HashMap<String, Object>();
		queryOpts.put("order", "-creation_time");
		if(limit != -1)
			queryOpts.put("limit", new Integer(limit));
		optsParam.put("queryOpts", queryOpts);
		params.add(optsParam);
		Object[] webMethodResult = null;
		try {
			webMethodResult = (Object[])xmlRpcClient.execute("listBuilds", params);
		}catch (XmlRpcException e) {
			throw new KojiClientException(e);
		}
		if((webMethodResult != null) && (webMethodResult.length > 0)) {
			for(Object o : webMethodResult) {
				if((o != null) && (o instanceof Map)) {
					Map<String, Object> buildMap = (Map<String, Object>)o;
					buildList.add(KojiBuildInfoParsingUtility.parseBuild(buildMap, this));
				}
			}
		}
		return buildList;
	}
	
	/**
	 * Gets a map represent the source rpm by a build ID.  For use with description
	 * retrieving by package ID, which is contained by the map returned here.
	 * @param buildId The build ID.
	 * @return A map representing the source rpm.
	 * @throws KojiClientException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSourceRPMFromBuildIdAsMap(int buildId) throws KojiClientException {
		if(this.userID == null)
			this.userID = KojiSessionInfoParsingUtility.getUserID(this.getSessionInfoAsMap());
		ArrayList<Object>params = new ArrayList<Object>();
		params.add(new Integer(buildId));
		Object[] webMethodResult = null;
		try {
			webMethodResult = (Object[])xmlRpcClient.execute("listBuildRPMs", params);
		} catch (XmlRpcException e) {
			throw new KojiClientException(e);
		}
		if((webMethodResult != null) && (webMethodResult.length > 0)) {
			//Select only the source rpm
			for(Object o : webMethodResult) {
				Map<String, Object> tempMap = (Map<String, Object>)o;
				if(tempMap.containsValue("src"))
					return tempMap;
			}//should not happen as the srpm is not found...
			return null;
		}else
			return null;
	}
	
	/**
	 * Gets the description of the rpm by its package ID.
	 * @param packageId The package ID.
	 * @return The description string of the rpm.
	 * @throws KojiClientException
	 */
	@SuppressWarnings("unchecked")
	public String getDescriptionFromPackageIdAsString(int packageId) throws KojiClientException {
		if(this.userID == null)
			this.userID = KojiSessionInfoParsingUtility.getUserID(this.getSessionInfoAsMap());
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new Integer(packageId));
		HashMap<String, Object> optsParam = new HashMap<String, Object>();
		ArrayList<String> rpmHeaders = new ArrayList<String>();
		optsParam.put("__starstar", new Boolean(true));
		rpmHeaders.add("description");
		optsParam.put("headers",rpmHeaders);
		params.add(optsParam);
		Map<String, String> webMethodResult = null;
		try {
			webMethodResult = (Map<String, String>)xmlRpcClient.execute("getRPMHeaders", params);
		} catch (XmlRpcException e) {
			throw new KojiClientException(e);
		}
		if((webMethodResult != null) && (webMethodResult.size() > 0)) {
			if(webMethodResult.containsKey("description"))
				return webMethodResult.get("description");
			else
				return null;
		}else
			return null;
	}
	
	/**
	 * Gets the current session's information as a Map.
	 * @return The map containing the current session's information.
	 * @throws KojiClientException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, ?> getSessionInfoAsMap() throws KojiClientException {
		ArrayList<Object> params = new ArrayList<Object>();
		Map<String, ?> webMethodResult = null;
		try {
			webMethodResult = (Map<String, ?>)xmlRpcClient.execute("getSessionInfo", params);
		} catch (XmlRpcException e) {
			throw new KojiClientException(e);
		}
		return webMethodResult;
	}
	
	/**
	 * Lists all packages available, owned by the user, on the Koji server.
	 * @return An array of Maps containing data of packages (ID and names).
	 * @throws KojiClientException
	 */
	public Object[] listPackagesOfUserAsObjectArray() throws KojiClientException {
		if(this.userID == null)
			this.userID = KojiSessionInfoParsingUtility.getUserID(this.getSessionInfoAsMap());
		Object[] webMethodResult = null;
		ArrayList<Object> filterParams = new ArrayList<Object>();
		filterParams.add("listPackages");
		HashMap<String, Object> filterOptsParam = new HashMap<String, Object>();
		filterOptsParam.put("__starstar", new Boolean(true));
		filterOptsParam.put("userID", this.userID);
		HashMap<String, Object> filterOpts = new HashMap<String, Object>();
		filterOpts.put("order", "package_name");
		filterOptsParam.put("filterOpts", filterOpts);
		filterParams.add(filterOptsParam);
		try {
			webMethodResult = (Object[])xmlRpcClient.execute("filterResults", filterParams);	
		} catch (XmlRpcException e) {
			throw new KojiClientException();
		}
		if((webMethodResult != null) && (webMethodResult.length > 0))
			return webMethodResult;
		else
			return null;
	}
	
	/**
	 * Lists all packages available, owned by the user, on the Koji server, does not query
	 * the packages' recent builds.
	 * @return	A list of KojiPackage objects representing all the packages
	 * available on the Koji server.
	 * @throws KojiClientException
	 */
	public List<KojiPackage> listPackagesOfUserAsKojiPackageList() throws KojiClientException {
		Object[] packageArray = this.listPackagesOfUserAsObjectArray();
		if(packageArray != null)
			return KojiPackageParsingUtility.parsePackageArrayAsKojiPackageList(packageArray);
		else
			return null;
	}
	
	/**
	 * Lists recent builds of a given package ID, owned by the user, amount can be limited by limit.
	 * @param packageID The ID of the package to be queried.
	 * @param limit The maximum amount of builds to be queried, -1 for no limit.
	 * @return A list of KojiBuildInfo objects that belongs to the package.
	 * @throws KojiClientException
	 */
	@SuppressWarnings("unchecked")
	public List<KojiBuildInfo> listBuildOfUserByKojiPackageIDAsList(int packageID, int limit) throws KojiClientException, IllegalArgumentException {
		if(this.userID == null)
			this.userID = KojiSessionInfoParsingUtility.getUserID(this.getSessionInfoAsMap());
		if((limit == 0) || (limit < -1))
			throw new IllegalArgumentException();
		LinkedList<KojiBuildInfo> buildList = new LinkedList<KojiBuildInfo>();
		ArrayList<Object>params = new ArrayList<Object>();
		HashMap<String, Object> optsParam = new HashMap<String, Object>();
		optsParam.put("__starstar", new Boolean(true));
		optsParam.put("packageID", new Integer(packageID));
		optsParam.put("userID", this.userID);
		HashMap<String, Object> queryOpts = new HashMap<String, Object>();
		queryOpts.put("order", "-creation_time");
		if(limit != -1)
			queryOpts.put("limit", new Integer(limit));
		optsParam.put("queryOpts", queryOpts);
		params.add(optsParam);
		Object[] webMethodResult = null;
		try {
			webMethodResult = (Object[])xmlRpcClient.execute("listBuilds", params);
		}catch (XmlRpcException e) {
			throw new KojiClientException(e);
		}
		if((webMethodResult != null) && (webMethodResult.length > 0)) {
			for(Object o : webMethodResult) {
				if((o != null) && (o instanceof Map)) {
					Map<String, Object> buildMap = (Map<String, Object>)o;
					buildList.add(KojiBuildInfoParsingUtility.parseBuild(buildMap, this));
				}
			}
		}
		return buildList;
	}
	
}
