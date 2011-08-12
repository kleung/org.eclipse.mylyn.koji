package org.eclipse.mylyn.koji.client.internal.utils;

import java.util.Map;

import org.eclipse.mylyn.koji.client.api.KojiUser;

/**
 * Koji user parsing utility.
 * 
 * @author Kiu Kwan Leung (Red Hat)
 * 
 */
public final class KojiUserParsingUtility {
	
	/**
	 * Constant for status codes.
	 */
	public static final String[] status = { "NORMAL", 		//$NON-NLS-1$
											"BLOCKED" };	//$NON-NLS-1$

	/**
	 * Constant for user type codes.
	 */
	public static final String[] userType = { "NORMAL", 	//$NON-NLS-1$
												"HOST", 	//$NON-NLS-1$
												"GROUP" };	//$NON-NLS-1$

	/**
	 * Parses a map representing a Koji user to a KojiUser object.
	 * 
	 * IMPORTANT: It is the caller's responsibility to make sure that input is not null.
	 * 
	 * @param input The map representing a Koji user.
	 * @return	The KojiUser object.
	 */
	public static KojiUser parseUser(Map<String, ?> input) {
		KojiUser user = new KojiUser();
		Object result = input.get("id"); //$NON-NLS-1$
		if(result instanceof Integer)
			user.setId(((Integer)result).intValue());
		result = input.get("status"); //$NON-NLS-1$
		if((result != null) && (result instanceof Integer)) {
			int statusCode = ((Integer)result).intValue();
			user.setStatusCode(statusCode);
			user.setStatus(new String(status[statusCode]));
		}
		result = input.get("name"); //$NON-NLS-1$
		if((result != null) && (result instanceof String))
			user.setName(new String(((String)result)));
		result = input.get("usertype"); //$NON-NLS-1$
		if((result != null) && (result instanceof Integer)) {
			int userTypeCode = ((Integer)result).intValue();
			user.setUserTypeCode(userTypeCode);
			user.setUserType(new String(userType[userTypeCode]));
		}
		return user;
	}
	
}
