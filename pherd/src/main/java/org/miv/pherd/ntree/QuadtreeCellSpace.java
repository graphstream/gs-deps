package org.miv.pherd.ntree;

import org.miv.pherd.*;
import org.miv.pherd.geom.*;

/**
 * Cell space for a quadtree.
 *
 * <p>
 * This cell space defines four subcells for each cell subdivision.
 * </p>
 *
 * @author Antoine Dutot
 * @since 2007
 */
public class QuadtreeCellSpace extends CellSpace
{
	protected Anchor C;
	
	public QuadtreeCellSpace( Anchor lo, Anchor hi )
	{
		super( lo, hi );
	}
	
	@Override
	public int getDivisions()
	{
		return 4;
	}
	
	/**
	 * Does this space contains the given particle?.
	 * @param particle The particle to classify.
	 * @return True if this space contains the given particle.
	 */
	@Override
	public boolean contains( Particle particle )
	{
		Point3 pos = particle.getPosition();
		
		return contains( pos.x, pos.y );
	}

	@Override
	public boolean contains( double x, double y, double z )
	{
		if( x < lo.x || x >= hi.x ) return false;
		if( y < lo.y || y >= hi.y ) return false;
	
		return true;
	}
	
	public boolean contains( double x, double y )
	{
		if( x < lo.x || x >= hi.x ) return false;
		if( y < lo.y || y >= hi.y ) return false;
	
		return true;
	}
	
	@Override
	public CellSpace newSubCellSpace( int i )
	{
		if( C == null )
		{
			double cx = lo.x + ( hi.x - lo.x ) / 2;
			double cy = lo.y + ( hi.y - lo.y ) / 2;

			C = new Anchor( cx, cy, hi.z );
		}
		
		Anchor BL = lo;
		Anchor TR = hi;
		
		switch( i )
		{
			case 0:
				C.z = hi.z;
				return new QuadtreeCellSpace( BL, C );
			case 1:
				Anchor BR1 = new Anchor( C.x, BL.y, lo.z );
				Anchor BR2 = new Anchor( TR.x, C.y, hi.z );
				return new QuadtreeCellSpace( BR1, BR2 );
			case 2:
				Anchor TL1 = new Anchor( BL.x, C.y, lo.z );
				Anchor TL2 = new Anchor( C.x, TR.y, hi.z );
				return new QuadtreeCellSpace( TL1, TL2 );
			case 3:
				C.z = lo.z;
				return new QuadtreeCellSpace( C, TR );
			default:
				throw new RuntimeException( "invalid subcell space index '"+i+"' for quad tree" );
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append( "[" );
		sb.append( lo.x );
		sb.append( ", " );
		sb.append( lo.y );
		sb.append( " -> " );
		sb.append( hi.x );
		sb.append( ", " );
		sb.append( hi.y );
		sb.append( "]" );
		
		return sb.toString();
	}
	
	@Override
	public void resize( Point3 min, Point3 max )
	{
		super.resize( min, max );
		
		double cx = lo.x + ( hi.x - lo.x ) / 2;
		double cy = lo.y + ( hi.y - lo.y ) / 2;
		double cz = lo.z + ( hi.z - lo.z ) / 2;
		
		C = new Anchor( cx, cy, cz );
	}
}