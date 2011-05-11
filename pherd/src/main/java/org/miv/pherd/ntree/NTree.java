package org.miv.pherd.ntree;

import java.util.*;
import org.miv.pherd.*;
import org.miv.pherd.geom.*;

/**
 * n-Tree.
 *
 * @see org.miv.pherd.ntree.Cell
 * @author Antoine Dutot
 * @since 2007
 */
public class NTree
{
// Constant

	/**
	 * How to handle particles that go out of the root cell space ?.
	 */
	public static enum OutOfUniverseMode {
		/**
		 * Delete the particle by removing it from the particle box.
		 */
		DELETE,
		/**
		 * Resize the root cell and rebuild the tree.
		 */
		RESIZE
	};
	
// Attribute

	/**
	 * The particle set.
	 */
	protected ParticleBox pbox;
	
	/**
	 * The root cell.
	 */
	protected Cell laMama;
	
	/**
	 * Maximum number of particles by cell.
	 */
	protected int pmax = 10;
	
	/**
	 * Maximum depth of the n-tree.
	 */
	protected int depthmax = 100;
	
	/**
	 * The set of listeners.
	 */
	protected ArrayList<NTreeListener> listeners = new ArrayList<NTreeListener>();
	
	/**
	 * How to handle particles that leave the universe initial bounding box.
	 * If the mode is RESIZE, the universe is resized. Else the particle is
	 * deleted.
	 */
	protected OutOfUniverseMode oum = OutOfUniverseMode.RESIZE;
	
	/**
	 * Set to true each time some particles left the root cell space and this
	 * space needs to be resized.
	 * @see #checkDivisions()
	 */
	protected boolean needResize = false;
	
	/**
	 * The minimum and maximum points of the universe when growing the root cell, if a
	 * resize of the root cell space is needed.
	 * @see #checkDivisions()
	 */
	protected Point3 min = new Point3(), max = new Point3();
	
// Construction

	/**
	 * New n-Tree with a universe of (-1,-1,-1) to (1,1,1).
	 * @param pmax Maximum number of particle by cell, once this number is
	 * superseded, the cell subdivides itself.
	 * @param data The data specific to each cell.
	 * @param pbox The particle set.
	 */
	public NTree( int pmax, CellData data, ParticleBox pbox )
	{
		this( pmax, new OctreeCellSpace(
				new Anchor( -1, -1, -1 ),
				new Anchor( 1, 1, 1 ) ), data, pbox );
	}
	
	/**
	 * New n-Tree with a universe defined by the given space.
	 * @param pmax Maximum number of particle by cell, once this number is
	 * superseded, the cell subdivides itself.
	 * @param space The universe bounding box.
	 * @param data The data specific to each cell.
	 * @param pbox The particle set.
	 */
	public NTree( int pmax, CellSpace space, CellData data, ParticleBox pbox )
	{
		this.pmax   = pmax;
		this.pbox   = pbox;
		this.laMama = new Cell( this, space, "laMama", data );		
		
		min.copy( space.lo );
		max.copy( space.hi );
	}
	
// Access
	
	/**
	 * The tree root cell. Note that this cell may change if the space is
	 * enlarged.
	 * @return The root cell.
	 */
	public Cell getRootCell()
	{
		return laMama;
	}
	
	/**
	 * Maximum number of particles per cell. If a cell contains more particles
	 * it subdivides itself.
	 * @return The number of particles per cell.
	 */
	public int getMaxParticlePerCell()
	{
		return pmax;
	}
	
	/**
	 * Maximum number of subdivisions of a cell.
	 * @return The maximum depth of the tree.
	 */
	public int getMaxDepth()
	{
		return depthmax;
	}
	
	/**
	 * The set of listeners of this tree.
	 * @return The listeners.
	 */
	public ArrayList<NTreeListener> getListeners()
	{
		return listeners;
	}
	
	/**
	 * What to do if a particle leaves the root cell ?.
	 * @return The action to take when a particle leaves the root cell.
	 */
	public OutOfUniverseMode getOutOfUniverseMode()
	{
		return oum;
	}
	
	/**
	 * The lowest coordinate used by a particle. With the highest point this
	 * forms the universe bounding box.
	 * @return A point.
	 */
	public Point3 getLowestPoint()
	{
		return min;
	}
	
	/**
	 * The highest coordinate used by a particle. With the lowest point this
	 * forms the universe bounding box.
	 * @return A point.
	 */
	public Point3 getHighestPoint()
	{
		return max;
	}
	
// Command
	
	/**
	 * Set the maximum depth of the tree. Be careful, this launch a re-computation of the whole
	 * tree.
	 * @param max The maximum number of decomposition of space (>0).
	 */
	public void setDepthMax( int max )
	{
		assert max > 0 : "invalid max depth";
		
		this.depthmax = max;
		laMama.recompute();
	}
	
	/**
	 * Add a listener that will receive events abound the ntree.
	 * @param listener The listener to add.
	 */
	public void addListener( NTreeListener listener )
	{
		listeners.add( listener );
		describeTheCurrentState( listener );
	}
	
	/**
	 * Remove a listener.
	 * @param listener The listener to remove.
	 */
	public void removeListener( NTreeListener listener )
	{
		int n = listeners.indexOf( listener );
		
		if( n >= 0 )
		{
			listeners.remove( n );
		}
	}
	
	/**
	 * Send events for all already existing cells to the newly added listener.
	 * @param listener The newly added listener.
	 */
	protected void describeTheCurrentState( NTreeListener listener )
	{
		laMama.describe( listener );
	}
	
	/**
	 * Remove a particle from the tree.
	 * @param particle The particle to remove.
	 */
	public void removeParticle( Particle particle )
	{
		// The process is recursive, going from the leaf to the root.
		
		Cell cell = particle.getCell();
		
		// Some particles can be tied to no cell if they left the root cell
		// space, hence the test for null.
		
		if( cell != null )
			cell.removeParticle( particle.getId() );
	}
	
	/**
	 * Add a particle in the tree.
	 * @param particle The particle to add.
	 */
	public void addParticle( Particle particle )
	{
		// If the tree (root cell) space is not large enough to
		// contain the particle, we must first enlarge it.
		
		if( ! laMama.contains( particle ) )
		{
			handleOutParticle( particle );
			checkDivisions();
			assert laMama.contains( particle );	// Assert the particle is within the new space.
		}

		// The process is recursive, going from the root to the leafs.
		
		laMama.addParticle( particle );
	}
	
	/**
	 * Recursively check if cells need subdivision or fusion. In some case if
	 * some particles left the root cell space and the policy is to resize this
	 * space, instead of checking divisions or fusions, this method recomputes
	 * the whole tree and resizes its space.
	 */
	public void checkDivisions()
	{
		if( needResize )
		{
			// Either double the current space or (if larger) use the computed
			// min and max values.
			
			CellSpace space = laMama.getSpace();

			double dbMinX = space.lo.x - (double)Math.abs( space.lo.x );
			double dbMinY = space.lo.y - (double)Math.abs( space.lo.y );
			double dbMinZ = space.lo.z - (double)Math.abs( space.lo.z );
			
			double dbMaxX = space.hi.x + (double)Math.abs( space.hi.x );
			double dbMaxY = space.hi.y + (double)Math.abs( space.hi.y );
			double dbMaxZ = space.hi.z + (double)Math.abs( space.hi.z );

			if( min.x > dbMinX ) min.x = dbMinX;
			if( min.y > dbMinY ) min.y = dbMinY;
			if( min.z > dbMinZ ) min.z = dbMinZ;
			
			if( max.x < dbMaxX ) max.x = dbMaxX;
			if( max.y < dbMaxY ) max.y = dbMaxY;
			if( max.z < dbMaxZ ) max.z = dbMaxZ;

			// Resize a little larger so that contains always work (since it tests for < and *>=*).

			double dx = ( max.x - min.x ) * 0.001f;
			double dy = ( max.y - min.y ) * 0.001f;
			double dz = ( max.z - min.z ) * 0.001f;
			
			max.x += dx; min.x -= dx;
			max.y += dy; min.y -= dy;
			max.z += dz; min.z -= dz;
		
			// Resize the whole tree.
			
			laMama.resize( min, max );	// This does the recompute.
			
			needResize = false;
		}
		else
		{
			laMama.recompute();
		}
		
		assert isValid();
	}
	
	/**
	 * Generate an identifier for a cell given its parent and index in this parent.
	 * This method generates a unique path of the index of each parent cell.
	 * @param parent The parent.
	 * @param index The cell index.
	 * @return An unique cell identifier.
	 */
	public String generateCellIdentifier( Cell parent, int index )
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append( "C[" );
		sb.append( index );
		sb.append( "/" );
		sb.append( parent.index );
		
		parent = parent.getParent();
		
		while( parent != null )
		{
			sb.append( "/" );
			sb.append( parent.index );
			
			parent = parent.getParent();
		}
		
		sb.append( "]" );
		
		return sb.toString();
	}
	
	/**
	 * Tell what to do when a particle leaves the root cell. The DELETE mode
	 * kills the particle. The RESIZE mode make the root cell a child of as
	 * many cell as needed to contain the leaving particle.
	 * @param mode The mode to follow.
	 */
	public void setOutOfUniverseMode( OutOfUniverseMode mode )
	{
		oum = mode;
	}
	
// Commands
	
	/**
	 * Decide what to do for a particle that moved out of the root cell space.
	 */
	protected void handleOutParticle( Particle particle )
	{
		switch( oum )
		{
			case DELETE:
				delete( particle );
				break;
			case RESIZE:
				resize( particle );
				break;
			default:
				assert false : "unknown OutOfUniverseMode";
		}
	}
	
	/**
	 * Ask a particle to remove itself from the particle box.
	 * @param particle The particle to remove.
	 */
	protected void delete( Particle particle )
	{
		particle.suicide();
	}
	
	/**
	 * Specify that a particle is out of the root cell space. This method
	 * remembers the particle position in order to compute the future new root
	 * cell space.
	 * @param particle The outrageous particle.
	 */
	protected void resize( Particle particle )
	{
		Point3 p = particle.getPosition();

		needResize = true;
		
		if( p.x > max.x )      max.x = p.x;
		else if( p.x < min.x ) min.x = p.x;
		if( p.y > max.y )      max.y = p.y;
		else if( p.y < min.y ) min.y = p.y;
		if( p.z > max.z )      max.z = p.z;
		else if( p.z < min.z ) min.z = p.z;
	}
	
	/**
	 * Make a recursive check of each cell of the tree.
	 * @return True if no discrepancy has been found.
	 */
	protected boolean isValid()
	{
		return laMama.isValid();
	}
}