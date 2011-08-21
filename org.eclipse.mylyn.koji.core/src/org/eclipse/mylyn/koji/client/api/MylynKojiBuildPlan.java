package org.eclipse.mylyn.koji.client.api;

import org.eclipse.mylyn.builds.internal.core.Build;
import org.eclipse.mylyn.builds.internal.core.BuildFactory;
import org.eclipse.mylyn.builds.internal.core.BuildPlan;
import org.eclipse.mylyn.koji.client.internal.utils.KojiBuildInfoParsingUtility;

@SuppressWarnings("restriction")
public class MylynKojiBuildPlan extends BuildPlan {
	private KojiPackage pack;
	
	public MylynKojiBuildPlan() {
		super();
	}

	public KojiPackage getPack() {
		return pack;
	}

	public void setPack(KojiPackage pack) {
		this.pack = pack;
	}
	
	public MylynKojiBuildPlan createWorkingCopy() {
		MylynKojiBuildPlan plan = new MylynKojiBuildPlan();
		plan.setId(new String(super.id));
		plan.setDescription(new String(super.description));
		plan.setName(new String(super.name));
		Build b = (Build) BuildFactory.eINSTANCE.createBuild();
		if((pack.getRecentBuilds().size() > 0) && (pack.getRecentBuilds().get(0) != null))
			b = KojiBuildInfoParsingUtility.cloneKojiBuildInfoContentToIBuild(this.pack.getRecentBuilds().get(0), b);
		plan.setLastBuild(b);
		plan.setPack(this.pack.clone());
		return plan;
	}
	
}
