package org.miv.pherd.ntree;

import org.miv.pherd.*;
import org.miv.pherd.geom.*;
import java.util.*;

/**
 * A cell of the n-Tree.
 *
 * TODO: dès qu'une particule est ajoutée il peut y avoir mitose.
 * Dès qu'une particule est enlevée il y a fusion. Cependant, durant le
 * déplacement des particules (qui se fait de manière itérative, toutes les
 * particules calculent leur nouvelle position, puis bougent. 
 * Ne serait-il pas plus efficace de réadapter l'arbre uniquement une fois que
 * toutes les particules on bougé, et non pas à chaque particule ?
 * C'est la raison pour laquelle certaines méthodes sont suffixées par "Direct".
 * Elles agissent directement. Les méthodes équivalentes sans le suffixe
 * "Direct" elles fonctionne itérativement.
 *
 * @see org.miv.pherd.ntree.NTree
 * @author Antoine Dutot
 * @since 2007
 */
public class Cell
{
// Attributes

	/**
	 * The cell identifier.
	 */
	protected String id;
	
	/**
	 * The tree containing me.
	 */
	protected NTree tree;
		
	/**
	 * Depth in the tree (distance from the root).
	 */
	protected int depth;
	
	/**
	 * Cell index in its mother.
	 */
	protected int index;
	
	/**
	 * Mother cell.
	 */
	protected Cell parent;
	
	/**
	 * Daughter cells.
	 */
	protected Cell sub[];
	
	/**
	 * The space occupied by this cell. This class allows to specify a bounding
	 * box (two extreme points in space), how to split a cell, and  
	 */
	protected CellSpace space;
	
	/**
	 * The data specific to a given particle simulation.
	 */
	protected CellData data;
	
	/**
	 * Particle bucket.
	 */
	protected HashMap<Object,Particle> particles = new HashMap<Object,Particle>();
	
	/**
	 * The total particle population handled by this cell, counting its subcells
	 * population, as only leafs contains particles.
	 */
	protected int population;
	
// Constructors
	
	/**
	 * Create a root cell in a given space.
	 * @param tree The n-tree containing this root cell.
	 * @param space The space occupied by this cell.
	 * @param data The cell data.
	 */
	public Cell( NTree tree, CellSpace space, String id, CellData data )
	{
		this.id    = id;
		this.tree  = tree;
		this.depth = 0;
		this.space = space;
		this.data  = data;
		
		if( data != null )
			data.setCell( this );
		
		for( NTreeListener listener: tree.listeners )
			listener.cellAdded( id, "", space.lo, space.hi, depth, index );
	}
	
	/**
	 * Used to create a subcell. This method is to be used only inside this
	 * class when subdividing a cell.
	 * @param parent The parent cell.
	 * @param index The cell index in its parent.
	 * @param space The space occupied by this cell.
	 * @param data The cell data.
	 */
	protected Cell( Cell parent, int index, CellSpace space, CellData data )
	{
		this.tree   = parent.tree;
		this.id     = tree.generateCellIdentifier( parent, index );
		this.parent = parent;
		this.depth  = parent.depth + 1;
		this.index  = index;
		this.space  = space;
		this.data   = data;
		
		if( data != null )
			data.setCell( this );
	
		for( NTreeListener listener: tree.listeners )
			listener.cellAdded( id, parent.id, space.lo, space.hi, depth, index );
	}
	
// Access

	/**
	 * The cell tree.
	 * @return The n-tree this cell pertains to.
	 */
	public NTree getTree()
	{
		return tree;
	}
	
	/**
	 * Cell identifier.
	 */
	public String getId()
	{
		return id;
	}
	
	/**
	 * Is this cell a leaf?.
	 * @return True if this cell has no subcells.
	 */
	public boolean isLeaf()
	{
		return( sub == null );
	}
	
	/**
	 * Is this cell a root cell?.
	 * @return True if this cell has no parent.
	 */
	public boolean isRoot()
	{
		return( parent == null );
	}
	
	/**
	 * The total population of particle this cell stores.
	 * @return Count of particle handled by this cell or its subcells.
	 */
	public int getPopulation()
	{
		return population;
	}
	
	/**
	 * The parent cell, if any.
	 * @return A cell that contains this one or null if this is a root cell.
	 */
	public Cell getParent()
	{
		return parent;
	}
	
	/**
	 * The i-th subcell or null if there is a leaf cell. Subcells are arranged
	 * in X row, then Y plane, then Z cube, going from the bottom-left-front
	 * point to the top-right-back point. This means for an octree that cell 0
	 * is on the bottom, left front, cell 1 on the bottom right front, cell 2 on
	 * the top left front, cell 3 on the top right front, cell 4 on the bottom
	 * left back, cell 5 on the bottom right back, cell 6 on the top left back
	 * and cell 6 on the top right back. Pfew.
	 * @param i The subcell index.
	 * @return The i-th subcell, or null if this cell is a leaf.
	 */
	public Cell getSub( int i )
	{
		if( sub != null )
			return sub[i];
		
		return null;
	}
	
	/**
	 * Depth of this cell.
	 * @return The number of parents of this cell.
	 */
	public int getDepth()
	{
		return depth;
	}

	/**
	 * The cell index in its mother cell.
	 * @return An integer.
	 */
	public int getIndex()
	{
		return index;
	}
	
	/**
	 * This cell space region.
	 * @return The space occupied by this cell.
	 */
	public CellSpace getSpace()
	{
		return space;
	}

	/**
	 * This cell data.
	 * @return The cell data.
	 */
	public CellData getData()
	{
		return data;
	}
	
	/**
	 * Set of stored particles, if this cell is a leaf, else null.
	 * @return An iterator on the set of stored particle or null if this cell is
	 * a leaf.
	 */
	public Iterator<? extends Particle> getParticles()
	{
		if( isLeaf() )
			return particles.values().iterator(); 
	
		return null;
	}
	
	/**
	 * Does this cell contains the position of the given particle ?.
	 * Be careful, this does not mean the cell contains the instance of the particle given, only
	 * that the particle position is within the cell space. 
	 * @param particle The particle to classify.
	 * @return True if this cell contains the given particle.
	 * @see #hasParticle(Particle)
	 */
	public boolean contains( Particle particle )
	{
		return space.contains( particle );
	}
	
	/**
	 * Does this cell contains the given position ?.
	 * @param x The abscissa.
	 * @param y The ordinate.
	 * @param z The depth.
	 * @return True if this space contains the given position.
	 */
	public boolean contains( double x, double y, double z )
	{
		return space.contains( x, y, z );
	}
	
	/**
	 * True if the cell has the particle as a child. This test verifies that the cell is the
	 * container of the particle.
	 * @param particle The particle to test.
	 * @return True if the cell is the container of the particle.
	 * @see #contains(Particle)
	 */
	public boolean hasParticle( Particle particle )
	{
		return( particles.get( particle.getId() ) != null );
	}
	
// Commands
		
	/**
	 * Add a particle in the n-Tree. This method is recursive, starting from the
	 * tree root. It adds the particle to leaf cells only. It never subdivide
	 * cells if needed, which means that a cell may contain too many particles
	 * at a time. The subdivision will occur when the {@link #recompute()}
	 * method will be called.
	 * @param particle The particle to add.
	 */
	public void addParticle( Particle particle )
	{
		// The particle is considered inside the root cell space.
		// We must therefore find a cell where to insert it.
		
		population++;
		
		if( ! isLeaf() )
		{
			int k = 0;
			
			for( int i=0; i<sub.length; ++i )
			{
				if( sub[i].contains( particle ) )
				{
//					System.err.printf( "   Subcell %s %s contains particle %s %s%n", sub[i].toString(), sub[i].getSpace().toString(), particle.getId(), particle.getPosition().toString() );
					
					if( k == 0 )
						sub[i].addParticle( particle );
					
					k++;
				}
			}
			
			assert k == 1 : "no subcell or too many subcells ("+k+") found to add particle "+particle.getId();
		}
		else
		{
			// Add the particle with the current divisions without trying to subdivide/merge
			// the tree like it would be needed. This will be done in a lazy fashion later
			// in the checkTree() method.
			
			Particle old = particles.put( particle.getId(), particle );
			particle.setCell( this );
			
			assert old == null : "Particle ID "+particle.getId()+" added in the cell already exists.";
			assert population == particles.size() : "Discepancy in population count of "+id+" ? (population="+population+" p.size="+particles.size()+")";
		}
	}
	
	/**
	 * Remove a particle from the n-Tree. This method must be called by the
	 * particle itself. It will recurse on the whole parent branch to adjust
	 * the population count. This method never try to fusion cells if the
	 * particle population size is too small. This will be done at a later time
	 * when the {@link #recompute()} method will be called.
	 * @param id The particle identifier.
	 */
	public void removeParticle( Object id )
	{
		population--;
		
		if( isLeaf() )
		{
			Particle p = particles.remove( id );
			
			assert p != null : "particle "+id+" wrongly removed?";
			assert population == particles.size() : "discrepancy between the population "+population+" and set of particles "+particles.size();
			
			p.setCell( null );
		}
		//else
		//{
		//	We do not need to do the fusion here. It will be done later in
		//	the checkTree() method.
		//}

		if( ! isRoot() )
			parent.removeParticle( this.id );
	}
	
	/**
	 * Message from a particle to indicate it moved. This will move the
	 * particle from one cell to another if needed, but in this case, no
	 * subdivision or fusion will occur until the {@link #recompute()}
	 * method is called.
	 * @param particle The particle that moved.
	 */
	public void particleMoved( Particle particle )
	{
		// If it moved out of me, reposition it.
		
		assert isLeaf() : "particle moved event in non-leaf cell "+id+" ?";
		
		if( ! contains( particle ) )
		{
			if( tree.laMama.contains( particle ) )
			{
				removeParticle( particle.getId() );
				tree.laMama.addParticle( particle );
			}
			else
			{
				tree.handleOutParticle( particle );
			}
		}
	}
	
	/**
	 * Recursively check cells to see if they need fusion or subdivision and
	 * send a recompute signal to all cell data elements.
	 */
	public void recompute()
	{
		// Here we check for the tree subdivision.
		
		if( isLeaf() )
		{
			assert population == particles.size() : "Discepancy in population count of "+id+" ? (population="+population+" p.size="+particles.size()+")";
			
			if( depth < tree.depthmax &&  population > tree.pmax )
			{
				mitosis();
				
				for( Cell cell: sub )
					cell.recompute();
			}
		}
		else
		{
			int hasLeafs = 0;
			int divs     = space.getDivisions();
			
			for( Cell cell: sub )
			{
				cell.recompute();
				
				hasLeafs += cell.isLeaf() ? 1 : 0;
			}

//			System.err.printf( "Check fusion of cell %s: pop=%d max=%d aboveLeafs=%b%n", id, population, tree.pmax, leaf );
			
			if( hasLeafs == divs && population <= tree.pmax )	// In fact the condition pop<pmax is
			{													// sufficient, since no subcell can
				fusion();										// be subdivided if there is this number
			}													// of particles. Its just another
		}														// invariant check.
		
		if( data != null )
			data.recompute();
	}
	
	/**
	 * Subdivide a cell into several sub-cells according to the currently used
	 * cell space.
	 */
	protected void mitosis()
	{
		assert sub == null : "sub should be null here";
		assert particles.size() > tree.pmax : "no subdivision needed ?";
		
		int div = space.getDivisions();

		sub = new Cell[div];
		
		for( int i=0; i<div; ++i )
		{
			// Create the sub cell.

			sub[i] = new Cell( this, i, space.newSubCellSpace( i ), data != null ? data.newCellData() : null );
			
			// Iterate over the remaining particles to place them
			// inside the new cell if it must contain them.
			
			Iterator<? extends Particle> k = particles.values().iterator();
		
			while( k.hasNext() )
			{
				Particle p = k.next();
			
				if( sub[i].contains( p ) )
				{
					k.remove();
					sub[i].addParticle( p );
				}
			}
			
			// Once this process is finished, it should not remain any particle
			// that is not placed in a sub-cell.
		}
		
		assert particles.size() == 0 : "there are "+particles.size()+" unclassified particles after mitosis ?";	
	}
	
	protected void fusion()
	{
		assert particles.size() == 0;
		
		for( Cell cell: sub )
		{
			assert cell.isLeaf() : "Fusion of non leaf-subcells !!";

			particles.putAll( cell.particles );
			
			for( NTreeListener listener: tree.listeners )
				listener.cellRemoved( cell.id );
		}

		sub = null;
		
		for( Particle particle: particles.values() )
			particle.setCell( this );

//		System.err.printf( "cell %s[%d] se fusionne (pop = %d)%n", toString(), depth, population );
	}
	
	/**
	 * Send the state of this cell to the given listener, then recurse on all
	 * subcells if any. This allows to send the ntree (or sub-ntree) description
	 * to a listener when it first connects to the tree.
	 * @param listener The listener that should receive the subtree description.
	 */
	protected void describe( NTreeListener listener )
	{
		String parentId = "";
		
		if( parent != null )
			parentId = parent.id;
		
		listener.cellAdded( id, parentId, space.lo, space.hi, depth, index );
		
		if( sub != null )
		{
			for( Cell cell: sub )
				cell.describe( listener );
		}
	}
	
	/**
	 * Resize the cell space so that 
	 * @param min The lowest new space point.
	 * @param max The hihest new space point.
	 */
	protected void resize( Point3 min, Point3 max )
	{
		if( parent != null )
			throw new RuntimeException( "can only resize the root cell" );
		
//		System.err.printf( "### RESIZE (%s  %s)%n", min, max );
	
		// We need to resize the tree as some particles left the root cell.
		// 3 ways to do this:
		//		* Make the laMama a child cell. Problem, we may need to
		//		  create several super cells if the particles that left
		//		  the root cell was very var away.
		//		* Enlarge laMama space and ask each particle to move. This
		//		  will trigger a reassignation of each particle. However
		//		  this is computationnaly more intensive as it involves a
		//		  browsing of each particle and several traversals of the
		//        tree.
		//		* Create a new tree. Intensive also, but very easy to
		//		  implement. Lets implement this last solution for now:
	
		// 1. We are sure we are in the root cell.
		// 2. We remove all particles from the particle box.
		
		ArrayList<Particle> particles = new ArrayList<Particle>();
		Iterator<Object>    k         = tree.pbox.getParticleIdIterator();
		int                 oldPop    = population;
		
		while( k.hasNext() )
		{
			Object id = k.next();
			particles.add( tree.pbox.getParticle( id ) );
		}
		
		tree.pbox.removeAllParticles();

		// 3. Recompute the tree to remove all children by fusion. We do this
		//    to trigger removal events.
		
		assert population == 0 : "after removal of all particles the root cell still contains "+population+" particles...";
		assert this.particles.size() == 0 : "after removal of all particles the root cell still contains "+this.particles.size()+" particles...";
		
		recompute();
		
		for( NTreeListener listener: tree.listeners )
			listener.cellRemoved( id );
		
		assert isLeaf(): "after particles removal the mama cell should be root and leaf";
		
		// 4. We resize this root cell.
		
		space.resize( min, max );
		
		for( NTreeListener listener: tree.listeners )
			listener.cellAdded( id, "", space.lo, space.hi, depth, index );
		
		// 5. Re-insert all particles in this root cell.

		for( Particle particle: particles )
			tree.pbox.addParticle( particle );
		
		// 6. Recompute the tree to subdivide it. We use recompute to trigger
		//    mitosis events.
		
		recompute();
		
		assert population == tree.pbox.getParticleCount() : "discrepancy when resinserting particles during mama resize";
		assert population == oldPop                       : "after resize new population size != old ("+population+" != "+oldPop+")";
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append( "[Cell " );
		sb.append( id );
		sb.append( " depth=" );
		sb.append( depth );
		sb.append( " pop=" );
		sb.append( population );
		if( isLeaf() )
			sb.append( " L" );
		if( isRoot() )
			sb.append( " R" );
		sb.append( "]" );
		
		return sb.toString();
	}
	
	public boolean isValid()
	{
		int div = space.getDivisions();
		int pop = 0;
		
		if( ! isLeaf() )
		{
			if( div != sub.length ) return false;
		
			for( int i=0; i<div; i++ )
			{
				if( ! sub[i].isValid() ) return false;
				pop += sub[i].getPopulation();
			
				if( sub[i].depth != ( depth + 1 ) ) return false;
			}
			
			if( pop != getPopulation() ) return false;
		}
		
		return true;
	}
}