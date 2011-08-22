package org.eclipse.mylyn.koji.client.internal.utils;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.mylyn.builds.core.IBuildPlan;
import org.eclipse.mylyn.builds.internal.core.Build;
import org.eclipse.mylyn.koji.client.api.IKojiHubClient;
import org.eclipse.mylyn.koji.client.api.KojiBuildInfo;
import org.eclipse.mylyn.koji.client.api.KojiPackage;
import org.eclipse.mylyn.koji.client.api.MylynKojiBuildPlan;
import org.eclipse.mylyn.koji.client.api.errors.KojiClientException;
import org.eclipse.mylyn.koji.connector.KojiServerBehavior;

/**
 * Koji package parsing utility.
 * 
 * @author Kiu Kwan Leung (Red Hat)
 * 
 */
@SuppressWarnings("restriction")
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
		if(parseBuilds && ((limit > 0) || (limit == -1))) {
			List<KojiBuildInfo> buildList = wsClient.listBuildOfUserByKojiPackageIDAsList(pack.getPackageID(), limit);
			pack.setRecentBuilds(buildList);
			if(buildList.size() > 0) {
				int mostRecentBuildID = buildList.get(0).getBuildId();
				Map<String, Object> srcRpmMap = wsClient.getSourceRPMFromBuildIdAsMap(mostRecentBuildID);
				int packId = ((Integer)srcRpmMap.get("id")).intValue();
				pack.setDescription(wsClient.getDescriptionFromPackageIdAsString(packId));
			}
		}
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
	
	/**
	 * Copy the applicable content of a given KojiPackage object into a MylynBuildPlan object for use with Mylyn Builds.
	 * If the Koji package doesn't contain any KojiBuildInfo or the server behavior failed to create a Build object,
	 * null is returned.
	 * 
	 * IMPORTANT: It is the caller's responsibility to ensure the parameter is not null and of the expected type.
	 * 
	 * @param pack The KojiPackage object.
	 * @return A MylynKojiBuildPlan object with its fields filled with content stored by the KojiPackage parameter.
	 */
	public static IBuildPlan cloneKojiPackageContentToIBuildPlan(KojiPackage pack, KojiServerBehavior behavior) throws IllegalArgumentException {
		if(pack == null)
			throw new IllegalArgumentException("Cannot convert a null Koji package to a Mylyn build plan.");
		MylynKojiBuildPlan buildPlan = new MylynKojiBuildPlan();
		buildPlan.setId(Integer.toString(pack.getPackageID()));
		buildPlan.setName(pack.getPackageName());
		buildPlan.setDescription(pack.getDescription());
		//The entire koji package is being stored here for use with resubmitting the task (rebuild).
		buildPlan.setPack(pack);
		//if Mylyn Builds team decides to support sub-builds, modify the rest of the body to include
		//the entire list of past builds.
		if((pack.getRecentBuilds().size() > 0) && (pack.getRecentBuilds().get(0) != null)) {
			KojiBuildInfo buildInfo = pack.getRecentBuilds().get(0);
			Build build = behavior.createBuild();
			if(build == null)
				buildPlan = null;
			else {
				build = KojiBuildInfoParsingUtility.cloneKojiBuildInfoContentToIBuild(buildInfo, build);
				buildPlan.setLastBuild(build);
			}
		} else 
			buildPlan = null;
		return buildPlan;
	}
}
