package org.eclipse.mylyn.koji.client.internal.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.mylyn.builds.core.BuildState;
import org.eclipse.mylyn.builds.core.BuildStatus;
import org.eclipse.mylyn.builds.core.IBuild;
import org.eclipse.mylyn.koji.client.api.IKojiHubClient;
import org.eclipse.mylyn.koji.client.api.KojiTask;
import org.eclipse.mylyn.koji.client.api.errors.KojiClientException;

/**
 * Koji task parsing utility.
 * 
 * @author Kiu Kwan Leung (Red Hat)
 * 
 */
@SuppressWarnings("restriction")
public final class KojiTaskParsingUtility {

	/**
	 * Parses a Map<String, ?> containing information about a Koji task into a
	 * KojiTask object.
	 * 
	 * 
	 * IMPORTANT: It is the caller's responsibility to make sure that input is not null.
	 * @param input
	 *            The map representing a Koji task.
	 * @param wsClient
	 *            The Koji webservice client.
	 * @param queryDescendents
	 *            A boolean determining whether the method should also query and
	 *            parse the task's descendents or not.
	 * @return A KojiObject.
	 * @throws KojiClientException
	 */
	@SuppressWarnings("unchecked")
	public static KojiTask parseTask(Map<String, ?> input,
			IKojiHubClient wsClient, boolean queryDescendents)
			throws KojiClientException {
		KojiTask task = internalParseTask(input, wsClient);
		Object result = null;
		result = input.get("parent"); //$NON-NLS-1$
		if ((result != null) && (result instanceof Integer)) {
			int parentID = ((Integer) result).intValue();
			Map<String, ?> wsResult = wsClient.getTaskInfoByIDAsMap(parentID);
			if(wsResult != null)
				// the third parameter is false because we are
				// not interested in building the entire tree.
				task.setParentTask(parseTask(wsResult, wsClient, false));
			else
				task.setParentTask(null);
		}
		result = input.get("channel_id"); //$NON-NLS-1$
		if ((result != null) && (result instanceof Integer)) {
			int channelID = ((Integer) result).intValue();
			task.setChannel(wsClient.getChannelInfoByIDAsKojiChannel(channelID));
		}
		if ((result != null) && (result instanceof Integer)) {
			int hostID = ((Integer) result).intValue();
			task.setHost(wsClient.getHostInfoByIDAsKojiHost(hostID));
		}
		if ((result != null) && (result instanceof Integer)) {
			int ownerID = ((Integer) result).intValue();
			task.setOwner(wsClient.getUserInfoByIDAsKojiUser(ownerID));
		}
		if (queryDescendents) {
			int taskID = task.getId();
			String taskIDString = Integer.toString(taskID);
			LinkedList<KojiTask> descendentList = new LinkedList<KojiTask>();
			LinkedList<Integer> descendentIDList = new LinkedList<Integer>();
			Map<String, ?> extractedMap = wsClient
					.getTaskDescendentsByIDAsMap(taskID);
			if (extractedMap != null) {
				Object[] descendentMaps = (Object[]) extractedMap
						.get(taskIDString);
				for (Object descendent : descendentMaps) {
					// not interested in building a tree, so set the third
					// parameter
					// as false.
					Map<String, ?> castedDescendent = (Map<String, ?>)descendent;
					KojiTask t = parseTask(castedDescendent, wsClient, false);
					descendentList.add(t);
					descendentIDList.add(new Integer(t.getId()));
				}
			}
			task.setDescendentIDs(descendentIDList);
			task.setDescendents(descendentList);
		}
		return task;
	}

	private static KojiTask internalParseTask(Map<String, ?> input,
			IKojiHubClient wsClient) {
		//fix me: task state is missing.
		KojiTask task = new KojiTask();
		Object result = input.get("id"); //$NON-NLS-1$
		if (result instanceof Integer)
			task.setId(((Integer) result).intValue());
		result = input.get("completion_ts"); //$NON-NLS-1$
		if ((result != null) && (result instanceof Double))
			task.setCompletionTime(((Double) result).doubleValue());
		result = input.get("weight"); //$NON-NLS-1$
		if ((result != null) && (result instanceof Double))
			task.setWeight(((Double) result).doubleValue());
		result = input.get("arch"); //$NON-NLS-1$
		if ((result != null) && (result instanceof String))
			task.setArchitecture(new String((String) result));
		result = input.get("start_ts"); //$NON-NLS-1$
		if ((result != null) && (result instanceof Double))
			task.setStartTime(((Double) result).doubleValue());
		result = input.get("parent"); //$NON-NLS-1$
		if ((result != null) && (result instanceof Integer))
			task.setParentTaskID(((Integer) result).intValue());
		result = input.get("label"); //$NON-NLS-1$
		if ((result != null) && (result instanceof String))
			task.setLabel(new String((String) result));
		result = input.get("create_ts"); //$NON-NLS-1$
		if ((result != null) && (result instanceof Double))
			task.setCreationTime(((Double) result).doubleValue());
		result = input.get("create_time"); //$NON-NLS-1$
		result = input.get("channel_id"); //$NON-NLS-1$
		if ((result != null) && (result instanceof Integer))
			task.setChannelID(((Integer) result).intValue());
		result = input.get("host_id"); //$NON-NLS-1$
		if ((result != null) && (result instanceof Integer))
			task.setHostID(((Integer) result).intValue());
		result = input.get("priority"); //$NON-NLS-1$
		if ((result != null) && (result instanceof Integer))
			task.setPriority(((Integer) result).intValue());
		result = input.get("owner"); //$NON-NLS-1$
		if ((result != null) && (result instanceof Integer))
			task.setOwnerID(((Integer) result).intValue());
		result = input.get("method"); //$NON-NLS-1$
		if((result != null) && (result instanceof String))
			task.setMethod(new String((String)result));
		result = input.get("request");//$NON-NLS-1$
		if ((result != null) && (result instanceof Object[])) {
			Object[] requestStrings = (Object[]) result;
			Object requestStr = requestStrings[0];
			if ((requestStr != null) && (requestStr instanceof String)) {
				String fullRpmStr = (String) requestStr;
				task.setRpm(fullRpmStr.substring(
						fullRpmStr.lastIndexOf("/") + 1, fullRpmStr.length())); //$NON-NLS-1$
			}
			requestStr = requestStrings[1];
			if ((requestStr != null) && (requestStr instanceof String))
				task.setBuildTarget(new String((String) requestStr));
		}
		return task;
	}

	/**
	 * Parses an array of hash maps, which contain information about Koji tasks,
	 * into a list of KojiTask objects.
	 * 
	 * 
	 * IMPORTANT: It is the caller's responsibility to make sure that input is not null.
	 * @param tasks
	 *            The array of hash map representing Koji tasks.
	 * @param wsClient
	 *            The Koji webservice client.
	 * @return A list of KojiTask objects.
	 * @throws KojiClientException
	 */
	@SuppressWarnings("unchecked")
	public static List<KojiTask> parseTaskArray(Object[] tasks,
			IKojiHubClient wsClient) throws KojiClientException {
		LinkedList<KojiTask> taskList = new LinkedList<KojiTask>();
		if (tasks != null) {
			for (Object o : tasks) {
				Map<String, ?> task = (Map<String, ?>) o;
				// not interested in building a tree, do not query descendents.
				taskList.add(internalParseTask(task, wsClient));
			}
			// sort task IDs in descending order (should, in theory, also sort them
			// by creation time from latest to oldest).
			Collections.sort(taskList);
			Collections.reverse(taskList);
		}
		return taskList;
	}
	
	/**
	 * Copy the applicable content of a given KojiTask object into an IBuild object for use with Mylyn Builds.
	 * 
	 * IMPORTANT: It is the caller's responsibility to ensure the parameters are not null and of the expected type.
	 * 
	 * @param task The KojiTask object.
	 * @param build The output IBuild object.
	 * @return The IBuild parameter with its fields filled with content stored by the KojiTask parameter.
	 */
	public static IBuild cloneKojiTaskContentToIBuild(KojiTask task, IBuild build) {
		String displayName = build.getDisplayName();
		if((displayName == null) || (displayName.trim().compareToIgnoreCase("") == 0))
			build.setDisplayName(task.getRpm());
		build.setBuildNumber(1);
		build.setName(1+"");
		switch(task.getTaskStateCode()) {
		case 0:	//Free
				build.setState(BuildState.BUILDABLE);
				break;
		case 1:	//Open
				build.setState(BuildState.RUNNING);
				break;
		case 2:	//closed
				build.setState(BuildState.STOPPED);
				build.setStatus(BuildStatus.SUCCESS);
				break;
		case 3:	//cancelled
				build.setState(BuildState.STOPPED);
				build.setStatus(BuildStatus.ABORTED);
				break;
		case 4:	//assigned
				build.setState(BuildState.QUEUED);
				break;
		case 5:	//failed
				build.setState(BuildState.STOPPED);
				build.setStatus(BuildStatus.FAILED);
				break;
		}
		
		
		/*
		build.setDuration(value)
		build.setSummary(value)
		build.setTimestamp(value)
		*/
		return build;
	}
	
	/**
	 * Copy the applicable content of a given KojiTask List, one by one, into a
	 * given list of IBuild objects.
	 * 
	 * IMPORTANT: It is the caller's responsibility to ensure that the
	 * parameters are not null, of same size and filled with the same amount of
	 * objects of the expected type.
	 * 
	 * @param taskList
	 *            The input KojiTask list.
	 * @param buildList
	 *            The output IBuild List.
	 * @return The IBuild List with the each of its entries' content filled with
	 *         content stored by the each of the entries of the input KojiTask
	 *         list.
	 */
	public static List<IBuild> cloneKojiTaskListContentToIBuildList(
			List<KojiTask> taskList, List<IBuild> buildList)
			throws IllegalArgumentException {
		return buildList;
	}
}
