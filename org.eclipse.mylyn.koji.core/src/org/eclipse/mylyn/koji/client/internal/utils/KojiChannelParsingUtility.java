package org.eclipse.mylyn.koji.client.internal.utils;

import java.util.Map;

import org.eclipse.mylyn.koji.client.api.KojiChannel;

/**
 * Koji channel parsing utility.
 * 
 * @author Kiu Kwan Leung (Red Hat)
 * 
 */
public final class KojiChannelParsingUtility {

	/**
	 * Utility method to parse a given map representing a Koji channel to a
	 * KojiChannel object.
	 * 
	 * IMPORTANT: It is the caller's responsibility to make sure that input is not null.
	 * 
	 * @param input
	 *            The map representing a Koji channel.
	 * @return A KojiChannel object.
	 */
	public static KojiChannel parseChannel(Map<String, ?> input) {
		KojiChannel channel = new KojiChannel();
		Object result = input.get("id"); //$NON-NLS-1$
		if ((result != null) && (result instanceof Integer))
			channel.setId((Integer) result);
		result = input.get("name"); //$NON-NLS-1$
		if ((result != null) && (result instanceof String))
			channel.setName((String) result);
		return channel;
	}
}
