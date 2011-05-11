package org.miv.pherd;

import java.util.HashMap;
import java.util.Iterator;

import org.miv.pherd.geom.*;
import org.miv.pherd.ntree.*;

/**
 * Base class for particles.
 * 
 * <p>
 * This class is the basis used to build particles classes. It has a unique identifier, a
 * position as a point in space, a weight as a scalar, and references a cell that contains it.
 * </p>
 * 
 * <p>
 * This class is abstract, the only method that must be implemented is the {@link #move(int)}
 * method. This method must store in the {@link #nextPos} field the position of the particle
 * at the next time step. The current time step is given as argument.
 * </p>
 *
 * @author Antoine Dutot
 * @since 2007
 */
public abstract class Particle
{
// Attributes
	
	/**
	 * Particle identifier.
	 */
	protected Object id;
	
	/**
	 * Particle position.
	 */
	protected Point3 pos;
	
	/**
	 * The computed next particle position.
	 */
	protected Point3 nextPos = new Point3();
	
	/**
	 * The particle importance.
	 */
	protected double weight = 1f;
	
	/**
	 * Cell containing this particle.
	 */
	protected Cell cell;
	
	/**
	 * The particle box containing this particle.
	 */
	protected ParticleBox box = null;

	/**
	 * The particle attributes.
	 */
	protected HashMap<String,Object> attributes;
	
	/**
	 * Flag used to avoid doing work on a particle that did not moved.
	 * This flag is set to false in {@link #nextStep(int)}, and should be set
	 * to true in {@link #move(int)} in order to effectively move the particle.
	 */
	protected boolean moved;
	
// Constructors
	
	/**
	 * New particle at (x,y,z).
	 * @param id Particle identifier.
	 * @param x Abscissa position.
	 * @param y Ordinate position.
	 * @param z Depth position.
	 */
	public Particle( Object id, double x, double y, double z )
	{
		this.id = id;
		initPos( x, y, z );
	}
	
	/**
	 * Particle at (0,0,0).
	 * @param id Particle identifier.
	 */
	public Particle( Object id )
	{
		this.id = id;
		initPos( 0, 0, 0 );
	}
	
	/**
	 * Can be used in the constructor to initialise a first position.
	 * Use it only in the constructor !! It is provided for the case you do not have
	 * the position when constructing a sub class of Particle and calling "super()".
	 * @param x Abscissa position.
	 * @param y Ordinate position.
	 * @param z Depth position.
	 */
	protected void initPos( double x, double y, double z )
	{
		this.pos = new Point3( x, y, z );
		nextPos.copy( pos );		
	}
	
// Access

	/**
	 * The particle unique identifier.
	 * @return An identifier object.
	 */
	public Object getId()
	{
		return id;
	}
	
	/**
	 * Particle position.
	 * @return The three particle coordinates.
	 */
	public Point3 getPosition()
	{
		return pos;
	}
	
	/**
	 * The cell actually responsible for this particle.
	 * @return The particle cell.
	 */
	public Cell getCell()
	{
		return cell;
	}
	
	/**
	 * Like {@link #closeTo(Particle, double)} with a delta of 0.001f.
	 * @param other The other particle to test.
	 * @return True if both particles are close to each other.
	 */
	public boolean closeTo( Particle other )
	{
		return closeTo( other, 0.001f );
	}
	
	/**
	 * Is the given particle close to this one.
	 * @param other The other particle to test.
	 * @param delta The distance threshold (included) to consider the other
	 * particle is close to this one.
	 * @return True if both particles are close to each other.
	 */
	public boolean closeTo( Particle other, double delta )
	{
		double d = pos.distance( other.pos );
		
		return( d <= delta );
	}
	
	/**
	 * Particle importance, or weight or mass.
	 * @return The particle weight or mass.
	 */
	public double getWeight()
	{
		return weight;
	}
	
	/**
	 * The value associated with one of the particle attributes.
	 * @param attribute The attribute.
	 * @return The attribute value or null if not found (or the attribute value is null).
	 */
	public Object getAttribute( String attribute )
	{
		if( attributes != null )
			return attributes.get( attribute );
	
		return null;
	}
	
	/**
	 * Iterator on the set of attribute keys. Null if there are no attributes.
	 * @return An iterator on strings.
	 */
	public Iterator<String> getAttributeKeyIterator()
	{
		if( attributes != null )
			return attributes.keySet().iterator();
		
		return null;
	}
	
// Commands
	
	/**
	 * Cell of the n-tree that handles this particle.
	 * @param responsible The cell that is now responsible for this particle.
	 */
	public void setCell( Cell responsible )
	{
		this.cell = responsible;
	}
	
	/**
	 * The importance (weight, mass, etc.) of the particle.
	 * @param newWeight The new value.
	 */
	public void setWeight( double newWeight )
	{
		weight = newWeight;
		
		// The data may take the weight into account.
		
		if( cell != null )
			cell.getData().recompute();
	}
	
	/**
	 * 
	 */
	public void setAttribute( String attribute, Object...values )
	{
		Object v;
		
		if( values == null || values.length == 0 )
		     v = true;
		else if( values.length == 1 )
		     v = values[0];
		else v = values;
		
		if( attributes == null )
			attributes = new HashMap<String,Object>();
		
		attributes.put( attribute, v );
		
		if( box != null && box.listeners != null )
			for( ParticleBoxListener listener: box.listeners )
				listener.particleAttributeChanged( id, attribute, v, false );
	}
	
	public void removeAttribute( String attribute )
	{
		if( attributes != null )
		{
			attributes.remove( attribute );
	
			if( box != null && box.listeners != null )
				for( ParticleBoxListener listener: box.listeners )
					listener.particleAttributeChanged( id, attribute, null, true );
		}
	}
	
	/**
	 * Should compute the next position from the current position. Be careful,
	 * the flag {@link #moved} must be set to "true" if this method did
	 * something and to false to avoid processing this particle any further
	 * if nothing has been done. This allows to avoid unwanted computation for
	 * particles that do not move.
	 * @param time The current iteration.
	 */
	public abstract void move( int time );
	
	/**
	 * The particle was just inserted in a particle box.
	 */
	public abstract void inserted();
	
	/**
	 * The particle is about to be removed from the particle box.
	 */
	public abstract void removed();
	
	/**
	 * Copy the next computed position from the current position.
	 * @param time The current iteration.
	 */
	public void nextStep( int time )
	{
		if( moved )
		{
			pos.copy( nextPos );

			assert cell != null : "No responsible cell ?";
		
			cell.particleMoved( this );

			for( ParticleBoxListener listener: box.listeners )
				listener.particleMoved( id, pos.x, pos.y, pos.z );
			
			moved = false;
		}
	}

	/**
	 * Called by the particle box when this particle is inserted in it.
	 * @param box The particle box that now contains this particle.
	 */
	protected void setBox( ParticleBox box )
	{
		assert ( box == null && this.box != null ) || ( box != null && this.box == null ) : "Particle in two boxes at a time ?";
		
		this.box = box;
	}
	
	/**
	 * Make this particle disappear from its particle box.
	 */
	public void suicide()
	{
		if( box != null )
		{
			box.removeParticle( id );
		}
	}
}