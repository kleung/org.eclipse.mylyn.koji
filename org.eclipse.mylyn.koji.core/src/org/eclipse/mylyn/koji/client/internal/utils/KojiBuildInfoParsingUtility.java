package org.eclipse.mylyn.koji.client.internal.utils;

import java.util.Map;

import org.eclipse.mylyn.builds.core.BuildState;
import org.eclipse.mylyn.builds.core.BuildStatus;
import org.eclipse.mylyn.builds.core.IBuild;
import org.eclipse.mylyn.koji.client.api.IKojiHubClient;
import org.eclipse.mylyn.koji.client.api.KojiBuildInfo;
import org.eclipse.mylyn.koji.client.api.errors.KojiClientException;
import org.eclipse.mylyn.koji.messages.KojiText;

/**
 * Koji task parsing utility.
 * 
 * @author Serverin Gehwolf (Red Hat), Kiu Kwan Leung (Red Hat)
 * 
 */
@SuppressWarnings("restriction")
public final class KojiBuildInfoParsingUtility {
	
	/* relevant keys of the returned map */ 
	private static final String KEY_ID = "build_id"; //$NON-NLS-1$
	private static final String KEY_PACKAGE_ID = "package_id"; //$NON-NLS-1$
	private static final String KEY_PACKAGE_NAME = "package_name"; //$NON-NLS-1$
	private static final String KEY_VERSION = "version"; //$NON-NLS-1$
	private static final String KEY_RELEASE = "release"; //$NON-NLS-1$
	private static final String KEY_EPOCH = "epoch"; //$NON-NLS-1$
	private static final String KEY_NVR = "nvr"; //$NON-NLS-1$
	private static final String KEY_STATE = "state"; //$NON-NLS-1$
	private static final String KEY_TASK_ID = "task_id"; //$NON-NLS-1$
	private static final String KEY_OWNER_NAME = "owner_name"; //$NON-NLS-1$
	private static final String KEY_CREATE_TIME = "creation_ts";
	private static final String KEY_COMPLETE_TIME = "completion_ts";
	
	
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
		result = input.get(KEY_CREATE_TIME);
		if((result != null) && (result instanceof Double)) {
			double startD = ((Double)result).doubleValue();
			build.setCreateTime((long)(startD*1000));
		}
		result = input.get(KEY_COMPLETE_TIME);
		if((result != null) && (result instanceof Double)) {
			double endD = ((Double)result).doubleValue();
			build.setCompleteTime((long)(endD*1000));
		}
		result = input.get(KEY_TASK_ID);
		if((result != null) && (result instanceof Integer)) {
			Integer taskId = (Integer)result;
			build.setTaskId(taskId);
			if(wsClient != null) {
				Map<String, ?> taskMap = wsClient.getTaskInfoByIDAsMap(taskId);
				build.setTask(KojiTaskParsingUtility.parseTask(taskMap, wsClient, true));
			}
		}
		return build;
	}

	/**
	 * Copy the applicable content of the KojiBuildInfo object to the IBuild
	 * object.
	 * 
	 * IMPORTANT: It is the caller's responsibility to ensure the parameters are
	 * not null and of the expected type.
	 * 
	 * @param kojiBuildInfo
	 *            The input KojiBuildInfo object.
	 * @param build
	 *            The output IBuild object.
	 * @return The IBuild parameter with the fields filled with content stored
	 *         by the KojiPackage parameter.
	 */
	public static IBuild cloneKojiBuildInfoContentToIBuild(
			KojiBuildInfo kojiBuildInfo, IBuild build) {
		//use build ID & NVR regardless availability of task.
		build.setId(Integer.toString(kojiBuildInfo.getBuildId()));
		build.setDisplayName(new String(kojiBuildInfo.getNvr()));
		build.setName(new String(kojiBuildInfo.getNvr()));
		if(kojiBuildInfo.getTask() == null) { //task is not available, use build instead.
			build.setBuildNumber(1);
			build.setName(1+"");
			//Build state and build status
			switch(kojiBuildInfo.getState()) {
			case 0: //running
					build.setState(BuildState.RUNNING);
					build.setSummary(KojiText.MylynBuildBuilding);
					break;
			case 1: //successful
					build.setState(BuildState.STOPPED);
					build.setStatus(BuildStatus.SUCCESS);
					build.setSummary(KojiText.MylynBuildSucceed);
					break;
			case 2: //deleted
					build.setState(BuildState.STOPPED);
					build.setStatus(BuildStatus.DISABLED);
					build.setSummary(KojiText.MylynBuildDeleted);
					break;
			case 3:	//failed
					build.setState(BuildState.STOPPED);
					build.setStatus(BuildStatus.FAILED);
					build.setSummary(KojiText.MylynBuildFailed);
					break;
			case 4:	//cancelled
					build.setState(BuildState.STOPPED);
					build.setStatus(BuildStatus.ABORTED);
					build.setSummary(KojiText.MylynBuildCancelled);
					break;
			}
			//build.setDuration(value)
			//build.setTimestamp(value)
		} else
			KojiTaskParsingUtility.cloneKojiTaskContentToIBuild(kojiBuildInfo.getTask(), build);
		return build;
	}
}
