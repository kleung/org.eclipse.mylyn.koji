/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc. - initial API and implementation
 *******************************************************************************/
package org.fedoraproject.eclipse.packager.tests.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.mylyn.koji.client.api.IKojiHubClient;
import org.eclipse.mylyn.koji.client.api.KojiBuildInfo;
import org.eclipse.mylyn.koji.client.api.KojiChannel;
import org.eclipse.mylyn.koji.client.api.KojiHost;
import org.eclipse.mylyn.koji.client.api.KojiPackage;
import org.eclipse.mylyn.koji.client.api.KojiTask;
import org.eclipse.mylyn.koji.client.api.KojiUser;
import org.eclipse.mylyn.koji.client.api.errors.KojiClientException;
import org.eclipse.mylyn.koji.client.api.errors.KojiLoginException;
import org.eclipse.mylyn.koji.client.internal.utils.KojiBuildInfoParsingUtility;

/**
 * Stub client, which essentially does nothing. Used for KojiBuildCommand
 * testing.
 *
 */
public class KojiGenericHubClientStub implements IKojiHubClient {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#login()
	 */
	@Override
	public HashMap<?, ?> login() throws KojiLoginException {
		// nothing
		return new HashMap<String, Object>();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#logout()
	 */
	@Override
	public void logout() throws KojiClientException {
		// nothing
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#build(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public int build(String target, String scmURL, String nvr, boolean scratch)
			throws KojiClientException {
		return 0xdead; // fake taskId
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#getBuild(java.lang.String)
	 */
	@Override
	public KojiBuildInfo getBuild(String unused) throws KojiClientException {
		Map<String, Object> rawBuildinfoMap = new HashMap<String, Object>();
		rawBuildinfoMap.put("state", new Integer(2));
		rawBuildinfoMap.put("task_id", new Integer(3333));
		rawBuildinfoMap.put("package_id", new Integer(9999));
		rawBuildinfoMap.put("package_name", "eclipse-fedorapackager");
		rawBuildinfoMap.put("epoch", new Integer(1));
		rawBuildinfoMap.put("version", "0.1.12");
		rawBuildinfoMap.put("release", "2.fc15");
		rawBuildinfoMap.put("nvr", "eclipse-fedorapackager-0.1.12-2.fc15");
		return KojiBuildInfoParsingUtility.parseBuild(rawBuildinfoMap, null);
	}

	/*
	 *  (non-Javadoc)
	 *  @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#getAllRootTasksAsObjectArray()
	 */
	@Override
	public Object[] getAllRootTasksAsObjectArray() throws KojiClientException {
		return null;
	}
	
	/*
	 *  (non-Javadoc)
	 *  @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#getAllRootTasksAsKojiTaskList()
	 */
	@Override
	public List<KojiTask> getAllRootTasksAsKojiTaskList()throws KojiClientException {
		return null;
	}
	
	/*
	 *  (non-Javadoc)
	 *  @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#getTaskInfoByIDAsMap(int)
	 */
	@Override
	public Map<String, ?> getTaskInfoByIDAsMap(int taskID) throws KojiClientException {
		return null;
	}
	
	/*
	 *  (non-Javadoc)
	 *  @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#getTaskInfoByIDAsKojiTask(int)
	 */
	@Override
	public KojiTask getTaskInfoByIDAsKojiTask(int taskID) throws KojiClientException {
		return null;
	}
	
	/*
	 *  (non-Javadoc)
	 *  @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#getTaskDescendentsByIDAsMap(int)
	 */
	@Override
	public Map<String, ?> getTaskDescendentsByIDAsMap(int taskID) throws KojiClientException {
		return null;
	}
	
	/*
	 *  (non-Javadoc)
	 *  @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#getChannelInfoByIDAsMap(int)
	 */
	@Override
	public Map<String,?> getChannelInfoByIDAsMap(int channelID) throws KojiClientException {
		return null;
	}
	
	/*
	 *  (non-Javadoc)
	 *  @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#getChannelInfoByIDAsKojiChannel(int)
	 */
	@Override
	public KojiChannel getChannelInfoByIDAsKojiChannel(int channelID) throws KojiClientException {
		return null;
	}
	
	/*
	 *  (non-Javadoc)
	 *  @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#getUserInfoByIDAsMap(int)
	 */
	@Override
	public Map<String, ?> getUserInfoByIDAsMap(int userID) throws KojiClientException {
		return null;
	}
	
	/*
	 *  (non-Javadoc)
	 *  @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#getUserInfoByIDAsKojiUser(int)
	 */
	@Override
	public KojiUser getUserInfoByIDAsKojiUser(int userID) throws KojiClientException {
		return null;
	}
	
	/*
	 *  (non-Javadoc)
	 *  @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#getHostInfoByIDAsMap(int)
	 */
	@Override
	public Map<String, ?> getHostInfoByIDAsMap(int hostID) throws KojiClientException {
		return null;
	}
	
	/*
	 *  (non-Javadoc)
	 *  @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#getHostInfoByIDAsKojiHost(int)
	 */
	@Override
	public KojiHost getHostInfoByIDAsKojiHost(int hostID) throws KojiClientException {
		return null;
	}
	
	/*
	 *  (non-Javadoc)
	 *  @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#listTaskOutputAsMap(int)
	 */
	@Override
	public HashMap<String, HashMap<String, Object>> listTaskOutputAsMap(int taskID) throws KojiClientException {
		return null;
	}
	
	/*
	 *  (non-Javadoc)
	 *  @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#downloadTaskOutputAsString(int, String, int, int)
	 */
	@Override
	public String downloadTaskOutputAsString(int taskID, String fileName, int offset, int size) throws KojiClientException {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#listPackagesAsObjectArray()
	 */
	@Override
	public Object[] listPackagesAsObjectArray() throws KojiClientException {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#listPackagesAsKojiPackageList()
	 */
	@Override
	public List<KojiPackage> listPackagesAsKojiPackageList() throws KojiClientException {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#getPackageByIDAsMap(int)
	 */
	@Override
	public Map<String, ?> getPackageByIDAsMap(int packageID) throws KojiClientException {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#getPackageByIDAsKojiPackage(int, int)
	 */
	@Override
	public KojiPackage getPackageByIDAsKojiPackage(int packageID, int limit) throws KojiClientException, IllegalArgumentException {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#listBuildByKojiPackageIDAsList(int, int)
	 */
	@Override
	public List<KojiBuildInfo> listBuildByKojiPackageIDAsList(int packageID, int limit) throws KojiClientException, IllegalArgumentException {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#getSourceRPMFromBuildIdAsMap(int)
	 */
	@Override
	public Map<String, Object> getSourceRPMFromBuildIdAsMap(int buildId)
			throws KojiClientException {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see org.eclipse.mylyn.koji.client.api.IKojiHubClient#getDescriptionFromPackageIdAsString(int)
	 */
	@Override
	public String getDescriptionFromPackageIdAsString(int packageId)
			throws KojiClientException {
		return null;
	}
	
}
