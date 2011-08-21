package org.eclipse.mylyn.koji.connector;

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.builds.core.IBuild;
import org.eclipse.mylyn.builds.core.IBuildPlan;
import org.eclipse.mylyn.builds.core.spi.BuildPlanRequest;
import org.eclipse.mylyn.builds.core.spi.BuildServerBehaviour;
import org.eclipse.mylyn.builds.core.spi.BuildServerConfiguration;
import org.eclipse.mylyn.builds.core.spi.GetBuildsRequest;
import org.eclipse.mylyn.builds.core.spi.RunBuildRequest;
import org.eclipse.mylyn.builds.internal.core.Build;
import org.eclipse.mylyn.commons.core.IOperationMonitor;

public class KojiServerBehavior extends BuildServerBehaviour {

	public Build createBuild() {
		IBuild build = super.createBuild();
		if(build instanceof Build)
			return (Build) build;
		else
			return null;
	}
	
	@Override
	public List<IBuild> getBuilds(GetBuildsRequest request,
			IOperationMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BuildServerConfiguration getConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getConsole(IBuild build, IOperationMonitor monitor)
			throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IBuildPlan> getPlans(BuildPlanRequest request,
			IOperationMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BuildServerConfiguration refreshConfiguration(
			IOperationMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void runBuild(RunBuildRequest request, IOperationMonitor monitor)
			throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public IStatus validate(IOperationMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

}
