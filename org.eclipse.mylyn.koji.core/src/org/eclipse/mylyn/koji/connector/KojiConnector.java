package org.eclipse.mylyn.koji.connector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.builds.core.spi.BuildConnector;
import org.eclipse.mylyn.builds.core.spi.BuildServerBehaviour;
import org.eclipse.mylyn.commons.repositories.RepositoryLocation;

/**
 * Koji Connector
 * 
 * @author Kiu Kwan Leung (Red Hat)
 *
 */
@SuppressWarnings("restriction")
public class KojiConnector extends BuildConnector {
	
	@Override
	public BuildServerBehaviour getBehaviour(RepositoryLocation location)
			throws CoreException {
		KojiServerBehavior behavior = new KojiServerBehavior(location);
		return behavior;
	}

}
