package org.eclipse.mylyn.koji.SSL;

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

import java.io.File;

/**
 * Factory for MylynSSL.
 */
public class MylynSSLFactory {

	/**
	 * Create a new MylynSSL instance using the default location of
	 * certificates.
	 * 
	 * @return The new instance.
	 */
	public static MylynSSL getInstance() {
		MylynSSL newInstance = new MylynSSL(
				new File(MylynSSL.DEFAULT_CERT_FILE),
				new File(MylynSSL.DEFAULT_UPLOAD_CA_CERT),
				new File(MylynSSL.DEFAULT_SERVER_CA_CERT));
		return newInstance;
	}
	
	/**
	 * Instantiate a MylynSSL object given the certificate files.
	 * 
	 * @param fedoraCert
	 * @param fedoraUploadCert
	 * @param fedoraServerCert
	 * @return A MylynSSL instance.
	 */
	public static MylynSSL getInstance(File fedoraCert, File fedoraUploadCert,
			File fedoraServerCert) {
		return new MylynSSL(fedoraCert, fedoraUploadCert, fedoraServerCert);
	}
}
