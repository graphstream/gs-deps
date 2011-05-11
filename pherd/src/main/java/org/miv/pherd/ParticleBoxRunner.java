package org.miv.pherd;

import org.miv.mbox.*;
import org.miv.pherd.ntree.*;

/**
 * Allows to put a ParticleBox inside a thread and to receive and send events
 * safely.
 *
 * @author Antoine Dutot
 * @since 2007
 */
@Deprecated
public class ParticleBoxRunner implements Runnable, MBoxListener
{
// Constants
	
	/**
	 * Messages understood by this runner.
	 *
	 * @author Antoine Dutot
	 * @since 2007
	 */
	public static enum InputProtocol
	{
		/**
		 * The data must contain [5]: this token, an identifier (Object), three
		 * coordinates (Float).
		 */
		ADD_PARTICLE,
		/**
		 * The data must contain [2]: this token, an identifier (Object).
		 */
		DEL_PARTICLE,
		/**
		 * The data must contain [1]: this token.
		 */
		EXIT;
	}
	
	/**
	 * Messages output by this object.
	 *
	 * @author Antoine Dutot
	 * @since 2007
	 */
	public static enum OutputProtocol
	{
		/**
		 * Data [5]: this token, an identifier (Object), three coordinates
		 * (Float).
		 */
		PARTICLE_ADDED,
		/**
		 * Data [2]: this token, an identifier (Object).
		 */
		PARTICLE_REMOVED,
		/**
		 * Data [5]: this token, an identifier (Object), three coordinates
		 * (Float).
		 */
		PARTICLE_MOVED,
		/**
		 * Data [5]: this token, an identifier (Object), a string, an object, a boolean.
		 */
		PARTICLE_ATTRIBUTE_CHANGED,
		/**
		 * Data [2]: this token, an integer (Integer).
		 */
		STEP_FINISHED,
		/**
		 * Data [7]: this token, 2 identifiers (Object) for the cell and its
		 * parent cell, the lo point (Anchor), the hi point (Anchor), a depth
		 * (Integer) and the cell index (Integer).
		 */
		CELL_ADDED,
		/**
		 * Data [2]: this token an identifier (Object).
		 */
		CELL_REMOVED,
		/**
		 * Data [4]: this token, an identifier (Object), a message (String) and
		 * arbitrary data (Object).
		 */
		CELL_DATA;
	}
	
// Attributes
	
	/**
	 * The particles and their environment.
	 */
	protected ParticleBox pbox;
	
	/**
	 * The input message box.
	 */
	protected MBoxStandalone inbox;
	
	/**
	 * Time to wait between each particle box step in milliseconds.
	 */
	protected int sleepMs = 1;
	
	/**
	 * This thread runs while this attribute is true.
	 */
	protected boolean loop = true;
	
	/**
	 * How to create particles?.
	 */
	protected ParticleFactory pfactory;
	
// Constructors
	
	public ParticleBoxRunner( int pmax, CellSpace space, CellData rootData, ParticleFactory pfactory )
	{
		this.pfactory = pfactory;

		inbox = new MBoxStandalone( this );
		pbox  = new ParticleBox( pmax, space, rootData );

	}
	
// Accessors

	/**
	 * The input message box of this runner. Send all commands that modify the
	 * particle box into this message box if your process runs in another thread.
	 * @return The particle box message box.
	 */
	public MBox getInbox()
	{
		return inbox;
	}
	
	/**
	 * The contained particle box.
	 * @return The particle box.
	 */
	public ParticleBox getParticleBox()
	{
		return pbox;
	}

// Commands

	/**
	 * Set the time to sleep between each step in milliseconds.
	 */
	public void setPriority( int ms )
	{
		sleepMs = ms;
	}
	
	public void processMessage( String from, Object[] data )
	{
		if( data.length > 0 && data[0] instanceof InputProtocol )
		{
			if( data[0] == InputProtocol.ADD_PARTICLE && data.length == 5 )
			{
				Object id = data[1];
				double  x  = (Float) data[2];
				double  y  = (Float) data[3];
				double  z  = (Float) data[4];
				
				pbox.addParticle( pfactory.newParticle( id, x, y, z ) );
			}
			else if( data[0] == InputProtocol.DEL_PARTICLE && data.length == 2 )
			{
				Object id = data[1];
				
				pbox.removeParticle( id );
			}
			else
			{
				throw new RuntimeException( "unknown input protocol " + data[0] );
			}
		}
		else
		{
			throw new RuntimeException( "unknown input protocol (len="+data.length+")" );
		}
	}

	/**
	 * Start a new thread that run this runner. Be careful, calling several
	 * times this method will launch several threads.
	 */
	public void start()
	{
		Thread t = new Thread( this );
		t.start();
	}
	
	/**
	 * Run the {@link #step()} method in a loop.
	 */
	public void run()
	{
		loop = true;

		while( loop )
		{
			step();
		}
	}

	/**
	 * Do one step of the iterative particle algorithm.
	 */
	public void step()
	{
		inbox.processMessages();
		pbox.step();
		sleep( sleepMs );		
	}
	
	protected void sleep( int ms )
	{
		try{ Thread.sleep( ms ); } catch( InterruptedException e ) {}
	}
	
	/**
	 * This class can listen at the events sent by a particle box and send
	 * them to an arbitrary (and eventually remote) message box.
	 *
	 * @author Antoine Dutot
	 * @since 2007
	 */
	public static class ParticleBoxListenerAdaptater implements
		ParticleBoxListener, NTreeListener
	{
	// Attributes

		/**
		 * Where to send messages ?.
		 */
		protected MBox outbox;
		
		/**
		 * Message sender name.
		 */
		protected String from;
		
	// Constructors
		
		/**
		 * New ParticleBox adapter that convert events from the particle box 
		 * to messages sent into a (eventually remote) message box.
		 * @param outbox Where to send particle messages.
		 * @param from The name of the sender this object will use to send messages.
		 */
		public ParticleBoxListenerAdaptater( MBox outbox, String from )
		{
			this.outbox = outbox;
			this.from   = from;
		}
		
	// Commands

		public void particleAdded( Object id, double x, double y, double z )
		{
			try
			{
				outbox.post( from, OutputProtocol.PARTICLE_ADDED, id, x, y, z );
			}
			catch( CannotPostException e )
			{
				e.printStackTrace();
			}
		}

		public void particleMoved( Object id, double x, double y, double z )
		{
			try
			{
				outbox.post( from, OutputProtocol.PARTICLE_MOVED, id, x, y, z );
			}
			catch( CannotPostException e )
			{
				e.printStackTrace();
			}
		}

		public void particleRemoved( Object id )
		{
			try
			{
				outbox.post( from, OutputProtocol.PARTICLE_REMOVED, id );
			}
			catch( CannotPostException e )
			{
				e.printStackTrace();
			}
		}
		
		public void particleAttributeChanged( Object id, String attribute, Object newValue, boolean removed )
		{
			try
			{
				outbox.post( from, OutputProtocol.PARTICLE_ATTRIBUTE_CHANGED, id, attribute, newValue, removed );
			}
			catch( CannotPostException e )
			{
				e.printStackTrace();
			}
		}

		public void stepFinished( int time )
		{
			try
			{
				outbox.post( from, OutputProtocol.STEP_FINISHED, time );
			}
			catch( CannotPostException e )
			{
				e.printStackTrace();
			}
		}

		public void cellAdded( Object id, Object parentId, Anchor lo, Anchor hi, int depth, int index )
		{
			try
			{
				outbox.post( from, OutputProtocol.CELL_ADDED, id, parentId, lo, hi, depth, index );
			}
			catch( CannotPostException e )
			{
				e.printStackTrace();
			}
		}

		public void cellRemoved( Object id )
		{
			try
			{
				outbox.post( from, OutputProtocol.CELL_REMOVED, id );
			}
			catch( CannotPostException e )
			{
				e.printStackTrace();
			}
		}
		
		public void cellData( Object id, String message, Object data )
		{
			try
			{
				outbox.post( from, OutputProtocol.CELL_DATA, id, message, data );
			}
			catch( CannotPostException e )
			{
				e.printStackTrace();
			}
		}
	}
}