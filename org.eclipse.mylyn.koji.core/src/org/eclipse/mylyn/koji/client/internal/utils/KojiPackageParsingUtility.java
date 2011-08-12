package org.eclipse.mylyn.koji.client.internal.utils;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.mylyn.koji.client.api.IKojiHubClient;
import org.eclipse.mylyn.koji.client.api.KojiPackage;
import org.eclipse.mylyn.koji.client.api.errors.KojiClientException;

/**
 * Koji package parsing utility.
 * 
 * @author Kiu Kwan Leung (Red Hat)
 * 
 */
public final class KojiPackageParsingUtility {

	/**
	 * Parses a Map<String, ?> containing information about a Koji package into a
	 * KojiPackage object.
	 * 
	 * 
	 * IMPORTANT: It is the caller's responsibility to make sure that input is not null.
	 * @param input
	 *            The map representing a Koji package.
	 * @param parseBuilds
	 * 			  Determines whether to parse its recent builds or not.
	 * @param wsClient
	 *            The Koji webservice client.
	 * @param limit
	 * 			  The amount of recent builds to parse, 
	 * @return A KojiObject.
	 * @throws KojiClientException
	 */
	public static KojiPackage parsePackage(Map<String, ?> input, boolean parseBuilds, IKojiHubClient wsClient, int limit) throws KojiClientException, IllegalArgumentException {
		KojiPackage pack = internalParsePackage(input);
		if(parseBuilds && (limit > 0))
			pack.setRecentBuilds(wsClient.listBuildByKojiPackageIDAsList(pack.getPackageID(), limit));
		return pack;
	}
	
	private static KojiPackage internalParsePackage(Map<String, ?> input){
		KojiPackage pack = new KojiPackage();
		Object result = input.get("id");
		if((result != null) && (result instanceof Integer))
			pack.setPackageID(((Integer)result).intValue());
		result = input.get("name");
		if((result != null) && (result instanceof String))
			pack.setPackageName(new String((String)result));
		return pack;
	}
	
	/**
	 * Parses an array of hash maps containning Koji packages into  a list of KojiPackage objects.
	 * 
	 * IMPORTANT: It is the caller's responsibility to make sure that input is not null.
	 * 
	 * @param input The array of hash maps containning information about Koji packages.
	 * @return A list of KojiPackage objects.
	 */
	public static List<KojiPackage> parsePackageArrayAsKojiPackageList(Object[] input) {
		LinkedList<KojiPackage> packList = new LinkedList<KojiPackage>();
		for(Object o : input) {
			if((o != null) && (o instanceof Map)) {
				@SuppressWarnings("unchecked")
				Map<String, Object> packageMap = (Map<String, Object>)o;
				KojiPackage pack = internalParsePackage(packageMap);
				packList.add(pack);
			}
		}
		return packList;
	}
}
