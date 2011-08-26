package org.eclipse.mylyn.koji.connector;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.mylyn.koji.client.api.KojiPackage;

public class KojiConfiguration implements Serializable {

	private static final long serialVersionUID = -5503993513785634562L;

	private Map<Integer, KojiPackage> packageByID = new HashMap<Integer,KojiPackage>();

	public Map<Integer, KojiPackage> getPackageByID() {
		return packageByID;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("KojiConfiguration [packageByID=");
		builder.append(packageByID);
		builder.append("]");
		return builder.toString();
	}
} 