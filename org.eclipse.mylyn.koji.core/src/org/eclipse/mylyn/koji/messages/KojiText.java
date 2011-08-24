package org.eclipse.mylyn.koji.messages;

import org.eclipse.osgi.util.NLS;

/**
 * Text for the Koji connector core plugin.
 *
 */
public class KojiText extends NLS {

	/**
	 * Do not in-line this into the static initializer as the
	 * "Find Broken Externalized Strings" tool will not be
	 * able to find the corresponding bundle file.
	 * 
	 * This is the path to the file containin gexternalized strings.
	 */
	private static final String BUNDLE_NAME = "org.eclipse.mylyn.koji.messages.messages";
	
	//MylynSSL
	/****/ public static String MylynSSL_certificatesMissingError;
	//KojiBuildExistException
	/****/ public static String KojiBuildExistException_msg;
	//KojiClientException
	/****/ public static String KojiLoginException_loginFailedMsg;
	//Generic Strings
	/****/ public static String xmlRPCconfigNotInitialized;
	//Build succeeded
	/****/ public static String MylynBuildSucceed;
	//Build failed
	/****/ public static String MylynBuildFailed;
	//Building
	/****/ public static String MylynBuildBuilding;
	//Build cancelled
	/****/ public static String MylynBuildCancelled;
	//Build deleted
	/****/ public static String MylynBuildDeleted;
	//Build is free
	/****/ public static String MylynBuildFree;
	//Build is assigned
	/****/ public static String MylynBuildAssigned;
	//Server validation error
	/****/ public static String KojiValidationError;
	static {
		initializeMessages(BUNDLE_NAME, KojiText.class);
	}
}
