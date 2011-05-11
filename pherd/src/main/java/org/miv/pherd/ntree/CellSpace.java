package org.miv.pherd.ntree;

import org.miv.pherd.*;
import org.miv.pherd.geom.*;

/**
 * Cell space.
 *
 * <p>
 * The cell space defines a rectangular region of a Cartesian 2D or 3D space.
 * This region is defined by two extreme points "lo" and "hi" that represent
 * the bottom-left-front point and the top-right-back point respectively.
 * </p>
 * 
 * <p>
 * The cell space defines also the number of sub-cells to create when subdividing
 * a cell, and how to distribute the particles of a cell to these sub-cells. It
 * allows generally to know where to put a particle in any space or subspace.
 * </p>
 *
 * @author Antoine Dutot
 * @since 2007
 */
public abstract class CellSpace
{
// Attribute
	
	/**
	 * The bounding box low point.
	 */
	protected Anchor lo;
	
	/**
	 * The bounding box hi point.
	 */
	protected Anchor hi;

	/**
	 * Largest diagonal length.
	 */
	protected double diag;
	
// Construction

	public CellSpace( Anchor lo, Anchor hi )
	{
		this.lo = lo;
		this.hi = hi;
		
		diag = lo.distance( hi );
	}
	
// Access
	
	/**
	 * Bottom-left-from point defining the bounding box of this cell.
	 * @return A point in 3D Cartesian space.
	 * @see #getHiAnchor()
	 */
	public Anchor getLoAnchor()
	{
		return lo;
	}
	
	/**
	 * Top-righ-back point defining the bounding box of this cell.
	 * @return A point in 3D Cartesian space.
	 * @see #getLoAnchor()
	 */
	public Anchor getHiAnchor()
	{
		return hi;
	}
	
	/**
	 * Number of sub-cells to create to divide a cell.
	 * @return The number of sub-cells obtained for a cell mitosis.
	 */
	public abstract int getDivisions();

	/**
	 * Create the appropriate cell space for the given sub-cell index. This
	 * method role is to create a sub-cell space when subdividing a cell.
	 * @param i The sub-cell index, must be equal or larger than zero and less
	 *        than {@link #getDivisions()}.
	 * @return A new cell space. 
	 */
	public abstract CellSpace newSubCellSpace( int i );
	
	/**
	 * Does this space contains the given particle?.
	 * @param particle The particle to classify.
	 * @return True if this space contains the given particle.
	 */
	public boolean contains( Particle particle )
	{
		Point3 ppos = particle.getPosition();

		return contains( ppos.x, ppos.y, ppos.z );
	}
	
	/**
	 * Does this space contains the given position.
	 * @param x The abscissa.
	 * @param y The ordinate.
	 * @param z The depth.
	 * @return True if this space contains the given position.
	 */
	public boolean contains( double x, double y, double z )
	{
		if( x < lo.x || x >= hi.x ) return false;
		if( y < lo.y || y >= hi.y ) return false;
		if( z < lo.z || z >= hi.z ) return false;/*{ System.err.printf("false on Z (%f not in [%f..%f] )!%n",z,lo.z,hi.z); return false; }*/
	
		return true;		
	}

	public boolean contains2( double x, double y, double z )
	{
System.err.printf( "contains (%f,%f,%f)  %s %s%n", x, y, z, lo, hi );
		
		if( x < lo.x || x >= hi.x ) return false;
		if( y < lo.y || y >= hi.y ) return false;
		if( z < lo.z || z >= hi.z ) return false;
	
		return true;		
	}
	
	/**
	 * The cell size estimation, often the length of the cell space largest diagonal.
	 * @return The cell size estimation.
	 */
	public double getSize()
	{
		return diag;
	}
	
// Commands

	/**
	 * Resize this space to the given bounding box. This operation is valid
	 * only if this CellSpace has no child spaces.
	 * @param min The lowest point (bottom-left-front).
	 * @param max The highest point (top-right-back).
	 */
	public void resize( Point3 min, Point3 max )
	{
		lo = new Anchor( min.x, min.y, min.z );
		hi = new Anchor( max.x, max.y, max.z );
		
		diag = lo.distance( hi );
	}
}