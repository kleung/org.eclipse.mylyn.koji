package org.eclipse.mylyn.koji.SSL;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.commons.ssl.KeyMaterial;
import org.apache.commons.ssl.TrustChain;
import org.apache.commons.ssl.TrustMaterial;
import org.eclipse.osgi.util.NLS;

import org.eclipse.mylyn.koji.messages.KojiText;

/**
 * Helper class for Fedora/Koji related SSL things.
 * Uses org.apache.commons.ssl (from not-yet-commons).
 */
public class MylynSSL {
	
	/**
	 * Constant returned when unable to determine the username from the Fedora
	 * SSL certificate.
	 */
	public static final String UNKNOWN_USER = "anonymous"; //$NON-NLS-1$
	
	/**
	 * Default certificate path.
	 */
	public static final String DEFAULT_CERT_FILE = System.getProperty("user.home") //$NON-NLS-1$
			+ File.separatorChar + ".fedora.cert"; //$NON-NLS-1$
	/**
	 * Default upload CA cert.
	 */
	public static final String DEFAULT_UPLOAD_CA_CERT = System.getProperty("user.home") //$NON-NLS-1$
			+ File.separatorChar + ".fedora-upload-ca.cert"; //$NON-NLS-1$
	/**
	 * Default server CA cert.
	 */
	public static final String DEFAULT_SERVER_CA_CERT = System.getProperty("user.home") //$NON-NLS-1$
			+ File.separatorChar + ".fedora-server-ca.cert"; //$NON-NLS-1$
	
	private File fedoraCert;
	private File fedoraUploadCert;
	private File fedoraServerCert;
	private boolean allCertsExist = false;
	
	/**
	 * Create a Mylyn SSL object from given cert files. The use of this
	 * constructor is discouraged. Use
	 * {@link FedoraSSLFactory#getInstance()} instead.
	 * 
	 * @param fedoraCert
	 * @param fedoraUploadCert
	 * @param fedoraServerCert
	 */
	MylynSSL(File fedoraCert, File fedoraUploadCert, File fedoraServerCert) {
		this.fedoraCert = fedoraCert;
		this.fedoraServerCert = fedoraServerCert;
		this.fedoraUploadCert = fedoraUploadCert;
		if (fedoraCert.exists() && fedoraServerCert.exists()
				&& fedoraUploadCert.exists()) {
			this.allCertsExist = true;
		}
	}
	
	/**
	 * Set up an SSLContext, and initialize it properly.
	 * 
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @return The initialized SSLConext instance.
	 * @throws FileNotFoundException
	 *             If one of the three certificates is missing.
	 */
	public SSLContext getInitializedSSLContext() throws GeneralSecurityException,
		FileNotFoundException, IOException  {
		if (!allCertsExist) {
			Object[] bindings = { fedoraCert.getAbsolutePath(),
					fedoraServerCert.getAbsolutePath(),
					fedoraUploadCert.getAbsolutePath() };
			throw new FileNotFoundException(NLS.bind(
					KojiText.MylynSSL_certificatesMissingError,
					bindings));
		}
		TrustChain tc = getTrustChain();

		KeyMaterial kmat = getFedoraCertKeyMaterial();

		SSLContext sc = SSLContext.getInstance("SSL"); //$NON-NLS-1$

		sc.init((KeyManager[]) kmat.getKeyManagers(), (TrustManager[]) tc
				.getTrustManagers(), new java.security.SecureRandom());
		return sc;
	}
	
	/**
	 * 
	 * @return
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	private TrustChain getTrustChain()
			throws GeneralSecurityException, IOException {
		TrustChain tc = new TrustChain();
		tc.addTrustMaterial(new TrustMaterial(fedoraUploadCert));
		tc.addTrustMaterial(new TrustMaterial(fedoraServerCert));
		return tc;
	}
	
	/**
	 * Retrieve key material from fedoraCert as specified by constructor.
	 * 
	 * @return The key material.
	 * @throws GeneralSecurityException
	 * @throws FileNotFoundException
	 *             If one of the three certificates is missing.
	 * @throws IOException
	 */
	public KeyMaterial getFedoraCertKeyMaterial()
			throws GeneralSecurityException, FileNotFoundException, IOException {
		if (!allCertsExist) {
			Object[] bindings = { fedoraCert.getAbsolutePath(),
					fedoraServerCert.getAbsolutePath(),
					fedoraUploadCert.getAbsolutePath() };
			throw new FileNotFoundException(NLS.bind(
					KojiText.MylynSSL_certificatesMissingError,
					bindings));
		}
		KeyMaterial kmat = new KeyMaterial(fedoraCert, fedoraCert,
				new char[0]);
		return kmat;
	}
}
