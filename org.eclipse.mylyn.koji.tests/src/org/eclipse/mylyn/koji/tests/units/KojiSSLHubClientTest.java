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
import org.eclipse.mylyn.koji.client.api.KojiBuildInfo;
import org.eclipse.mylyn.koji.client.api.KojiChannel;
import org.eclipse.mylyn.koji.client.api.KojiHost;
import org.eclipse.mylyn.koji.client.api.KojiPackage;
import org.eclipse.mylyn.koji.client.api.KojiSSLHubClient;
import org.eclipse.mylyn.koji.client.api.KojiTask;
import org.eclipse.mylyn.koji.client.api.KojiUser;
import org.eclipse.mylyn.koji.client.internal.utils.KojiSessionInfoParsingUtility;
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
	private static final String EFP_NVR = "eclipse-fedorapackager-0.1.12-1.fc15";
	/**
	 * Some known to be working scm-URL
	 */
	private static final String EFP_SCM_URL = "git://pkgs.fedoraproject.org/eclipse-fedorapackager.git?#302d36c1427a0d8578d0a1d88b4c9337a4407dde";

	private int testNum = 1;
	
	private IKojiHubClient kojiClient;
	
	@Before
	public void setUp() throws Exception {
		// a bare SSL Koji client
		if(testNum < 4)
			kojiClient = new KojiSSLHubClient("https://koji.fedoraproject.org/kojihub");
		else
			kojiClient = new KojiSSLHubClient("https://koji-toronto.usersys.redhat.com/kojihub");
	}

	@After
	public void tearDown() throws Exception {
		kojiClient.logout();
		testNum++;
	}
	
	/**
	 * 1. Log on to Koji using SSL authentication.
	 * This test requires proper certificates to be set up.
	 * 
	 * @throws Exception
	 */
	@Test
	public void canLoginUsingSSLCertificate() throws Exception {
		// Logging in to koji should return session data
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		assertNotNull(sessionData.get("session-id"));
		assertTrue(sessionData.get("session-id") instanceof Integer);
		assertNotNull(sessionData.get("session-key"));
	}
	
	/**
	 * 2. Get build info test.
	 * 
	 * @throws Exception
	 */
	
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
	}
	
	/**
	 * 3. Push scratch build test.
	 * 
	 * @throws Exception
	 */
	
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
	}
	
	// Toronto Koji specific query test starts here.
	/**
	 * 4. Query channel by ID as map, positive testing.
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
	 * 5. Query channel ID as map, negative testing.
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
	 * 6. Query channel by ID as KojiChannel, positive testing.
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
	 * 7. Query channel by ID as KojiChannel, negative testing.
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
	
	/**
	 * 8. Query user by ID as Map, positive testing
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
	 * 9. Query user by ID as Map, negative testing
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
	 * 10. Query user by ID as KojiUser, positive testing
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
	 * 11. Query user by ID as KojiUser, negative testing
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
	
	/**
	 * 12. Query host by ID as Map, positive testing
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
	 * 13. Query host by ID as Map, negative testing
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
	 * 14. Query host by ID as KojiHost, positive testing
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
	 * 15. Query host by ID as KojiHost, negative testing
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
	
	/**
	 * 16. Query all root task test.
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
		Object rootTaskArrayObject = kojiClient.getAllRootTasksAsObjectArray();
		assertNotNull(rootTaskArrayObject);
		assertTrue(rootTaskArrayObject instanceof Object[]);
		Object[] rootTaskArray = (Object[])rootTaskArrayObject;
		int arraySize = rootTaskArray.length;
		assertTrue(arraySize > 0);
		assertTrue(rootTaskArray[0] instanceof Map);
		Object rootTaskList = kojiClient.getAllRootTasksAsKojiTaskList();
		assertNotNull(rootTaskList);
		assertTrue(rootTaskList instanceof List);
		int listSize = ((List<KojiTask>)rootTaskList).size();
		assertTrue(listSize > 0);
		assertEquals(arraySize, listSize);
	}
	
	/**
	 * 17. Query task descendents, positive testing.
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
	 * 18. Query task descendents, negative testing
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetTaskDescendentsByIDAsMapNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query task -1
		Object descendents = kojiClient.getTaskDescendentsByIDAsMap(-1);
		assertNull(descendents);
	}
	
	/**
	 * 19. Query task by ID as Map, positive testing
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
	 * 20. Query task by ID as Map, negative testing
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetTaskInfoByIDAsMapNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query task -1
		Object taskObject = kojiClient.getTaskInfoByIDAsMap(-1);
		assertNull(taskObject);
	}
	
	/**
	 * 21. Query task by ID as KojiTask, positive testing.
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
	 * 22. Query task by ID as KojiTask, negative testing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetTaskInfoByIDAsKojiTaskNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		// query task -1
		Object taskObject = kojiClient.getTaskInfoByIDAsKojiTask(-1);
		assertNull(taskObject);
	}
	
	/**
	 * 23. Query task outputs, positive testing.
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
	 * 24. Query task outputs, negative testing.
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
		assertNull(taskOutputs);
		//query task 4626 (does not have outputs)
		taskOutputs = kojiClient.listTaskOutputAsMap(4626);
		assertNull(taskOutputs);
	}
	
	/**
	 * 25. Download task output, positive testing
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDownloadTaskOutputAsStringPositive() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		String output = kojiClient.downloadTaskOutputAsString(8150, "build.log", 0, 8401);
		assertEquals(8401, output.length());
		assertTrue(output.contains("Wrote: /builddir/build/SRPMS/ed-1.5-2.fc15.src.rpm"));
	}
	
	/**
	 * 26. Download task output, negative testing
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDownloadTaskOutputAsStringNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object output = kojiClient.downloadTaskOutputAsString(8149, "build.log", 0, 8401);
		assertNull(output);
	}
	
	/**
	 * 27. List package as object array test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testListPackagesAsObjectArray() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object packs = kojiClient.listPackagesAsObjectArray();
		assertNotNull(packs);
		assertTrue(packs instanceof Object[]);
		Object[] packages = (Object[])packs;
		assertTrue(packages.length > 0);
	}
	
	/**
	 * 28. List package as KojiPackage List test.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testListPackagesAsKojiPackageList() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object packs = kojiClient.listPackagesAsKojiPackageList(-1);
		assertNotNull(packs);
		assertTrue(packs instanceof List);
		List<KojiPackage> packages = (List<KojiPackage>)packs;
		assertTrue(packages.size() > 0);
	}
	
	/**
	 * 29. Get package by ID as map test, positive testing.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetPackageByIDAsMapPositive() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object result = kojiClient.getPackageByIDAsMap(430);
		assertNotNull(result);
		assertTrue(result instanceof Map);
		Map<String, ?> pack = (Map<String, ?>)result;
		Object nameObj = pack.get("name");
		assertNotNull(nameObj);
		String name = (String)nameObj;
		assertEquals(0, name.compareToIgnoreCase("ed"));
	}
	
	/**
	 * 30. Get package by ID as map test, negative testing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetPackageByIDAsMapNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object result = kojiClient.getPackageByIDAsMap(-1);
		assertNull(result);
	}
	
	/**
	 * 31. Get package by ID as KojiPackage Test, positive testing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetPackageByIDAsKojiPackagePositive() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object result = kojiClient.getPackageByIDAsKojiPackage(430, -1);
		assertNotNull(result);
		assertTrue(result instanceof KojiPackage);
		KojiPackage pack = (KojiPackage)result;
		assertEquals(3, pack.getRecentBuilds().size());
		assertEquals(0, pack.getPackageName().compareToIgnoreCase("ed"));
		result = kojiClient.getPackageByIDAsKojiPackage(430, 1);
		assertNotNull(result);
		assertTrue(result instanceof KojiPackage);
		pack = (KojiPackage)result;
		assertEquals(1, pack.getRecentBuilds().size());
		assertEquals(0, pack.getPackageName().compareToIgnoreCase("ed"));
	}
	
	/**
	 * 32. Get package by ID as KojiPackage test, negative testing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetPackageByIDAsKojiPackageNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object result = kojiClient.getPackageByIDAsKojiPackage(-1, -1);
		assertNull(result);
	}
	
	/**
	 * 33. List KojiBuild by package ID as List test, positive testing.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testListBuildByKojiPackageIDAsListPositve() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object result = kojiClient.listBuildByKojiPackageIDAsList(430, -1);
		assertNotNull(result);
		assertTrue(result instanceof List);
		List<KojiBuildInfo> buildList = (List<KojiBuildInfo>)result;
		assertEquals(3, buildList.size());
		assertEquals(0, buildList.get(0).getNvr().compareToIgnoreCase("ed-1.5-3.fc15"));
		result = kojiClient.listBuildByKojiPackageIDAsList(430, 1);
		assertNotNull(result);
		assertTrue(result instanceof List);
		buildList = (List<KojiBuildInfo>)result;
		assertEquals(1, buildList.size());
		assertEquals(0, buildList.get(0).getNvr().compareToIgnoreCase("ed-1.5-3.fc15"));
	}
	
	/**
	 * 34. List KojiBuild by package ID as List test, negative testing.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testListBuildByKojiPackageIDAsListNegatve() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object result = kojiClient.listBuildByKojiPackageIDAsList(-1, -1);
		assertNotNull(result);
		assertTrue(result instanceof List);
		List<KojiBuildInfo>buildList = (List<KojiBuildInfo>)result;
		assertEquals(0, buildList.size());
	}
	
	/**
	 * 35. Get source RPM by Build ID test, positive testing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetSourceRPMFromBuildIdAsMapPositive() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object result = kojiClient.getSourceRPMFromBuildIdAsMap(3015);
		assertNotNull(result);
		assertTrue(result instanceof Map);
		@SuppressWarnings("unchecked")
		Map<String, ?> sourceMap = (Map<String, ?>)result;
		int rpmID = ((Integer)sourceMap.get("id")).intValue();
		assertEquals(9167, rpmID);
	}

	/**
	 * 36. Get source RPM by Build ID test, negative testing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetSourceRPMFromBuildIdAsMapNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object result = kojiClient.getSourceRPMFromBuildIdAsMap(-1);
		assertNull(result);
	}
	
	/**
	 * 37. Get description from package ID test, positive testing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDescriptionFromPackageIdAsStringPositive() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object result = kojiClient.getDescriptionFromPackageIdAsString(9167);
		assertNotNull(result);
		assertTrue(result instanceof String);
		String descriptionStr = (String)result;
		assertTrue(descriptionStr.contains("Ed"));
	}
	
	/**
	 * 38. Get description from package ID test, negative testing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDescriptionFromPackageIdAsStringNegativeive() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object result = kojiClient.getDescriptionFromPackageIdAsString(-1);
		assertNull(result);
	}
	
	/**
	 * 39. Get the session info test.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetSessionInfoAsMap() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object result = kojiClient.getSessionInfoAsMap();
		assertNotNull(result);
		assertTrue(result instanceof Map);
		Map<String, ?> sessionInfo = (Map<String, ?>)result;
		int userID = KojiSessionInfoParsingUtility.getUserID(sessionInfo).intValue();
		assertEquals(9, userID);
	}
	
	/**
	 * 40. List packages of the user as object array, negative test.
	 * (this test is based on the assumption that the "testuser" account
	 * of the Toronto Koji server has no package, task or build pushed)
	 * 
	 *  @throws Exception
	 * 
	 */
	@Test
	public void testListPackageOfUserAsObjectArrayNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object result = kojiClient.listPackagesOfUserAsObjectArray();
		assertNull(result);
	}
	
	/**
	 * 41. List packages of the user as KojiPackage List, negative test.
	 * (this test is based on the assumption that the "testuser" account
	 * of the Toronto Koji server has no package, task or build pushed)
	 * 
	 *  @throws Exception
	 */
	@Test
	public void testListPackagesOfUserAsKojiPackageListNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object result = kojiClient.listPackagesOfUserAsKojiPackageList(-1);
		assertNull(result);
	}
	
	/**
	 * 42. List build of user by KojiPackage ID as list, negative test.
	 * (this test is based on the assumption that the "testuser" account
	 * of the Toronto Koji server has no package, task or build pushed)
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testListBuildOfUserByKojiPackageIDAsListNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object result = kojiClient.listBuildOfUserByKojiPackageIDAsList(-1, -1);
		assertNotNull(result);
		assertTrue(result instanceof List);
		List<KojiBuildInfo> buildList = (List<KojiBuildInfo>)result;
		assertEquals(0, buildList.size());
		result = kojiClient.listBuildOfUserByKojiPackageIDAsList(430, -1);
		assertNotNull(result);
		assertTrue(result instanceof List);
		buildList = (List<KojiBuildInfo>)result;
		assertEquals(0, buildList.size());
	}
	
	
	
	/**
	 * Get package by name as map, positive test.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetPackageByNameAsMapPositive() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object result = kojiClient.getPackageByNameAsMap("ed");
		assertNotNull(result);
		assertTrue(result instanceof Map);
		Map<String, ?> packMap = (Map<String, ?>)result;
		assertTrue(packMap.size() > 0);
		Object nameObj = packMap.get("name");
		assertNotNull(nameObj);
		String name = (String)nameObj;
		assertEquals(0, name.compareToIgnoreCase("ed"));
	}
	
	/**
	 * Get package by name as map, negative test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetPackageByNameAsMapNegative() throws Exception {
		// Log in first
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		Object result = kojiClient.getPackageByNameAsMap("e");
		assertNull(result);
	}
}
