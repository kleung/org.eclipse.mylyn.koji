package org.eclipse.mylyn.koji.connector;

import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.builds.core.IBuild;
import org.eclipse.mylyn.builds.core.IBuildPlan;
import org.eclipse.mylyn.builds.core.spi.BuildPlanRequest;
import org.eclipse.mylyn.builds.core.spi.BuildServerBehaviour;
import org.eclipse.mylyn.builds.core.spi.BuildServerConfiguration;
import org.eclipse.mylyn.builds.core.spi.GetBuildsRequest;
import org.eclipse.mylyn.builds.core.spi.GetBuildsRequest.Scope;
import org.eclipse.mylyn.builds.core.spi.RunBuildRequest;
import org.eclipse.mylyn.builds.core.spi.GetBuildsRequest.Kind;
import org.eclipse.mylyn.builds.internal.core.Build;
import org.eclipse.mylyn.commons.core.IOperationMonitor;
import org.eclipse.mylyn.commons.repositories.RepositoryLocation;
import org.eclipse.mylyn.koji.client.api.KojiBuildInfo;
import org.eclipse.mylyn.koji.client.api.KojiPackage;
import org.eclipse.mylyn.koji.client.api.KojiSSLHubClient;
import org.eclipse.mylyn.koji.client.api.KojiTask;
import org.eclipse.mylyn.koji.client.api.MylynKojiBuildPlan;
import org.eclipse.mylyn.koji.client.api.errors.KojiClientException;
import org.eclipse.mylyn.koji.client.api.errors.KojiLoginException;
import org.eclipse.mylyn.koji.client.internal.utils.KojiBuildInfoParsingUtility;
import org.eclipse.mylyn.koji.client.internal.utils.KojiPackageParsingUtility;
import org.eclipse.mylyn.koji.client.internal.utils.KojiTaskParsingUtility;
import org.eclipse.mylyn.koji.core.KojiCorePlugin;
import org.eclipse.mylyn.koji.messages.KojiText;

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
		if((plan != null) && (plan instanceof MylynKojiBuildPlan))//TODO this will change after Build class is revised.
			buildInfoList = ((MylynKojiBuildPlan)plan).getPack().getRecentBuilds();
		if(!((buildInfoList != null) && (buildInfoList.size() > 0))) {//needs to query koji for the list.
			int packID = 0;
			try {
				packID = Integer.parseInt(plan.getId());
			} catch (NumberFormatException e) {
				//should not happen...
				throw KojiCorePlugin.toCoreException(e);
			}
			int limit = 0;
			if(kind == Kind.LAST)
				limit = 1;
			else if(kind == Kind.ALL) {
				if(request.getScope() == Scope.HISTORY) {
					//TODO limit should be taken from a configuration page...
					//TODO limit = SOMETHING;
				} else
					limit = -1;
			}
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
					if((task != null) && (task.getId() == buildID))
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
		IBuildPlan plan = build.getPlan();
		int id = 0;
		try {
			id = (Integer.parseInt(build.getId()));
		} catch (NumberFormatException e) {
			throw KojiCorePlugin.toCoreException(e);
		}
		Reader resultReader = null;
		String result = "";
		KojiTask targetTask = null;
		//find the build/task
		if(plan instanceof MylynKojiBuildPlan) {//TODO MylynKojiBuildPlan should be taken out when Mylyn Builds people finishes revising the BuildPlan class.
			KojiPackage pack = ((MylynKojiBuildPlan)plan).getPack();
			if(pack.getTask() != null) {
				//coming from fedora packager as a build editor viewing of a recently pushed task
				KojiTask task = pack.getTask();
				if(task.getId() != id) {
					//package doesn't contain the required information
					throw KojiCorePlugin.toCoreException(new Exception(KojiText.NoBuildOwnedByUserError));
				} else 
					targetTask = task;
			}else if(pack.getRecentBuilds() != null) {
				//coming from Mylyn Builds in the usual way
				//search for each build's task, if task w/ a matching id is found, query, otherwise, fail it
				Object target = null;
				List<KojiBuildInfo> buildList= pack.getRecentBuilds();
				for(int icounter = 0; ((icounter < buildList.size()) && (target == null)); icounter++) {
					KojiBuildInfo info = buildList.get(icounter);
					if(info.getTaskId() == id)
						target = info.getTask();
					else if(info.getBuildId() == id)
						target = info;
					else {}
				}
				if(target != null) {
					if(target instanceof KojiBuildInfo)
						throw KojiCorePlugin.toCoreException(new Exception(KojiText.ImportedTaskConsoleShowingError));
					else
						targetTask = (KojiTask)target;
				}
			} else {
				//package doesn't contain the required information - the user doesn't own any of the package's build
				throw KojiCorePlugin.toCoreException(new Exception(KojiText.NoBuildOwnedByUserError));
			}
		}
		if(targetTask != null) {
			//update the task by querying, then
			//query koji for its descendents' outputs, concatenate them and store in result.
			//otherwise, fail it
			KojiTask updatedTask = null;
			try {
				this.client.login();
				updatedTask = this.client.getTaskInfoByIDAsKojiTask(id);
				this.client.logout();
			} catch (KojiClientException kce) {
				throw KojiCorePlugin.toCoreException(kce);
			} catch (KojiLoginException kle) {
				throw KojiCorePlugin.toCoreException(kle);
			}
			if(updatedTask != null) {
				List<KojiTask> descendentList = updatedTask.getDescendents();
				if((descendentList != null) && (descendentList.size() > 0)) {
					result += (KojiText.outputHeader + "\n\n");
					for(int icounter = 0; icounter < descendentList.size(); icounter++) {
						KojiTask task = descendentList.get(icounter);
						int taskState = task.getTaskStateCode();
						if((taskState == 2) || (taskState == 5)) {//task finished successfully or failed
							Map<String, HashMap<String, Object>> outputMap = null;
							try {
								this.client.login();
								outputMap = this.client.listTaskOutputAsMap(task.getId());
								this.client.logout();
							}  catch (KojiClientException kce) {
								throw KojiCorePlugin.toCoreException(kce);
							} catch (KojiLoginException kle) {
								throw KojiCorePlugin.toCoreException(kle);
							}
							if((outputMap != null) && (outputMap.size() > 0)) {
								Set<String> keySet = outputMap.keySet();
								if(keySet.size() > 0) {
									String content = null;
									result += (task.getRpm() + ":\n\n");
									if(keySet.contains("build.log")) {
										result += "build.log:\n\n";
										content = this.downloadOutputContent(task.getId(), "build.log", outputMap);
										if(content != null)
											result += content;
										result += "\n\n";
									}
									if(keySet.contains("mock_output.log")) {
										result += "mock_output.log:\n\n";
										content = this.downloadOutputContent(task.getId(), "mock_output.log", outputMap);
										if(content != null)
											result += content;
										result += "\n\n";
									}
									if(keySet.contains("root.log")) {
										result += "root.log:\n\n";
										content = this.downloadOutputContent(task.getId(), "root.log", outputMap);
										if(content != null)
											result += content;
										result += "\n\n";
									}
									if(keySet.contains("state.log")) {
										result += "state.log:\n\n";
										content = this.downloadOutputContent(task.getId(), "state.log", outputMap);
										if(content != null)
											result += content;
										result += "\n\n";
									}
									result += "\n\n";
								}
							}
						}
					}
					result += (KojiText.outputFooter + "\n");
				}
			}
		}
		resultReader = new StringReader(result);
		return resultReader;
	}

	private String downloadOutputContent(int taskId, String fileName, Map<String, HashMap<String, Object>> data) throws CoreException {
		int textSize = 0;
		String content = null;
		try {
			textSize = Integer.parseInt((String)data.get(fileName).get("st_size"));
		} catch (NumberFormatException nfe) {
			throw KojiCorePlugin.toCoreException(nfe);
		}
		try {
			this.client.login();
			content = this.client.downloadTaskOutputAsString(taskId, fileName, 0, textSize);
			this.client.logout();
		}  catch (KojiClientException kce) {
			throw KojiCorePlugin.toCoreException(kce);
		} catch (KojiLoginException kle) {
			throw KojiCorePlugin.toCoreException(kle);
		}
		return content;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IBuildPlan> getPlans(BuildPlanRequest request,
			IOperationMonitor monitor) throws CoreException {
		//query koji for a list of user owned packages
		Object[] packArray = null;
		try {
			this.client.login();
			packArray = this.client.listPackagesOfUserAsObjectArray();
			this.client.logout();
		} catch (KojiLoginException kle) {
			throw KojiCorePlugin.toCoreException(kle);
		} catch (KojiClientException kce) {
			throw KojiCorePlugin.toCoreException(kce);
		}
		List<IBuildPlan> planList = new ArrayList<IBuildPlan>();
		if((packArray != null) && (packArray.length > 0)) {
			// extract list of IDs from request
			List<String> idStringList = request.getPlanIds();
			List<Map<String, Object>> packMapList = new ArrayList<Map<String, Object>>();
			// convert list of string ids to list of integer ids.
			// match against them and put into a new object array for parsing
			for (String s : idStringList) {
				int id = 0;
				try {
					id = Integer.parseInt(s);
				} catch (NumberFormatException e) {
					// should not happen...
					throw KojiCorePlugin.toCoreException(e);
				}
				Object targetMap = null;
				for(int icounter = 0; ((icounter < packArray.length) && (targetMap == null)); icounter++) {
					Map<String, Object> packMap = null;
					if(packArray[icounter] instanceof Map) {
						 packMap = (Map<String, Object>)packArray[icounter];
						 if(((Integer)packMap.get("id")).intValue() == id)
							 targetMap = packArray[icounter];
					}
				}
				if(targetMap != null)
					packMapList.add((Map<String, Object>)targetMap);
			}
			// TODO need a preference page or something to limit the build query
			// count...
			//TODO int limit = something;
			try {
				this.client.login();
				for(Map<String, Object> m : packMapList) {
					KojiPackage pack = KojiPackageParsingUtility.parsePackage(m, true, this.client, /*TODO limit*/-1, true);
					planList.add(KojiPackageParsingUtility.cloneKojiPackageContentToIBuildPlan(pack, this));
				}
				this.client.logout();
			} catch (KojiClientException kce) {
				throw KojiCorePlugin.toCoreException(kce);
			} catch (KojiLoginException kle) {
				throw KojiCorePlugin.toCoreException(kle);
			}
			// and return as a list of build plan.
		}
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
		// or most recent build's task and the task was failed or cancelled
		//throw exception otherwise - scratch/import builds and successful builds cannot be
		//resubmitted, please push a new build
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
			throw KojiCorePlugin.toCoreException(kce);
		} catch (KojiLoginException kle) {
			throw KojiCorePlugin.toCoreException(kle);
		}
		if(sessionInfo != null)
			return Status.OK_STATUS;
		else
			throw KojiCorePlugin.toCoreException(new Exception(KojiText.KojiValidationError));
	}

}
