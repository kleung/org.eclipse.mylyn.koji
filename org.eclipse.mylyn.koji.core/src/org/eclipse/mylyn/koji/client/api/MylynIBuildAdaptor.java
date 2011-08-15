package org.eclipse.mylyn.koji.client.api;

import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.builds.core.BuildState;
import org.eclipse.mylyn.builds.core.BuildStatus;
import org.eclipse.mylyn.builds.core.IArtifact;
import org.eclipse.mylyn.builds.core.IBuild;
import org.eclipse.mylyn.builds.core.IBuildCause;
import org.eclipse.mylyn.builds.core.IBuildPlan;
import org.eclipse.mylyn.builds.core.IBuildServer;
import org.eclipse.mylyn.builds.core.IChangeSet;
import org.eclipse.mylyn.builds.core.IOperation;
import org.eclipse.mylyn.builds.core.ITestResult;
import org.eclipse.mylyn.builds.core.IUser;

@SuppressWarnings("restriction")
public class MylynIBuildAdaptor implements IBuild {

	private KojiBuildInfo build;
	private KojiTask task;
	
	public MylynIBuildAdaptor() {
		super();
		this.build = null;
		this.task = null;
	}
	
	
	
	public KojiBuildInfo getBuild() {
		return build;
	}



	public void setBuild(KojiBuildInfo build) {
		this.build = build;
	}



	public KojiTask getTask() {
		return task;
	}



	public void setTask(KojiTask task) {
		this.task = task;
	}



	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUrl(String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<IOperation> getOperations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IStatus getElementStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setElementStatus(IStatus value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Date getRefreshDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRefreshDate(Date value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getBuildNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBuildNumber(int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getTimestamp() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTimestamp(long value) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDuration(long value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDisplayName(String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public BuildState getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setState(BuildState value) {
		// TODO Auto-generated method stub

	}

	@Override
	public BuildStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStatus(BuildStatus value) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<IArtifact> getArtifacts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IChangeSet getChangeSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setChangeSet(IChangeSet value) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBuildPlan getPlan() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPlan(IBuildPlan value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLabel(String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBuildServer getServer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setServer(IBuildServer value) {
		// TODO Auto-generated method stub

	}

	@Override
	public ITestResult getTestResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTestResult(ITestResult value) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<IUser> getCulprits() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSummary() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSummary(String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<IBuildCause> getCause() {
		// TODO Auto-generated method stub
		return null;
	}

}
