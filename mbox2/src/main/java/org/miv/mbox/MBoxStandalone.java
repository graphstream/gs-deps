package org.miv.mbox;

import java.util.ArrayList;

/**
 * Implementation of a message box to be used without having to inherit it.
 * 
 * <p>
 * This message box is a fully fledged box that can work by itself, receive
 * messages and dispatch them to a listener when invoked. It is the pending of
 * the {@link org.miv.mbox.MBoxBase} when it is not possible to inherit from it.
 * </p>
 *
 * @author Antoine Dutot
 * @since  28102006
 */
public class MBoxStandalone extends MBoxBase
{
// Attributes

	/**
	 * Listener for this message box. There can be only one listener per message
	 * box.
	 */
	protected ArrayList<MBoxListener> listeners = new ArrayList<MBoxListener>();

	/**
	 * Notify every threads waiting on this mbox monitor at each post?.
	 */
	protected boolean notifyOnPost = false;
	
// Constructors

	/**
	 * New empty message box dispatching messages to a given listener.
	 * @param listener The message reader.
	 */
	public MBoxStandalone( MBoxListener listener )
	{
		listeners.add( listener );
	}
	
	/**
	 * New empty message box. To use this, you must add a listener.
	 */
	public MBoxStandalone()
	{
	}

// Access

	/**
	 * Does the {@link #post(String,Object...)} method must notify any
	 * thread waiting on the monitor of this message box?.
	 */
	public boolean notifyOnPost()
	{
		return notifyOnPost;
	}

// Command

	/**
	 * Specify if each call to {@link #post(String, Object...)} should notify threads
	 * waiting on the monitor of this message box.
	 */
	public void setNotifyOnPost( boolean on )
	{
		notifyOnPost = on;
	}
	
	@Override
	public synchronized void post( String from, Object ... data )
	{
		super.post( from, data );
		
		if( notifyOnPost )
			notifyAll();
	}
	
	@Override
	public void processMessage( String from, Object data[] )
	{
		for( MBoxListener listener: listeners )
			listener.processMessage( from, data );
	}
	
	/**
	 * Set the listener of this message box. All messages will be sent to it
	 * when {@link #processMessages()} will be called.
	 * @param listener The message listener.
	 */
	@Deprecated
	public void setListener( MBoxListener listener )
	{
		listeners.add( listener );
	}
	
	/**
	 * Add a listener for this message box. All message will be sent to it
	 * when {@link #processMessages()} is called. Note that all listeners
	 * receive all messages. That is if there is N listeners, and the box
	 * received one message, the message is sent N times, once for each
	 * listener.
	 * @param listener The listener to add.
	 */
	public void addListener( MBoxListener listener )
	{
		listeners.add( listener );
	}

	/**
	 * Remove a listener.
	 * @param listener The listener to remove.
	 */
	public void removeListener( MBoxListener listener )
	{
		int index = listeners.indexOf( listener );
	
		if( index >= 0 )
			listeners.remove( index );
	}
}