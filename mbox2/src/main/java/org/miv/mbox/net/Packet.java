package org.miv.mbox.net;

import java.io.*;

/**
 * Packet to encapsulate exchanged objects between senders and receivers.
 * 
 * <p>
 * Each object sent is encapsulated inside such a packet. It allows to send
 * meta-data (actually the sender and receiver names) with the data.
 * </p>
 *
 * @author Antoine Dutot
 * @since 20040624
 */
public class Packet
	implements Serializable
{
// Attributes

	private static final long serialVersionUID = 4170706444856161456L;

	/**
	 * Name of the sender.
	 */
	public String from;

	/**
	 * Name of the receiver.
	 */
	public String to;

	/**
	 * Data of the message.
	 */
	public Object data[];

// Constructors

	/**
	 * New packet.
	 * @param from Packet sender name.
	 * @param to Packet receiver name.
	 * @param data Data objects.
	 */
	public
	Packet( String from, String to, Object data[] )
	{
		this.from = from;
		this.to   = to;
		this.data = data;
	}

// Access

	/**
	 * Sender of the message.
	 * @return The sender name.
	 */
	public String
	getFrom()
	{
		return from;
	}

	/**
	 * Receiver of the message.
	 * @return The receiver name.
	 */
	public String
	getTo()
	{
		return to;
	}

	/**
	 * Message object.
	 * @return The data object.
	 */
	public Object[]
	getMessage()
	{
		return data;
	}
}