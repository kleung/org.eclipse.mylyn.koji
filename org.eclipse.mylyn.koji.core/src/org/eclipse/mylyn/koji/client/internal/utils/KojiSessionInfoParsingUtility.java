package org.eclipse.mylyn.koji.client.internal.utils;

import java.util.Map;

/**
 * Koji session parsing utility.
 * 
 * @author Kiu Kwan Leung (Red Hat)
 * 
 */
public final class KojiSessionInfoParsingUtility {

	/**
	 * Extracts the user ID out of a map containing the current session's information.
	 * @param sessionInfo The map containing the current session's information.
	 * @return The user ID.
	 */
	public static Integer getUserID(Map<String, ?> sessionInfo) {
		return (Integer)sessionInfo.get("user_id");
	}
}
