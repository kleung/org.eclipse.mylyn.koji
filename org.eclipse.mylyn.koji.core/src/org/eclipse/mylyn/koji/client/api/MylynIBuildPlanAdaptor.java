package org.eclipse.mylyn.koji.client.api;

import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.builds.core.BuildState;
import org.eclipse.mylyn.builds.core.BuildStatus;
import org.eclipse.mylyn.builds.core.IBuild;
import org.eclipse.mylyn.builds.core.IBuildPlan;
import org.eclipse.mylyn.builds.core.IBuildServer;
import org.eclipse.mylyn.builds.core.IHealthReport;
import org.eclipse.mylyn.builds.core.IOperation;
import org.eclipse.mylyn.builds.core.IParameterDefinition;

public class MylynIBuildPlanAdaptor implements IBuildPlan {

	private KojiPackage pack;
	
	public MylynIBuildPlanAdatpro() {
		super();
		this.pack = null;
	}
	
	public KojiPackage getPack() {
		return pack;
	}

	public void setPack(KojiPackage pack) {
		this.pack = pack;
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
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
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
	public List<IBuildPlan> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBuildPlan getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(IBuildPlan value) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getHealth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setHealth(int value) {
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
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setSelected(boolean value) {
		// TODO Auto-generated method stub

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
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDescription(String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBuild getLastBuild() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLastBuild(IBuild value) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<IParameterDefinition> getParameterDefinitions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IHealthReport> getHealthReports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BuildState> getFlags() {
		// TODO Auto-generated method stub
		return null;
	}

}
