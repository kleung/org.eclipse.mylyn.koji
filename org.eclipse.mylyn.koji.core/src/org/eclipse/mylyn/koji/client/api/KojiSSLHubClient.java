/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.koji.client.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.xmlrpc.XmlRpcException;
import org.eclipse.mylyn.koji.SSL.MylynSSL;
import org.eclipse.mylyn.koji.SSL.MylynSSLFactory;
import org.eclipse.mylyn.koji.messages.KojiText;
import org.eclipse.mylyn.koji.client.api.errors.KojiLoginException;

/**
 * Koji hub client which uses certificate based
 * authentication (SSL).
 */
public class KojiSSLHubClient extends AbstractKojiHubBaseClient {
	
	/**
	 * @param kojiHubUrl The koji hub URL to use.
	 * 
	 * @throws MalformedURLException
	 */
	public KojiSSLHubClient(String kojiHubUrl) throws MalformedURLException {
		super(kojiHubUrl);
	}
	
	/**
	 * SSL implementation of XMLRPC based login()
	 * 
	 * Login to remote URL specified by constructor or setter using the SSL
	 * client certificate.
	 * 
	 * @see IKojiHubClient#login()
	 * 
	 * @return session key and session id as a Map.
	 * @throws IllegalStateException
	 *             if hub URL has not been configured.
	 * @throws KojiLoginException
	 *             if login fails for some other reason.
	 */
	@Override
	public HashMap<?, ?> login() throws KojiLoginException {
		// Initialize SSL connection
		try {
			initSSLConnection();
		} catch (FileNotFoundException e) {
			// certs are missing
			throw new KojiLoginException(e, true);
		} catch (GeneralSecurityException e) {
			throw new KojiLoginException(e);
		} catch (IOException e) {
			throw new KojiLoginException(e);
		}
		HashMap<?, ?> loginSessionInfo = doSslLogin();
		// save session info in xmlrpc config
		saveSessionInfo(
				(String) loginSessionInfo.get("session-key"), //$NON-NLS-1$
				((Integer)loginSessionInfo.get("session-id")).toString()); //$NON-NLS-1$
		return loginSessionInfo;
	}

	/**
	 * Log on to URL using SSL.
	 * 
	 * @throws KojiLoginException
	 *             if login returns something unexpected.
	 */
	private HashMap<?, ?> doSslLogin() throws KojiLoginException {
		// prepare XMLRPC
		setupXmlRpcConfig();
		// setup for SSL login
		setupSSLLoginXMLRPCConfig();
		setupXmlRpcClient();
		// do the login
		ArrayList<String> params = new ArrayList<String>();
		Object result = null;
		HashMap<?, ?> hashMap = null;
		try {
			result = xmlRpcClient.execute("sslLogin", params); //$NON-NLS-1$
			hashMap = (HashMap<?, ?>) result;
		} catch (ClassCastException e) {
			// Something is fishy, should have returned a map
			throw new KojiLoginException(e);
		} catch (XmlRpcException e) {
			throw new KojiLoginException(e);
		}
		return hashMap;
	}

	/**
	 * Initialize SSL connection
	 */
	protected void initSSLConnection() throws FileNotFoundException, GeneralSecurityException, IOException {
		// Create empty HostnameVerifier
		HostnameVerifier hv = new HostnameVerifier() {
			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
		};
		//MylynSSL fedoraSSL = MylynSSLFactory.getInstance();
		MylynSSL fedoraSSL = MylynSSLFactory.getInstance(new File("/home/testuser/koji/.koji/client.crt"), 
				new File("/home/testuser/koji/.koji/client.crt"), new File("/home/testuser/koji/.koji/serverca.crt"));
		SSLContext ctxt = null;
		// may throw exceptions (dealt with in login())
 	    ctxt = fedoraSSL.getInitializedSSLContext();
		// set up the proper socket
		HttpsURLConnection.setDefaultSSLSocketFactory(ctxt.getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}
	
	/**
	 * As of 2011-02-08 we need to use a different URL for SSL login. This
	 * method sets the server URL appropriately.
	 * 
	 * @throws KojiLoginException
	 */
	private void setupSSLLoginXMLRPCConfig() throws KojiLoginException {
		if (this.xmlRpcConfig == null) {
			throw new KojiLoginException(new IllegalStateException(
					KojiText.xmlRPCconfigNotInitialized));
		}
		URL sslLoginUrl = null;
		try {
			sslLoginUrl = new URL(this.kojiHubUrl.toString() + "/ssllogin"); //$NON-NLS-1$
		} catch (MalformedURLException e) {
			// Ignore. If hub URL was invalid an exception should have been
			// thrown earlier.
		}
		this.xmlRpcConfig.setServerURL(sslLoginUrl);
	}
	
}
