package org.miv.mbox;

import java.util.*;

/**
 * MBox default implementation usable as base class.
 * 
 * <p>
 * The goal of this implementation is to provide a base to construct other
 * classes able to handle messages coming from different sources and threads.
 * The presupposed in this class is that the inheriting class will check
 * messages at regular time using the method {@link #processMessages()}.
 * </p>
 *
 * @see org.miv.mbox.MBox
 * @author Antoine Dutot
 * @since 20041102
 */
public abstract class
	MBoxBase
implements
	MBox, MBoxListener
{
// Attributes

	/**
	 * Temporary array to store messages when sent to the listener to avoid
	 * blocking the Receiver that can post messages during the execution of
	 * messages.
	 */
	protected ArrayList<Message> mtmp = new ArrayList<Message>();

	/**
	 * List of unread messages.
	 */
	protected ArrayList<Message> messages = new ArrayList<Message>();

// Constructors

	/**
	 * New empty Message Box.
	 */
	public MBoxBase()
	{
	}

// Constructors
	
	
// Access
	
	public synchronized boolean isMBoxEmpty()
	{
		return( messages.size() <= 0 );
	}
	
	public synchronized int getMessageCount()
	{
		return messages.size();
	}
	
// Command
	
	public synchronized void post( String from, Object... data )
	{
		messages.add( new Message( from, data ) );
	}
	
	/**
	 * Process all messages. Call this method at regular intervals to empty the
	 * message box. This method will call
	 * {@link #processMessage(String, Object[])} for each message in the box.
	 */
	public void processMessages()
	{
		int n;
		
		// We first swap the messages with a temporary buffer in order to let
		// the synchronized block be as short as possible. This avoids that
		// sending threads be blocked a long time when posting to this object.
		// Indeed, we do not know the time used by processMessage(), but it can
		// be quite long.
		
		synchronized( this )
		{
			ArrayList<Message> tmp = messages;
			messages = mtmp;
			mtmp = tmp;
		}
		
		// Then we really process the messages in our own thread without
		// blocking using the swapped array, the other array is free to
		// receive messages from another thread.
		
		n = mtmp.size();
		
		for( int i=0; i<n; ++i )
		{
			Message msg = mtmp.get( i );
			
			processMessage( msg.from, msg.data );
		}
		
		if( n > 0 )
			mtmp.clear();
	}
	
	/**
	 * Like {@link #processMessages()} but instead of calling the
	 * {@link #processMessage(String, Object[])} method for each message, returns the list of
	 * pending messages. The messages are removed from the list of pending messages and therefore
	 * will not be processed by the next calls to {@link #processMessages()}.
	 * @return A list of all messages not yet processed, removed from the list of pending messages.
	 */
	public ArrayList<Message> popPendingMessages()
	{
		int n;
		ArrayList<Message> m = new ArrayList<Message>();
		
		synchronized( this )
		{
			n = messages.size();
			
			if( n > 0 )
			{
				m.ensureCapacity( n );
				m.addAll( messages );
				messages.clear();
			}
		}
		
		return m;
	}
	
	/**
	 * Method to override to process each incoming message.
	 * @param from The address of the sender.
	 * @param data The data sent by the sender.
	 */
	public abstract void processMessage( String from, Object[] data );

// Inner classes

/**
 * Simple message container.
 */
public static class Message
{
	public String from;
	public Object data[];
	protected Message( String from, Object data[] ) { this.from = from; this.data = data; }
	public String getFrom() { return from; }
	public Object[] getData() { return data; }
}
	
}