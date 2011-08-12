package org.eclipse.mylyn.koji.client.internal.utils;

import java.util.Map;

import org.eclipse.mylyn.koji.client.api.IKojiHubClient;
import org.eclipse.mylyn.koji.client.api.KojiBuildInfo;
import org.eclipse.mylyn.koji.client.api.errors.KojiClientException;

/**
 * Koji task parsing utility.
 * 
 * @author Serverin Gehwolf (Red Hat), Kiu Kwan Leung (Red Hat)
 * 
 */
public final class KojiBuildInfoParsingUtility {
	
	/* relevant keys of the returned map */ 
	private static final String KEY_ID = "id"; //$NON-NLS-1$
	private static final String KEY_PACKAGE_ID = "package_id"; //$NON-NLS-1$
	private static final String KEY_PACKAGE_NAME = "package_name"; //$NON-NLS-1$
	private static final String KEY_VERSION = "version"; //$NON-NLS-1$
	private static final String KEY_RELEASE = "release"; //$NON-NLS-1$
	private static final String KEY_EPOCH = "epoch"; //$NON-NLS-1$
	private static final String KEY_NVR = "nvr"; //$NON-NLS-1$
	private static final String KEY_STATE = "state"; //$NON-NLS-1$
	private static final String KEY_TASK_ID = "task_id"; //$NON-NLS-1$
	private static final String KEY_OWNER_NAME = "owner_name"; //$NON-NLS-1$
	
	/**
	 * Utility method to parse a given map representing a Koji build to a
	 * KojiBuildInfo object.
	 * 
	 * IMPORTANT: It is the caller's responsibility to make sure that input is not null.
	 * 
	 * @param input
	 *            The map representing a Koji build.
	 * @return A KojiBuildInfo object.
	 */
	
	public static KojiBuildInfo parseBuild(Map<String, Object> input, IKojiHubClient wsClient) throws KojiClientException {
		KojiBuildInfo build = new KojiBuildInfo();
		Object result = input.get(KEY_STATE);
		if((result != null) && (result instanceof Integer))
			build.setState(((Integer)result).intValue());
		result = input.get(KEY_ID);
		if((result != null) && (result instanceof Integer))
			build.setBuildId(((Integer)result).intValue());
		result = input.get(KEY_RELEASE);
		if((result != null) && (result instanceof String))
			build.setRelease(new String((String)result));
		result = input.get(KEY_VERSION);
		if((result != null) && (result instanceof String))
			build.setVersion(new String((String)result));
		result = input.get(KEY_PACKAGE_NAME);
		if((result != null) && (result instanceof String))
			build.setPackageName(new String((String)result));
		result = input.get(KEY_NVR);
		if((result != null) && (result instanceof String))
			build.setNvr(new String((String)result));
		result = input.get(KEY_PACKAGE_ID);
		if((result != null) && (result instanceof Integer))
			build.setPackageId(((Integer)result).intValue());
		result = input.get(KEY_OWNER_NAME);
		if((result != null) && (result instanceof String))
			build.setOwnerName(new String((String)result));
		result = input.get(KEY_EPOCH);
		if((result != null) && (result instanceof Integer))
			build.setEpoch(((Integer)result).intValue());
		result = input.get(KEY_TASK_ID);
		if((result != null) && (result instanceof Integer)) {
			int taskId = ((Integer)result).intValue();
			build.setTaskId(taskId);
			Map<String, ?> taskMap = wsClient.getTaskInfoByIDAsMap(taskId);
			build.setTask(KojiTaskParsingUtility.parseTask(taskMap, wsClient, true));
		}
		return build;
	}

}
