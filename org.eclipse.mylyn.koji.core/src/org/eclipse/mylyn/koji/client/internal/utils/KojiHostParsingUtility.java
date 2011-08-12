package org.eclipse.mylyn.koji.client.internal.utils;

import java.util.Map;

import org.eclipse.mylyn.koji.client.api.IKojiHubClient;
import org.eclipse.mylyn.koji.client.api.KojiHost;
import org.eclipse.mylyn.koji.client.api.errors.KojiClientException;

/**
 * Koji host parsing utility.
 * 
 * @author Kiu Kwan Leung (Red Hat)
 * 
 */
public final class KojiHostParsingUtility {
	/**
	 * Parses a hash map representing a Koji host into a KojiHost object.
	 * 
	 * IMPORTANT: It is the caller's responsibility to make sure that input is not null.
	 *  
	 * @param input	The hash map representing a Koji host.
	 * @param wsClient	The Koji webservice client.
	 * @return	The KojiHost object.
	 * @throws KojiHubClientException 
	 */
	public static KojiHost parseHost(Map<String, ?> input, IKojiHubClient wsClient) throws KojiClientException {
		KojiHost host = new KojiHost();
		Object result = input.get("id");	//$NON-NLS-1$
		if(result instanceof Integer)
			host.setId(((Integer)result).intValue());
		result = input.get("name"); //$NON-NLS-1$
		if((result != null) && (result instanceof String))
			host.setName(new String((String)result));
		result = input.get("enabled"); 		//$NON-NLS-1$
		if((result != null) && (result instanceof Boolean))
			host.setEnabled(((Boolean)result).booleanValue());
		result = input.get("task_load");	//$NON-NLS-1$
		if((result != null) && (result instanceof Double))
			host.setTaskLoad(((Double)result).doubleValue());
		result = input.get("description"); 	//$NON-NLS-1$
		if((result != null) && (result instanceof String))
			host.setDescription(new String((String)result));
		result = input.get("arches");		//$NON-NLS-1$
		if((result != null) && (result instanceof String))
			host.setArchitecture(new String((String)result));
		result = input.get("capacity"); 	//$NON-NLS-1$
		if((result != null) && (result instanceof Double))
			host.setCapacity(((Double)result).doubleValue());
		result = input.get("user_id"); 		//$NON-NLS-1$
		if((result != null) && (result instanceof Integer)) {
			int userID = ((Integer)result).intValue();
			host.setUserID(userID);
			host.setUser(wsClient.getUserInfoByIDAsKojiUser(userID));
		}
		result = input.get("comment");		//$NON-NLS-1$
		if((result != null) && (result instanceof String))
			host.setComment(new String((String)result));
		result = input.get("ready");		//$NON-NLS-1$
		if((result != null) && (result instanceof Boolean))
			host.setReady(((Boolean)result).booleanValue());
		return host;
	}
	
}
