package org.miv.mbox.net;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Create and cache senders automatically.
 *
 * <p>
 * A sender pool allows to avoid creating a {@link org.miv.mbox.net.Sender} for
 * each remote object we want to send message to, since one can use the same
 * sender for the same host. The sender pool remembers hosts and ports and their
 * associated sender. It can also create senders automatically for a given
 * {@link org.miv.mbox.net.MBoxLocator}.
 * </p>
 * 
 * @see org.miv.mbox.net.Sender
 * @see org.miv.mbox.net.Receiver
 * @author Antoine Dutot
 * @since 20040624
 */
public class SenderPool
{
// Attributes

	/**
	 * Map of senders. Each item is a pair (key,value) where the key is the
	 * sender locator host id ({@link org.miv.mbox.net.MBoxLocator#getHostId()})
	 * as a string and the value is the Sender instance.
	 */
	protected HashMap<String,Sender> senders = new HashMap<String,Sender>();

	protected boolean debug;
	
// Constructors

	/**
	 * Empty sender pool.
	 */
	public
	SenderPool()
	{
	}
	
	/**
	 * Empty sender pool.
	 * @param debug Activate debugging in all senders created.
	 */
	public
	SenderPool( boolean debug )
	{
		this.debug = debug;
	}

// Accessors

	/**
	 * Is there a cached connection to the hostname and port of the given
	 * locator.
	 * @param locator The locator to check.
	 * @return True if a sender is cached for this locator.
	 */
	public boolean
	hasSender( MBoxLocator locator )
	{
		return( senders.get( locator.getHostId() ) != null );
	}

	/**
	 * Get the sender for the host given by the given locator. Only the hostname
	 * and port of the locator are considered.
	 * @param locator The locator of the host to search.
	 * @return A sender to this host if cached, else null.
	 */
	public Sender
	getSender( MBoxLocator locator )
	{
		return( senders.get( locator.getHostId() ) );
	}

	/**
	 * Get the sender for the host given by the given hostname and port.
	 * @param hostname The hostname of the host to connect to.
	 * @param port The port to connect to.
	 * @return A sender to this host if cached, else null.
	 */
	public Sender
	getSender( String hostname, int port )
	{
		String id = "//" + hostname + ":" + port;
		return senders.get( id );
	}

// Commands

	/**
	 * Create a new sender to "//hostname:port" and cache it. If  a sender
	 * already exists it is used.
	 * @param hostname The hostname of the host to connect to.
	 * @param port The port to connect to.
	 */
	public Sender
	connect( String hostname, int port )
		throws IOException, UnknownHostException
	{
		String id     = "//" + hostname + ":" + port;
		Sender sender = senders.get( id );

		if( sender == null )
		{
			sender = new Sender( new MBoxLocator( hostname, port ), debug );
			senders.put( id, sender );
		}

		return sender;
	}

	/**
	 * Create a new sender for the host part of the given locator and cache it.
	 * If a sender already exists it is used.
	 * @param locator The locator used to create the connection.
	 */
	public Sender
	connect( MBoxLocator locator )
		throws IOException, UnknownHostException
	{
		String id     = locator.getHostId();
		Sender sender = senders.get( id );

		if( sender == null )
		{
			sender = new Sender( new MBoxLocator( locator.getHostname(), locator.getPort() ), debug );
			senders.put( id, sender );
		}

		return sender;
	}

	/**
	 * Send a message to a distant message box, first creating a sender if not
	 * already cached.
	 * @param from The sender object name.
	 * @param hostname The host name where the receiver box resides.
	 * @param port The port of the host where the receiver box resides.
	 * @param to The receiver box name.
	 * @param messages The message data.
	 */
	public void
	send( String from, String hostname, int port, String to, Object ... messages )
		throws IOException, UnknownHostException
	{
		Sender sender = connect( hostname, port );
		sender.send( from, to, messages );
	}

	/**
	 * Send a message to a distant message box, first creating a sender if not
	 * already cached.
	 * @param from The sender object name.
	 * @param to The location and name of the receiver box.
	 * @param messages The message data.
	 */
	public void
	send( String from, MBoxLocator to, Object ... messages )
		throws IOException, UnknownHostException
	{
		Sender sender = connect( to );
		sender.send( from, to.getName(), messages );
	}

	/**
	 * Send a prepacked message to a distant message box, first creating a
	 * sender if not already cached.
	 * @param to The location and name of the receiver box.
	 * @param packet The prepacket message.
	 */
	public void
	send( MBoxLocator to, Packet packet )
		throws IOException, UnknownHostException
	{
		Sender sender = connect( to );
		sender.send( packet );
	}
}