package org.eclipse.mylyn.koji.connector;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.eclipse.mylyn.builds.core.spi.AbstractConfigurationCache;

@SuppressWarnings("restriction")
public class KojiConfigurationCache extends
		AbstractConfigurationCache<KojiConfiguration> {

	public KojiConfigurationCache(File cacheFile) {
		super(cacheFile);
	}
	
	public KojiConfigurationCache() {
		super();
	}
	
	@Override
	protected KojiConfiguration createConfiguration() {
		return new KojiConfiguration();
	}

	@Override
	protected KojiConfiguration readConfiguration(ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		return (KojiConfiguration)in.readObject();
	}

}
