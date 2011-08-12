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
package org.eclipse.mylyn.koji.tests.units;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.mylyn.koji.client.api.IKojiHubClient;
import org.eclipse.mylyn.koji.client.api.KojiChannel;
import org.eclipse.mylyn.koji.client.api.KojiHost;
import org.eclipse.mylyn.koji.client.api.KojiSSLHubClient;
import org.eclipse.mylyn.koji.client.api.KojiTask;
import org.eclipse.mylyn.koji.client.api.KojiUser;
import org.eclipse.mylyn.koji.client.api.errors.KojiClientException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for the Koji hub client class. Note that for a successful run of
 * these tests, one has to have valid Fedora certificates in ~/
 * 
 */
public class KojiSSLHubClientTest {
	
	/**
	 * Name-version-release of some successful build.
	 */
	@SuppressWarnings("unused")
	private static final String EFP_NVR = "eclipse-fedorapackager-0.1.12-1.fc15";
	/**
	 * Some known to be working scm-URL
	 */
	@SuppressWarnings("unused")
	private static final String EFP_SCM_URL = "git://pkgs.fedoraproject.org/eclipse-fedorapackager.git?#302d36c1427a0d8578d0a1d88b4c9337a4407dde";

	private IKojiHubClient kojiClient;
	
	@Before
	public void setUp() throws Exception {
		// a bare SSL Koji client
		kojiClient = new KojiSSLHubClient("https://koji-toronto.usersys.redhat.com/kojihub");
	}

	@After
	public void tearDown() throws Exception {
		kojiClient.logout();
	}
	
	/**
	 * Log on to Koji using SSL authentication.
	 * This test requires proper certificates to be set up.
	 * 
	 * @throws Exception
	 */
	/*
	@Test
	public void canLoginUsingSSLCertificate() throws Exception {
		// Logging in to koji should return session data
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		assertNotNull(sessionData.get("session-id"));
		assertTrue(sessionData.get("session-id") instanceof Integer);
		assertNotNull(sessionData.get("session-key"));
	}*/
	
	/**
	 * Get build info test.
	 * 
	 * @throws Exception
	 */
	/*
	@Test
	public void canGetBuildInfo() throws Exception {
		// First log in
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// get build info for eclipse-fedorapackager-0.1.12-1.fc15
		KojiBuildInfo info = kojiClient.getBuild(EFP_NVR);
		assertNotNull(info);
		assertTrue(info.getRelease().equals("1.fc15"));
		assertTrue(info.getPackageName().equals("eclipse-fedorapackager"));
		assertTrue(info.getVersion().equals("0.1.12"));
		assertTrue(info.isComplete());
	}*/
	
	/**
	 * Push scratch build test.
	 * 
	 * @throws Exception
	 */
	/*
	@Test
	public void canPushScratchBuild() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// get build info for eclipse-fedorapackager-0.1.13-fc15
		boolean isScratchBuild = true;
		int taskId = kojiClient.build("dist-rawhide", EFP_SCM_URL, EFP_NVR, isScratchBuild);
		System.out.println("Pushed task ID: " + taskId);
		assertNotNull(taskId);
	}*/

	// Toronto Koji specific query test starts here.
	
	//Channel testing
	/**
	 * Query channel by ID as map, positive testing.
	 * 
	 * @throws Exception
	 */
	@Test
	@SuppressWarnings({ "unchecked" })
	public void testGetChannelInfoByIDAsMapPositive() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query channel ID 2
		Object channelMapObject = kojiClient.getChannelInfoByIDAsMap(2);
		assertNotNull(channelMapObject);
		assertTrue(channelMapObject instanceof Map);
		Map<String, ?> channelMap = (Map<String, ?>)channelMapObject;
		Object channelNameFromMap = channelMap.get("name");
		assertNotNull(channelNameFromMap);
		assertTrue(channelNameFromMap instanceof String);
		assertEquals(0, ((String)channelNameFromMap).compareToIgnoreCase("createrepo"));
		Object channelIDFromMap = channelMap.get("id");
		assertNotNull(channelIDFromMap);
		assertTrue(channelIDFromMap instanceof Integer);
		assertEquals(2, ((Integer)channelIDFromMap).intValue());
	}
	
	/**
	 * Query channel ID as map, negative testing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetChannelInfoByIDAsMapNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query channel ID -1
		Map<String, ?> channelMap = kojiClient.getChannelInfoByIDAsMap(-1);
		assertNull(channelMap);
	}
	
	/**
	 * Query channel by ID as KojiChannel, positive testing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetChannelInfoByIDAsKojiChannelPositive() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query channel ID 2
		KojiChannel channel = kojiClient.getChannelInfoByIDAsKojiChannel(2);
		assertNotNull(channel);
		assertEquals(2, channel.getId());
		assertNotNull(channel.getName());
		assertEquals(0, channel.getName().compareToIgnoreCase("createrepo"));
	}
	
	/**
	 * Query channel by ID as KojiChannel, negative testing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetChannelInfoByIDAsKojiChannelNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query channel ID -1
		KojiChannel channel = kojiClient.getChannelInfoByIDAsKojiChannel(-1);
		assertNull(channel);
	}
	
	//User testing
	/**
	 * Query user by ID as Map, positive testing
	 * 
	 * @throws Exception
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testGetUserInfoByIDAsMapPositive() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query channel ID 2
		Object userMapObject = kojiClient.getUserInfoByIDAsMap(2);
		assertNotNull(userMapObject);
		assertTrue(userMapObject instanceof Map);
		Map<String, ?> userMap = (Map<String, ?>)userMapObject;
		Object userID = userMap.get("id");
		assertNotNull(userID);
		assertTrue(userID instanceof Integer);
		assertEquals(2, ((Integer)userID).intValue());
		Object userName = userMap.get("name");
		assertNotNull(userName);
		assertTrue(userName instanceof String);
		assertEquals(0, ((String)userName).compareToIgnoreCase("kojiadmin"));
		//cannot test everything of the class...
	}
	
	/**
	 * Query user by ID as Map, negative testing
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetUserInfoByIDAsMapNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query channel ID -1
		Map<String, ?> userMap = kojiClient.getUserInfoByIDAsMap(-1);
		assertNull(userMap);
	}
	
	/**
	 * Query user by ID as KojiUser, positive testing
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetUserInfoByIDAsKojiUserPositive() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query channel ID 2
		KojiUser user = kojiClient.getUserInfoByIDAsKojiUser(2);
		assertNotNull(user);
		assertEquals(2, user.getId());
		assertNotNull(user.getName());
		assertEquals(0, user.getName().compareToIgnoreCase("kojiadmin"));
		//cannot test everything of the class...
	}
	
	/**
	 * Query user by ID as KojiUser, negative testing
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetUserInfoByIDAsKojiUserNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query channel ID -1
		KojiUser user = kojiClient.getUserInfoByIDAsKojiUser(-1);
		assertNull(user);
	}
	
	//Host testing
	/**
	 * Query host by ID as Map, positive testing
	 * 
	 * @throws Exception
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testGetHostInfoByIDAsMapPositive() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		//query host ID 1
		Object hostMapObject = kojiClient.getHostInfoByIDAsMap(1);
		assertNotNull(hostMapObject);
		assertTrue(hostMapObject instanceof Map);
		Map<String, ?> hostMap = (Map<String, ?>)hostMapObject;
		Object id = hostMap.get("id");
		assertNotNull(id);
		assertTrue(id instanceof Integer);
		assertEquals(1, ((Integer)id).intValue());
		Object name = hostMap.get("name");
		assertNotNull(name);
		assertTrue(name instanceof String);
		assertEquals(0, ((String)name).compareToIgnoreCase("kojibuilder1"));
		//cannot test everything of the class...
	}
	
	/**
	 * Query host by ID as Map, negative testing
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetHostInfoByIDAsMapNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query host ID -1
		Object hostMapObject = kojiClient.getHostInfoByIDAsMap(-1);
		assertNull(hostMapObject);
	}
	
	/**
	 * Query host by ID as KojiHost, positive testing
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetHostInfoByIDAsKojiHostPositive() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query host ID 1
		KojiHost host = kojiClient.getHostInfoByIDAsKojiHost(1);
		assertNotNull(host);
		assertEquals(1, host.getId());
		Object name = host.getName();
		assertNotNull(name);
		assertTrue(name instanceof String);
		assertEquals(0, ((String)name).compareToIgnoreCase("kojibuilder1"));
	}
	
	/**
	 * Query host by ID as KojiHost, negative testing
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetHostInfoByIDAsKojiHostNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query host ID -1
		KojiHost host = kojiClient.getHostInfoByIDAsKojiHost(-1);
		assertNull(host);
	}
	
	//Task testing
	/**
	 * Query all root task test.
	 * 
	 * @throws Exception
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testGetAllRootTasks() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query all root tasks
		Object rootTaskArrayObject = kojiClient.getAllRootTaskBuildsAsObjectArray();
		assertNotNull(rootTaskArrayObject);
		assertTrue(rootTaskArrayObject instanceof Object[]);
		Object[] rootTaskArray = (Object[])rootTaskArrayObject;
		int arraySize = rootTaskArray.length;
		assertTrue(arraySize > 0);
		assertTrue(rootTaskArray[0] instanceof Map);
		Object rootTaskList = kojiClient.getAllRootTaskBuildsAsKojiTaskList();
		assertNotNull(rootTaskList);
		assertTrue(rootTaskList instanceof List);
		int listSize = ((List<KojiTask>)rootTaskList).size();
		assertTrue(listSize > 0);
		assertEquals(arraySize, listSize);
	}
	
	/**
	 * Query task descendents, positive testing.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetTaskDescendentsByIDAsMapPositive() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		//query task 9391
		Object descendents = kojiClient.getTaskDescendentsByIDAsMap(8149);
		assertNotNull(descendents);
		assertTrue(descendents instanceof Map);
		Map<String, ?> descendentMap = (Map<String, ?>)descendents;
		assertTrue(descendentMap.size() == 1);
		descendents = descendentMap.get(Integer.toString(8149));
		assertNotNull(descendents);
		assertTrue(descendents instanceof Object[]);
		Object[] descendentArray = (Object[])descendents;
		assertEquals(2, descendentArray.length);
		assertTrue(descendentArray[0] instanceof Map);
		assertTrue(descendentArray[1] instanceof Map);
		Map<String, ?>descendent = (Map<String, ?>)descendentArray[0];
		int descendentID1 = ((Integer)descendent.get("id")).intValue();
		descendent = (Map<String, ?>)descendentArray[1];
		int descendentID2 = ((Integer)descendent.get("id")).intValue();
		assertTrue(descendentID1 != descendentID2);
		assertTrue((descendentID1 == 8150) || (descendentID1 == 8151));
		assertTrue((descendentID2 == 8150) || (descendentID2 == 8151));
	}
	
	/**
	 * Query task descendents, negative testing
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetTaskDescendentsByIDAsMapNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query task -1
		Object descendents = kojiClient.getTaskDescendentsByIDAsMap(-1);
		assertNotNull(descendents);
		assertTrue(descendents instanceof Map);
		assertNull(((Map<String, ?>)descendents).get("id"));
	}
	
	/**
	 * Query task by ID as Map, positive testing
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetTaskInfoByIDAsMapPositive() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query task 9391
		Object taskObject = kojiClient.getTaskInfoByIDAsMap(9391);
		assertNotNull(taskObject);
		assertTrue(taskObject instanceof Map);
		Map<String, ?> taskMap = (Map<String, ?>)taskObject;
		assertEquals(9391, ((Integer)taskMap.get("id")).intValue());
		assertNull(taskMap.get("parent"));
		Object taskArch = taskMap.get("arch");
		assertNotNull(taskArch);
		assertTrue(taskArch instanceof String);
		String architectureString = (String)taskArch;
		assertEquals(0, architectureString.compareToIgnoreCase("noarch"));
	}
	
	/**
	 * Query task by ID as Map, negative testing
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetTaskInfoByIDAsMapNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query task -1
		boolean exceptionThrown = false;
		try {
			@SuppressWarnings("unused")
			Object taskObject = kojiClient.getTaskInfoByIDAsMap(-1);
		} catch (KojiClientException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
	}
	
	/**
	 * Query task by ID as KojiTask, positive testing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetTaskInfoByIDAsKojiTaskPositive() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query task 8149
		Object taskObject = kojiClient.getTaskInfoByIDAsKojiTask(8149);
		assertNotNull(taskObject);
		assertTrue(taskObject instanceof KojiTask);
		KojiTask task = (KojiTask) taskObject;
		assertEquals(8149, task.getId());
		assertNull(task.getParentTask());
		assertEquals(0, task.getArchitecture().compareToIgnoreCase("noarch"));
		List<Integer> descendentIDList = task.getDescendentIDs();
		List<KojiTask> descendentList = task.getDescendents();
		assertEquals(descendentIDList.size(), descendentList.size());
		assertEquals(descendentIDList.size(), 2);// implies descendentList is
													// size of 2 too.
		//IDs stored by the lists are not the same
		assertTrue(descendentIDList.get(0).intValue() != descendentIDList
				.get(1).intValue());
		assertTrue(descendentList.get(0).getId() != descendentList.get(1)
				.getId());
		//The IDs from ID list and descendent list equal to either 8150
		//or 8151, and since they are not equal, one of them has to be 8151 and
		//the other has to be 8151
		int firstIDFromIDList = descendentIDList.get(0).intValue();
		assertTrue((firstIDFromIDList == 8150) || (firstIDFromIDList == 8151));
		int secondIDFromIDList = descendentIDList.get(1).intValue();
		assertTrue((secondIDFromIDList == 8150) || (secondIDFromIDList == 8151));
		int firstIDFromDescendentList = descendentList.get(0).getId();
		assertTrue((firstIDFromDescendentList == 8150) || (firstIDFromDescendentList == 8151));
		int secondIDFromDescendentList = descendentList.get(1).getId();
		assertTrue((secondIDFromDescendentList == 8150) || (secondIDFromDescendentList == 8151));
	}
	
	/**
	 * Query task by ID as KojiTask, negative testing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetTaskInfoByIDAsKojiTaskNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query task -1
		boolean exceptionThrown = false;
		try {
			@SuppressWarnings("unused")
			Object taskObject = kojiClient.getTaskInfoByIDAsKojiTask(-1);
		} catch (KojiClientException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
	}
	
	/**
	 * Query task outputs, positive testing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testListTaskOutputAsMapPositive() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query task 8150's output
		HashMap<String, HashMap<String, Object>> taskOutputs = kojiClient.listTaskOutputAsMap(8150);
		assertNotNull(taskOutputs);
		Set<String> keySet = taskOutputs.keySet();
		assertTrue(keySet.contains("build.log"));
		assertTrue(keySet.contains("mock_output.log"));
		assertTrue(keySet.contains("root.log"));
		assertTrue(keySet.contains("state.log"));
		assertTrue(keySet.contains("ed-1.5-2.fc15.src.rpm"));
		assertTrue(keySet.contains("ed-1.5-2.fc15.x86_64.rpm"));
		assertTrue(keySet.contains("ed-debuginfo-1.5-2.fc15.x86_64.rpm"));
	}
	
	/**
	 * Query task outputs, negative testing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testListTaskOutputAsMapNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		//query task -1
		HashMap<String, HashMap<String, Object>> taskOutputs = kojiClient.listTaskOutputAsMap(-1);
		assertEquals(0, taskOutputs.size());
	}
}
