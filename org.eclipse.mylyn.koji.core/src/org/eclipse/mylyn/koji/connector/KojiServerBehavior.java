package org.eclipse.mylyn.koji.connector;

import java.io.Reader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.builds.core.IBuild;
import org.eclipse.mylyn.builds.core.IBuildPlan;
import org.eclipse.mylyn.builds.core.spi.BuildPlanRequest;
import org.eclipse.mylyn.builds.core.spi.BuildServerBehaviour;
import org.eclipse.mylyn.builds.core.spi.BuildServerConfiguration;
import org.eclipse.mylyn.builds.core.spi.GetBuildsRequest;
import org.eclipse.mylyn.builds.core.spi.RunBuildRequest;
import org.eclipse.mylyn.builds.core.spi.GetBuildsRequest.Kind;
import org.eclipse.mylyn.builds.internal.core.Build;
import org.eclipse.mylyn.commons.core.IOperationMonitor;
import org.eclipse.mylyn.commons.repositories.RepositoryLocation;
import org.eclipse.mylyn.koji.client.api.KojiBuildInfo;
import org.eclipse.mylyn.koji.client.api.KojiSSLHubClient;
import org.eclipse.mylyn.koji.client.api.KojiTask;
import org.eclipse.mylyn.koji.client.api.MylynKojiBuildPlan;
import org.eclipse.mylyn.koji.client.api.errors.KojiClientException;
import org.eclipse.mylyn.koji.client.api.errors.KojiLoginException;
import org.eclipse.mylyn.koji.client.internal.utils.KojiBuildInfoParsingUtility;
import org.eclipse.mylyn.koji.client.internal.utils.KojiTaskParsingUtility;
import org.eclipse.mylyn.koji.core.KojiCorePlugin;

@SuppressWarnings("restriction")
public class KojiServerBehavior extends BuildServerBehaviour {

	private KojiSSLHubClient client;
	
	public KojiServerBehavior(RepositoryLocation location) {
		try {
			this.client = new KojiSSLHubClient(location.getUrl());
		} catch (MalformedURLException e) {
			//should not happen
		}
	}
	

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
		//extract plan(koji package) and its id.
		IBuildPlan plan = request.getPlan();
		Kind kind = request.getKind();
		List<IBuild> buildList= new ArrayList<IBuild>();
		List<KojiBuildInfo> buildInfoList = null;
		if((plan != null) && (plan instanceof MylynKojiBuildPlan))//this will change after Build class is revised.
			buildInfoList = ((MylynKojiBuildPlan)plan).getPack().getRecentBuilds();
		if(!((buildInfoList != null) && (buildInfoList.size() > 0))) {//needs to query koji for the list.
			int packID = 0;
			try {
				packID = Integer.parseInt(plan.getId());
			} catch (NumberFormatException e) {
				//should not happen...
				throw KojiCorePlugin.toCoreException(e);
			}
			int limit = (kind == Kind.LAST) ? 1 : -1;
			try {
				this.client.login();
				buildInfoList = this.client.listBuildOfUserByKojiPackageIDAsList(
						packID, limit);
				this.client.logout();
			} catch (KojiClientException e) {
				throw KojiCorePlugin.toCoreException(e);
			} catch (KojiLoginException kle) {
				throw KojiCorePlugin.toCoreException(kle);
			}
		}//filter the results
		if ((buildInfoList != null) && (buildInfoList.size() > 0)) {
			if(kind == Kind.SELECTED) {
				//select the selected one out of the buildInfoList
				int buildID = 0;
				try {
					Integer.parseInt(request.getIds().iterator().next());
				} catch (NumberFormatException e) {
					throw KojiCorePlugin.toCoreException(e);
				}
				Object target = null;
				for(int icounter = 0; ((icounter < buildInfoList.size()) && target == null); icounter++) {
					KojiBuildInfo build = buildInfoList.get(icounter);
					KojiTask task = build.getTask();
					if(task.getId() == buildID)
						target = task;
					else if(build.getBuildId() == buildID)
						target = build;
					else {}
				}
				if(target == null)//cannot be found
					return buildList;
				IBuild build = super.createBuild();
				if(target instanceof KojiBuildInfo)
					build = KojiBuildInfoParsingUtility.cloneKojiBuildInfoContentToIBuild((KojiBuildInfo)target, build);		
				else
					build = KojiTaskParsingUtility.cloneKojiTaskContentToIBuild((KojiTask)target, build);
				build.setPlan(plan);
				buildList.add(build);
			} else {//all or last
				//prepare the IBuild list
				for (int icounter = 0; icounter < buildInfoList.size(); icounter++) {
					IBuild b = super.createBuild();
					b.setPlan(plan);
					buildList.add(b);
				}
				buildList = KojiBuildInfoParsingUtility
					.cloneKojiBuildInfoListToIBuildList(buildInfoList,
							buildList);
			}
		}
		return buildList;
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
		//take the plan out of the build, go to the most recent build or task
		//if the task or most recent build's task exists, query the outputs
		//otherwise, fail it with exception.
		return null;
	}

	@Override
	public List<IBuildPlan> getPlans(BuildPlanRequest request,
			IOperationMonitor monitor) throws CoreException {
		List<IBuildPlan> planList = new ArrayList<IBuildPlan>();
		List<String> idStringList = request.getPlanIds();
		List<Integer> idList = new ArrayList<Integer>();
		//convert list of string ids to list of integer ids.
		for(String s : idStringList) {
			int id = 0;
			try {
				id = Integer.parseInt(s);
			} catch (NumberFormatException e) {
				//should not happen...
				throw KojiCorePlugin.toCoreException(e);
			}
			idList.add(new Integer(id));
		}
		//need a preference page or something to limit the build query count...
		
		
		return planList;
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
		//resubmit a task if the plan contained by the request has a task associated with
		//like task or most recent build's task
		//throw exception otherwise.
	}

	@Override
	public IStatus validate(IOperationMonitor monitor) throws CoreException {
		//log in, try getting session info, logout, anything exception happens, fail it
		Map<String, ?> sessionInfo = null;
		try {
			this.client.login();
			sessionInfo = this.client.getSessionInfoAsMap();
			this.client.logout();
		} catch (KojiClientException kce) {
			KojiCorePlugin.toCoreException(kce);
		} catch (KojiLoginException kle) {
			KojiCorePlugin.toCoreException(kle);
		}
		if(sessionInfo != null)
			return Status.OK_STATUS;
		else
			throw KojiCorePlugin.toCoreException(new Exception("Sever did not respond properly"));
	}

}
