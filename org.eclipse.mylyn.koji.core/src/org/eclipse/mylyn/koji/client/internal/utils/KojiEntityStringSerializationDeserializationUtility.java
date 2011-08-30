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
import org.apache.commons.codec.binary.Base64;

/**
 * Koji Entity to base 64 string serialization deserialization utility
 * 
 * @author Kiu Kwan Leung
 *
 */
public class KojiEntityStringSerializationDeserializationUtility {

	/**
	 * Method to parse all Koji entities (Serializable) into base 64 strings.
	 * @param in Serializable Koji entity.
	 * @return A serialized koji entity encoded as a base 64 string
	 * @throws IOException
	 */
	public static String serializeKojiEntityToBase64String(Serializable in) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(in);
		oos.flush();
		oos.close();
		bos.close();
		return Base64.encodeBase64String(bos.toByteArray());
	}
	
	private static Object deserializeObjectFromBase64String(String in) throws IOException, ClassNotFoundException {
		byte[] base64Input = Base64.decodeBase64(in);
		ByteArrayInputStream bis = new ByteArrayInputStream(base64Input);
		ObjectInputStream ois = new ObjectInputStream(bis);
		Object output = ois.readObject();
		ois.close();
		bis.close();
		return output;
	}
	
	/**
	 * Deserializes a base 64 string storing Koji task information into a KojiTask object.
	 * @param in A base 64 string storing Koji task information.
	 * @return A KojiTask object stored by the string.
	 * @throws IOException
	 * @throws ClassCastException
	 */
	public static KojiTask deserializeKojiTaskFromBase64String(String in) throws IOException, ClassCastException {
		KojiTask task = null;
		try {
			Object result = deserializeObjectFromBase64String(in);
			if(!(result instanceof KojiTask))
				throw new ClassCastException();
			task = (KojiTask)result;
		} catch (ClassNotFoundException cnfe) {
			throw new ClassCastException();
		}
		return task;
	}
	
	/**
	 * Deserializes a base 64 string storing Koji build information into a KojiBuildInfo object.
	 * @param in A base 64 string storing Koji build information.
	 * @return A KojiBuildInfo object stored by the string.
	 * @throws IOException
	 * @throws ClassCastException
	 */
	public static KojiBuildInfo deserializeKojiBuildInfoFromBase64String(String in) throws IOException, ClassCastException {
		KojiBuildInfo build = null;
		try {
			Object result = deserializeObjectFromBase64String(in);
			if(!(result instanceof KojiBuildInfo))
				throw new ClassCastException();
			build = (KojiBuildInfo)result;
		} catch (ClassNotFoundException cnfe) {
			throw new ClassCastException();
		}
		return build;
	}
	
	/**
	 * Deserializes a base 64 string storing Koji package information into a KojiPackage object.
	 * @param in A base 64 string storing Koji package information.
	 * @return A KojiPackage object stored by the string.
	 * @throws IOException
	 * @throws ClassCastException
	 */
	public static KojiPackage deserializeKojiPackageFromBase64String(String in) throws IOException, ClassCastException {
		KojiPackage pack = null;
		try {
			Object result = deserializeObjectFromBase64String(in);
			if(!(result instanceof KojiPackage))
				throw new ClassCastException();
			pack = (KojiPackage)result;
		} catch (ClassNotFoundException cnfe) {
			throw new ClassCastException();
		}
		return pack;
	}
}
