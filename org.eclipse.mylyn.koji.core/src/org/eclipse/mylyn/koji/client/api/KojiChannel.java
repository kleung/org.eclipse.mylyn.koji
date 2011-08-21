package org.eclipse.mylyn.koji.client.api;

import java.io.Serializable;

/**
 * Class representing channel as returned by getChannel XMLRPC call.
 * 
 * Author: Kiu Kwan Leung (Red Hat)
 */
@SuppressWarnings("rawtypes")
public class KojiChannel implements Comparable, Cloneable, Serializable {

	private static final long serialVersionUID = 5352832980494748397L;
	private int id; 		// channel ID
	private String name;	// channel name

	/**
	 * Default constructor - java bean class style
	 */
	public KojiChannel() {
		super();
	}

	/**
	 * Gets the ID of the channel.
	 * 
	 * @return Channel ID.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the ID of the channel.
	 * 
	 * @param id
	 *            Channel ID.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the name of the channel.
	 * 
	 * @return Channel name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the channel.
	 * 
	 * @param name
	 *            Channel name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Calculates and returns the hashcode of the KojiChannel object.
	 * 
	 * @return Hashcode of the KojiChannel object.
	 */
	@Override
	public int hashCode() {
		return (31 * this.id) + this.name.hashCode();
	}

	/**
	 * Checks whether a given KojiChannel object instance has the same ID as
	 * this.
	 * 
	 * @return Boolean determining whether this equals to the given channel.
	 */
	@Override
	public boolean equals(Object channel) throws ClassCastException {
		if (!(channel instanceof KojiChannel))
			throw new ClassCastException();
		KojiChannel compare = (KojiChannel) channel;
		return this.id == compare.getId();
	}

	/**
	 * Compares the ID of this and a given channel.
	 * 
	 * @return An int value, -1 for this has a smaller ID than the given
	 *         channel, 0 for this's ID is equal to the given channel's, 1 for
	 *         this's has a greater ID than the given channel's.
	 */
	@Override
	public int compareTo(Object channel) throws ClassCastException {
		boolean equals = this.equals(channel);
		if (equals)
			return 0;
		else {
			KojiChannel compare = (KojiChannel) channel;
			return ((this.id > compare.getId()) ? 1 : -1);
		}
	}
	
	/**
	 * Clones the KojiChannel object itself.
	 * 
	 * @return A clone of the KojiChannel object.
	 */
	@Override
	public KojiChannel clone() {
		KojiChannel channel = new KojiChannel();
		channel.setId(this.id);
		if(this.name != null)
			channel.setName(new String(this.name));
		else
			channel.setName(null);
		return channel;
	}
}
