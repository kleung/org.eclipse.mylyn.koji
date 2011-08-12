package org.eclipse.mylyn.koji.client.internal.utils;

import java.util.Collections;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;

import org.eclipse.mylyn.koji.client.api.IKojiHubClient;
import org.eclipse.mylyn.koji.client.api.KojiTask;
import org.eclipse.mylyn.koji.client.api.errors.KojiClientException;

/**
 * Koji task parsing utility.
 * 
 * @author Kiu Kwan Leung (Red Hat)
 * 
 */
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
		result = input.get("completion_time"); //$NON-NLS-1$
		if ((result != null) && (result instanceof String)) {
			// drop decimal places
			result = dropDecimalFromTimestamp((String) result);
			task.setCompletionTimeString(new String((String) result));
		}
		result = input.get("weight"); //$NON-NLS-1$
		if ((result != null) && (result instanceof Double))
			task.setWeight(((Double) result).doubleValue());
		result = input.get("arch"); //$NON-NLS-1$
		if ((result != null) && (result instanceof String))
			task.setArchitecture(new String((String) result));
		result = input.get("start_ts"); //$NON-NLS-1$
		if ((result != null) && (result instanceof Double))
			task.setStartTime(((Double) result).doubleValue());
		result = input.get("start_time"); //$NON-NLS-1$
		if ((result != null) && (result instanceof String)) {
			// drop decimal places
			result = dropDecimalFromTimestamp((String) result);
			task.setStartTimeString(new String((String) result));
		}
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
		if ((result != null) && (result instanceof String)) {
			// drop decimal places
			result = dropDecimalFromTimestamp((String) result);
			task.setCreationTimeString(new String((String) result));
		}
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
	
	private static String dropDecimalFromTimestamp(String input) {
		// drop the decimal places of the timestamp
		int dotIndex = input.indexOf("."); //$NON-NLS-1$
		return input.substring(0, dotIndex);
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
	
}
