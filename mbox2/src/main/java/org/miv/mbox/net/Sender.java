package org.miv.mbox.net;

import java.io.*;
import java.net.*;
import java.nio.channels.*;

/**
 * Sends messages to message boxes.
 * 
 * <p>
 * A sender is an object able to send data to another host on a given port. It
 * is not necessary to create a distinct sender for each object we want to send
 * data to (and it would be a loss of space), only for each host and port we
 * need to send data to.
 * </p>
 * 
 * <p>
 * The {@link org.miv.mbox.net.SenderPool} allows to remember a set of senders
 * toward several hosts and ports. It can also create them.
 * </p>
 * 
 * <p>
 * Senders send data synchronously, that is, you wait while the data is sent,
 * however you do not wait until the data is received.
 * </p>
 * 
 * @see org.miv.mbox.net.SenderPool
 * @author Antoine Dutot
 * @since 20040624
 */
public class Sender
{
// Attributes

	/**
	 * Socket to the receiver.
	 */
	protected SocketChannel socket;

	/**
	 * Locator of the distant message box.
	 */
	protected MBoxLocator locator;

	/**
	 * Object output stream.
	 */
	protected ObjectOutputStream objectOut;

	/**
	 * Byte output stream.
	 */
	protected ByteArrayOutputStream byteArrayOut;

	/**
	 * Echo debugging messages?.
	 */
	protected boolean debug = false;

	/**
	 * Ah?!.
	 */
	protected int reset = 0;

	/**
	 * Oh?!.
	 */
	protected byte header[] = new byte[4];

// Constructors

	/**
	 * New sender toward the given receiver. The given locator must only contain
	 * an hostname and a port.
	 * @param locator MBoxLocator of the receiver.
	 */
	public Sender( MBoxLocator locator )
		throws IOException, UnknownHostException
	{
		this( locator, false );
	}
	
	/**
	 * New sender toward the given receiver. The given locator must only contain
	 * an hostname and a port.
	 * @param locator MBoxLocator of the receiver.
	 * @param debug Enable or disable debugging outputs on the console.
	 */
	public Sender( MBoxLocator locator, boolean debug )
		throws IOException, UnknownHostException
	{
		this.locator = locator;
		this.debug   = debug;
		
		InetSocketAddress addr = new InetSocketAddress( locator.getHostname(), locator.getPort() );

		socket = SocketChannel.open();
		socket.connect( addr );
		socket.configureBlocking( true );
		socket.finishConnect();

		if( debug )
			debug( "connect from %s:%d to %s:%d",
					socket.socket().getLocalAddress(),
					socket.socket().getLocalPort(),
					socket.socket().getInetAddress(),
					socket.socket().getPort() );

		byteArrayOut = new ByteArrayOutputStream( 4096 );	// Sufficient?
		objectOut    = new ObjectOutputStream( byteArrayOut );
		
		if( debug )
			debug( "ready" );
	}

// Access

	/**
	 * Locator of the distant receiver.
	 * @return The locator of a receiver.
	 */
	public MBoxLocator getLocator()
	{
		return locator;
	}

// Command

	/**
	 * Enable or disable debugging.
	 */
	public void setDebugOn( boolean on )
	{
		debug = on;
	}

	/**
	 * Send a message. This method encodes the messages and the destination and
	 * sender names into a packet and send it.
	 * @param from Sender identifier.
	 * @param to Receiver identifier.
	 * @param messages Message parameters.
	 */
	public void send( String from, String to, Object ... messages )
		throws IOException
	{
		// Encode the data to a buffer.

		send( new Packet( from, to, messages ) );
	}

	/**
	 * Send a data packet. This method is a shortcut, assuming you are able to
	 * build your own packets to send them. The
	 * {@link #send(String, String, Object[])} method uses it.
	 * @param packet The already packed message to send.
     */
	public void send( Packet packet )
		throws IOException
	{
		if( reset >= 10240 )
		{
			reset = 0;
			objectOut.reset();
		}

		objectOut.writeObject( packet );

		int size = byteArrayOut.size();

		OutputStream out = socket.socket().getOutputStream();

		header[0] = (byte) ( ( size >> 24 ) & 0xFF );
		header[1] = (byte) ( ( size >> 16 ) & 0xFF );
		header[2] = (byte) ( ( size >> 8  ) & 0xFF );
		header[3] = (byte) ( ( size       ) & 0xFF ); 
		// TODO: problem with bigEndian systems?

		if( debug )
			debug( "send (%s -> %s)", packet.from, packet.to );

		out.write( header );
		byteArrayOut.writeTo( out );

		if( debug )
			debug( "sent (%s -> %s)", packet.from, packet.to );
		
		reset++;

		byteArrayOut.reset();
	}

// Utilities

	protected static final String LIGHT_GREEN = "[32;1m";
	protected static final String RESET = "[0m";

	protected void
	debug( String message, Object ... data )
	{
		System.err.print( LIGHT_GREEN );
		System.err.printf( "[%s|", locator.toString() );
		System.err.print( RESET );
		System.err.printf( message, data );
		System.err.print( LIGHT_GREEN );
		System.err.print( "]" );
		System.err.println( RESET );
	}
}