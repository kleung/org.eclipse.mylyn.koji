package org.eclipse.mylyn.koji.client.api;

import org.eclipse.mylyn.builds.internal.core.BuildPlan;

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
		
		return plan;
	}
	
}
