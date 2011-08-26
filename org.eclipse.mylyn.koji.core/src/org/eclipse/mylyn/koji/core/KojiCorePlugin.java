package org.eclipse.mylyn.koji.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.koji.connector.KojiConnector;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class KojiCorePlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.mylyn.koji.core"; //$NON-NLS-1$

	// Connector kind
	public static final String CONNECTOR_KIND = "org.eclipse.mylyn.koji"; //NON-NLS-1$
	
	// The shared instance
	private static KojiCorePlugin plugin;
	
	private KojiConnector connector;
	
	/**
	 * The constructor
	 */
	public KojiCorePlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static KojiCorePlugin getDefault() {
		return plugin;
	}

	public KojiConnector getConnector() {
		if (connector == null) {
			connector = new KojiConnector();
		}
		return connector;
	}

	void setConnector(KojiConnector connector) {
		this.connector = connector;
	}
	
	public static CoreException toCoreException(Exception e) {
		return new CoreException(new Status(IStatus.ERROR, PLUGIN_ID, "Unexpected error: " + e.getMessage(), e));
	}
	
}
