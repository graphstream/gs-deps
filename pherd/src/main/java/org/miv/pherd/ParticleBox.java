package org.miv.pherd;

import java.util.*;
import org.miv.pherd.ntree.*;

/**
 * Container, handler, and animator for the particles.
 *
 * @author Antoine Dutot
 * @since 2007
 */
public class ParticleBox
{
// Attributes

	/**
	 * The list of particles.
	 */
	protected HashMap<Object,Particle> particles = new HashMap<Object,Particle>();

	/**
	 * The n-tree.
	 */
	protected NTree tree;
	
	/**
	 * Set of listeners.
	 */
	protected ArrayList<ParticleBoxListener> listeners = new ArrayList<ParticleBoxListener>();
	
	/**
	 * Current step.
	 */
	protected int time = 0;
	
// Constructors

	/**
	 * New particle box.
	 * @param pmax The maximum number of particles per cell before a subdivision
	 * occurs.
	 * @param universe The specific space defining the initial root node. 
	 * @param rootData The data specific to the particle simulation.
	 */
	public ParticleBox( int pmax, CellSpace universe, CellData rootData )
	{
		tree = new NTree( pmax, universe, rootData, this );
	}
	
// Access

	/**
	 * Access to a particle.
	 * @param id The particle unique identifier.
	 */
	public Particle getParticle( Object id )
	{
		return particles.get( id );
	}
	
	/**
	 * The internal space representation, a n-tree.
	 * @return The n-tree.
	 */
	public NTree getNTree()
	{
		return tree;
	}
	
	/**
	 * Number of particles in the box.
	 * @return The particle count.
	 */
	public int getParticleCount()
	{
		return particles.size();
	}
	
	/**
	 * Iterator on the set of particles identifiers.
	 * @return An object iterator.
	 */
	public Iterator<Object> getParticleIdIterator()
	{
		return particles.keySet().iterator();
	}
	
// Commands

	/**
	 * Add a particle to the list of active particles.
	 * @param particle The new particle.
	 * @throws IdAlreadyInUseException If a particle with the same identifier that
	 *         is not the same particle is added.
	 */
	public void addParticle( Particle particle )
		throws IdAlreadyInUseException
	{
		tree.addParticle( particle );
		Particle p = particles.put( particle.getId(), particle );
		
		if( p != null && p != particle )
			throw new IdAlreadyInUseException( "a particle with the same identifier already exists ("+particle.getId()+")" );

		particle.setBox( this );
		
		for( ParticleBoxListener listener: listeners )
			listener.particleAdded( particle.getId(), particle.pos.x, particle.pos.y, particle.pos.z );
		
		particle.inserted();
	}

	/**
	 * Remove the particle identifier by the given identifier.
	 * @param id The particle unique identifier.
	 * @return The removed particle (or null if not found).
	 */
	public Particle removeParticle( Object id )
	{
		Particle particle = particles.remove( id );
		
		// Remove it from the tree.
		
		if( particle != null )
		{			
			particle.removed();

			for( ParticleBoxListener listener: listeners )
				listener.particleRemoved( particle.getId() );

			tree.removeParticle( particle );
			particle.setBox( null );
		}
		
		return particle;
	}
	
	/**
	 * Remove all particles of this box.
	 */
	public void removeAllParticles()
	{
		for( Particle particle: particles.values() )
		{
			for( ParticleBoxListener listener: listeners )
				listener.particleRemoved( particle.getId() );
		
			Cell cell = particle.getCell();
			
//			if( cell != null )
//			{
				tree.removeParticle( particle );
				assert cell != null : "removing a particle ("+particle.getId()+") that is not in the tree, it has no cell";
				assert ! cell.hasParticle( particle ) : "the cell from which the particle was removed still contains the particle";
//			}
	
			particle.setBox( null );
		}
		
		particles.clear();
		
		assert tree.getRootCell().getPopulation() == 0 : "after remove all particles, the mama cell strill contains "+tree.getRootCell().getPopulation()+" particles";
	}
	
	/**
	 * One step of the particle simulation. This method:
	 * <ol>
	 * 		<li>move all particles according to their old positions;</li>
	 * 		<li>commit the newly computed position into the particles;</li>
	 * 		<li>appropriately subdivide or fusion the n-tree cells for the next
	 *          computation;</li>
	 * 		<li>tell what it did to the listeners.</li>
	 * </ol>
	 */
	public void step()
	{
//		System.err.printf( "ITERATION n°%d%n", time );

		// First make all particles move.
		
		for( Particle particle: particles.values() )
			particle.move( time );
		
		// Next make all particle switch to their next position.
		
		for( Particle particle: particles.values() )
			particle.nextStep( time );
		
		// Next we check the tree.
		
		tree.checkDivisions();
		
		// Tell to the listeners.
		
		for( ParticleBoxListener listener: listeners )
			listener.stepFinished( time );
		
		time++;
	}
	
	/**
	 * Add a listener for all events coming from the particle box: when a
	 * particle is added, removed or moved and for each time step.
	 * @param listener The listener to add.
	 */
	public void addParticleBoxListener( ParticleBoxListener listener )
	{
		listeners.add( listener );
	}
	
	/**
	 * Remove a listener from the particle box.
	 * @param listener The listener to remove.
	 */
	public void removeParticleBoxListener( ParticleBoxListener listener )
	{
		int i = listeners.indexOf( listener );
		
		if( i >= 0 )
		{
			listeners.remove( i );
		}
	}
}