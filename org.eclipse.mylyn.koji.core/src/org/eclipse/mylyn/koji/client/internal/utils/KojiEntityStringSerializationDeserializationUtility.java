package org.eclipse.mylyn.koji.client.internal.utils;

import java.io.IOException;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import org.eclipse.mylyn.koji.client.api.KojiBuildInfo;
import org.eclipse.mylyn.koji.client.api.KojiPackage;
import org.eclipse.mylyn.koji.client.api.KojiTask;

public class KojiEntityStringSerializationDeserializationUtility {

	public static String serializeKojiEntityToString(Serializable in) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(in);
		oos.flush();
		oos.close();
		bos.close();
		return bos.toString();
	}
	
	private static Object deserializeObjectFromString(String in) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(in.getBytes());
		ObjectInputStream ois = new ObjectInputStream(bis);
		Object output = ois.readObject();
		ois.close();
		bis.close();
		return output;
	}
	
	public static KojiTask deserializeKojiTaskFromString(String in) throws IOException, ClassCastException {
		KojiTask task = null;
		try {
			Object result = deserializeObjectFromString(in);
			if(!(result instanceof KojiTask))
				throw new ClassCastException();
			task = (KojiTask)result;
		} catch (ClassNotFoundException cnfe) {
			throw new ClassCastException();
		}
		return task;
	}
	
	public static KojiBuildInfo deserializeKojiBuildInfoFromString(String in) throws IOException, ClassCastException {
		KojiBuildInfo build = null;
		try {
			Object result = deserializeObjectFromString(in);
			if(!(result instanceof KojiBuildInfo))
				throw new ClassCastException();
			build = (KojiBuildInfo)result;
		} catch (ClassNotFoundException cnfe) {
			throw new ClassCastException();
		}
		return build;
	}
	
	public static KojiPackage deserializeKojiPackageFromString(String in) throws IOException, ClassCastException {
		KojiPackage pack = null;
		try {
			Object result = deserializeObjectFromString(in);
			if(!(result instanceof KojiPackage))
				throw new ClassCastException();
			pack = (KojiPackage)result;
		} catch (ClassNotFoundException cnfe) {
			throw new ClassCastException();
		}
		return pack;
	}
}
