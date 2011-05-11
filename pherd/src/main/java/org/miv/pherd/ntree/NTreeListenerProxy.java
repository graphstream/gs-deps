package org.miv.pherd.ntree;

import java.util.ArrayList;

import org.miv.mbox.MBoxListener;
import org.miv.mbox.MBoxStandalone;

/**
 * Helper class that allows to listen at a n-tree across thread boundaries.
 * 
 * @author Antoine Dutot
 */
public class NTreeListenerProxy implements NTreeListener, MBoxListener
{
// Attributes
	
	/**
	 * The message box used to exchange messages between the two threads.
	 */
	protected MBoxStandalone events;
	
	/**
	 * The listeners in another thread than this one.
	 */
	protected ArrayList<NTreeListener> listeners = new ArrayList<NTreeListener>();

// Constructors
	
	/**
	 * New listener proxy.
	 * All events coming from the n-tree will be buffered and be available in another
	 * thread as soon as {@link #checkEvents()} is called.
	 * @param tree The n-tree to listen at.
	 * @param replay Send events for all the n-tree cells already in the tree ?. 
	 */
	public NTreeListenerProxy( NTree tree, boolean replay )
	{
		events = new MBoxStandalone( this );
		
		connect( tree, replay );
	}
	
	public void disconnect( NTree tree )
	{
		tree.removeListener( this );
	}

	public void connect( NTree tree, boolean replay )
	{
		tree.addListener( this );
		
		if( replay )
			replayTree( tree );
	}
	
	/**
	 * Send events for all the cells already in the tree as if these events
	 * where generated normally.
	 * @param tree The n-tree.
	 */
	protected void replayTree( NTree tree )
	{
		replayTree( tree.getRootCell() );
	}
	
	/**
	 * Recursively replay the n-tree.
	 * @param cell The starting cell.
	 */
	protected void replayTree( Cell cell )
	{
		cellAdded( cell.getId(), cell.getParent() != null ? cell.getParent().getId() : null,
			cell.getSpace().getLoAnchor(), cell.getSpace().getHiAnchor(),
			cell.getDepth(), cell.getIndex() );
		
		if( ! cell.isLeaf() )
		{
			int n = cell.getSpace().getDivisions();
		
			for( int i=0; i<n; ++i )
				replayTree( cell.getSub( i ) );
		}
	}

	/**
	 * Add a listener to the events of the input particle box.
	 * @param listener The listener to call for each event in the input graph.
	 */
	public void addNTreeListener( NTreeListener listener )
	{
		listeners.add( listener );
	}
	
	/**
	 * Remove a listener. 
	 * @param listener The listener to remove.
	 */
	public void removeNTReeListener( NTreeListener listener )
	{
		int index = listeners.indexOf( listener );
		
		if( index >= 0 )
			listeners.remove( index );
	}
	
	/**
	 * This method must be called regularly to check if the particle box sent
	 * events. If some event occurred, the listeners will be called.
	 */
	public void checkEvents()
	{
		((MBoxStandalone)events).processMessages();
	}
	
// MBoxListener -- receive redirected events and re-send then to listeners.
	
	public void processMessage( String from, Object[] data )
    {
		if( data.length > 0 )
		{
			if( data[0].equals( "CA" )  )
			{
				Anchor lo    = (Anchor) data[3];
				Anchor hi    = (Anchor) data[4];
				int    depth = ((Number)data[5]).intValue();
				int    index = ((Number)data[6]).intValue();
				
				for( NTreeListener listener: listeners )
					listener.cellAdded( data[1], data[2], lo, hi, depth, index );
			}
			else if( data[0].equals( "CD" ) )
			{
				String msg = (String) data[2];
				
				for( NTreeListener listener: listeners )
					listener.cellData( data[1], msg, data[3] );
			}
			else if( data[0].equals( "CR" ) )
			{
				for( NTreeListener listener: listeners )
					listener.cellRemoved( data[1] );
			}
			else
			{
				// What to do ?

				if( data.length > 0 && data[0] instanceof String )
					throw new RuntimeException( "NTReeListenerProxy: uncaught message from "+
					        from+" : "+data[0]+"["+ data.length+"]" );
				else
					throw new RuntimeException( "NTreeListenerProxy: uncaught message from "+
					        from+" : ["+data.length+"]" );
			}
		}	    
    }
 
	public void cellAdded( Object id, Object parentId, Anchor lo, Anchor hi, int depth, int index )
    {
		events.post( "me", "CA", id, parentId, lo, hi, depth, index );
    }

	public void cellData( Object id, String message, Object data )
    {
		events.post( "me", "CD", id, message, data );
    }

	public void cellRemoved( Object id )
    {
		events.post( "me", "CR", id );
    }
}