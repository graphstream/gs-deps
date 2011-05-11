package org.miv.pherd;

import java.util.ArrayList;
import java.util.Iterator;

import org.miv.mbox.MBoxListener;
import org.miv.mbox.MBoxStandalone;
import org.miv.pherd.geom.Point3;

/**
 * Helper class that allows to listen at a particle box across thread boundaries.
 * 
 * @author Antoine Dutot
 */
public class ParticleBoxListenerProxy implements ParticleBoxListener, MBoxListener
{
// Attributes
	
	/**
	 * The message box used to exchange messages between the two threads.
	 */
	protected MBoxStandalone events;
	
	/**
	 * The listeners in another thread than this one.
	 */
	protected ArrayList<ParticleBoxListener> listeners = new ArrayList<ParticleBoxListener>();

// Constructors
	
	/**
	 * New listener proxy.
	 * All events coming from the particle box will be buffered and be available in another
	 * thread as soon as {@link #checkEvents()} is called.
	 * @param box The particle box to listen at.
	 * @param replay Send events for all the particles already in the particle box ?. 
	 */
	public ParticleBoxListenerProxy( ParticleBox box, boolean replay )
	{
		events = new MBoxStandalone( this );

		connect( box, replay );
	}
	
	public void disconnect( ParticleBox box )
	{
		box.removeParticleBoxListener( this );
	}
	
	public void connect( ParticleBox box, boolean replay )
	{
		box.addParticleBoxListener( this );

		if( replay )
			replayBox( box );
	}
	
	/**
	 * Send events for all the particles already in the particle box as if these events
	 * where generated normally.
	 * @param box The particle box.
	 */
	protected void replayBox( ParticleBox box )
	{
		Iterator<Object> particles = box.getParticleIdIterator();
		
		while( particles.hasNext() )
		{
			Particle p = box.getParticle( particles.next() );
			Point3   P = p.getPosition();
			
			particleAdded( p.getId(), P.x, P.y, P.z );
			
			Iterator<String> i = p.getAttributeKeyIterator();
			
			if( i != null )
			{
				while( i.hasNext() )
				{
					String key = i.next();
					Object val = p.getAttribute( key );
					
					particleAttributeChanged( p.getId(), key, val, false );
				}
			}
		}
	}

	/**
	 * Add a listener to the events of the input particle box.
	 * @param listener The listener to call for each event in the input graph.
	 */
	public void addParticleBoxListener( ParticleBoxListener listener )
	{
		listeners.add( listener );
	}
	
	/**
	 * Remove a listener. 
	 * @param listener The listener to remove.
	 */
	public void removeParticleBoxListener( ParticleBoxListener listener )
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
			if( data[0].equals( "PA" )  )
			{
				double  x  = ((Number)data[2]).doubleValue();
				double  y  = ((Number)data[3]).doubleValue();
				double  z  = ((Number)data[4]).doubleValue();
				
				for( ParticleBoxListener listener: listeners )
					listener.particleAdded( data[1], x, y, z );
			}
			else if( data[0].equals( "PAC" ) )
			{
				String  attribute = (String) data[2];
				boolean removed   = (Boolean) data[4];

				for( ParticleBoxListener listener: listeners )
					listener.particleAttributeChanged( data[1], attribute, data[3], removed );
			}
			else if( data[0].equals( "PM" ) )
			{
				double  x  = ((Number)data[2]).doubleValue();
				double  y  = ((Number)data[3]).doubleValue();
				double  z  = ((Number)data[4]).doubleValue();
				
				for( ParticleBoxListener listener: listeners )
					listener.particleMoved( data[1], x, y, z );
			}
			else if( data[0].equals( "PR" ) )
			{
				for( ParticleBoxListener listener: listeners )
					listener.particleRemoved( data[1] );				
			}
			else if( data[0].equals( "SF" ) )
			{
				int time = ((Number)data[1]).intValue();
				
				for( ParticleBoxListener listener: listeners )
					listener.stepFinished( time );				
			}
			else
			{
				// What to do ?

				if( data.length > 0 && data[0] instanceof String )
					throw new RuntimeException( "ParticleBoxListenerProxy: uncaught message from "+
					        from+" : "+data[0]+"["+ data.length+"]" );
				else
					throw new RuntimeException( "ParticleBoxListenerProxy: uncaught message from "+
					        from+" : ["+data.length+"]" );
			}
		}	    
    }
    
	public void particleAdded( Object id, double x, double y, double z )
    {
		events.post( "me", "PA", id, x, y, z );
    }

	public void particleAttributeChanged( Object id, String attribute, Object newValue, boolean removed )
    {
		events.post( "me", "PAC", id, attribute, newValue, removed );
    }

	public void particleMoved( Object id, double x, double y, double z )
    {
		events.post( "me", "PM", id, x, y, z );
    }

	public void particleRemoved( Object id )
    {
	    events.post( "me", "PR", id );
    }

	public void stepFinished( int time )
    {
	    events.post( "me", "SF", time );
    }
}