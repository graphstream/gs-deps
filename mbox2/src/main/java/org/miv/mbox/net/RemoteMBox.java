package org.miv.mbox.net;

import java.io.IOException;

import org.miv.mbox.*;

/**
 * Representation of a remove message box.
 *
 * <p>
 * Instances of this class can be used to mimic the behaviour of a local message
 * box, allowing to post things in it, whereas the thing that lays under is a
 * {@link Sender} that transmit messages to a distant message box.
 * </p>
 *
 * @author Antoine Dutot
 * @since  20061212
 */
public class RemoteMBox implements MBox
{
// Attributes
	
	/**
	 * The sender.
	 */
	protected Sender sender;
	
	/**
	 * Name of the remote message box.
	 */
	protected String remoteMBoxName;
	
// Constructors

	/**
	 * New remote message box.
	 * @param sender Sender to the distant host containing the remove message
	 * 		box.
	 * @param remoteMBoxName Name of the distant message box on the distant
	 * 		host.
	 */
	public
	RemoteMBox( Sender sender, String remoteMBoxName )
	{
		this.sender         = sender;
		this.remoteMBoxName = remoteMBoxName;
	}
	
// Commands
	
	/**
	 * Post a message to the remote message box. As the post method in the
	 * message box cannot fail, this method throws a RuntimeException if
	 * something bad happen.
	 * @param from The name of the message sender.
	 * @param data A set of objects to send.
	 */
	public void post( String from, Object... data )
		throws CannotPostException
	{
		try
		{
			sender.send( from, remoteMBoxName, data );
		}
		catch( IOException e )
		{
			throw new CannotPostException( "cannot post to remote mbox: " + e.getMessage() );
		}
	}
}