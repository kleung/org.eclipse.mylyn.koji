package org.eclipse.mylyn.koji.tests.units;

import static org.junit.Assert.*;

import java.util.List;
import java.util.HashMap;

import org.eclipse.mylyn.koji.client.api.IKojiHubClient;
import org.eclipse.mylyn.koji.client.api.KojiBuildInfo;
import org.eclipse.mylyn.koji.client.api.KojiPackage;
import org.eclipse.mylyn.koji.client.api.KojiSSLHubClient;
import org.eclipse.mylyn.koji.client.api.KojiTask;
import org.eclipse.mylyn.koji.client.internal.utils.KojiEntityStringSerializationDeserializationUtility;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KojiEntityStringSerializationDeserializationTest {
	
	private IKojiHubClient kojiClient;
	
	@Before
	public void setUp() throws Exception {
		// a bare SSL Koji client
		//kojiClient = new KojiSSLHubClient("https://koji.fedoraproject.org/kojihub");
		kojiClient = new KojiSSLHubClient("https://koji-toronto.usersys.redhat.com/kojihub");
	}

	@After
	public void tearDown() throws Exception {
		kojiClient.logout();
	}
	
	@Test
	public void KojiTasKSerializationDeserializationTest() throws Exception {
		HashMap<?, ?> sessionData = kojiClient.login();
		assertNotNull(sessionData);
		KojiTask task = kojiClient.getTaskInfoByIDAsKojiTask(8149);
		int taskId = task.getId();
		List<KojiTask> descendentList = task.getDescendents();
		String serializedString = null;
		serializedString = KojiEntityStringSerializationDeserializationUtility.serializeKojiEntityToString(task);
		assertNotNull(serializedString);
		KojiTask deserializedTask = KojiEntityStringSerializationDeserializationUtility.deserializeKojiTaskFromString(serializedString);
		assertNotNull(deserializedTask);
		assertEquals(taskId, deserializedTask.getId());
		List<KojiTask>deserializedTaskDescendentList = deserializedTask.getDescendents();
		assertNotNull(deserializedTaskDescendentList);
		assertEquals(descendentList.size(), deserializedTaskDescendentList.size());
	}
}
