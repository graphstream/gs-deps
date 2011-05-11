package org.miv.pherd.ntree;

import org.miv.pherd.geom.*;

/**
 * Cell space for an octree.
 *
 * <p>
 * This cell space defines height subcells for each cell subdivision.
 * </p>
 *
 * @author Antoine Dutot
 * @since 2007
 */
public class OctreeCellSpace extends CellSpace
{
	protected Anchor C;
	
	public OctreeCellSpace( Anchor lo, Anchor hi )
	{
		super( lo, hi );
	}

	@Override
	public int getDivisions()
	{
		return 8;
	}

	@Override
	public CellSpace newSubCellSpace( int i )
	{
		// We need to describe the corners of the two cubes on the lo-hi
		// diagonal. There are 13 new points to create.
		// T top   / B bottom
		// L left  / R right
		// F front / B back
		
		if( C == null )
		{
			double cx = lo.x + ( hi.x - lo.x ) / 2;
			double cy = lo.y + ( hi.y - lo.y ) / 2;
			double cz = lo.z + ( hi.z - lo.z ) / 2;
			
			C = new Anchor( cx, cy, cz );
		}
				
		Anchor BLF1 = lo;
		Anchor TRB2 = hi;
		
		switch( i )
		{
			case 0:
				return new OctreeCellSpace( BLF1, C );
			case 1:
				Anchor BRF1 = new Anchor( C.x, lo.y, lo.z );
				Anchor BRF2 = new Anchor( hi.x, C.y, C.z );
				return new OctreeCellSpace( BRF1, BRF2 );
			case 2:
				Anchor TRF1 = new Anchor( C.x, C.y, lo.z );
				Anchor TRF2 = new Anchor( hi.x, hi.y, C.z );
				return new OctreeCellSpace( TRF1, TRF2 );
			case 3:
				Anchor TLF1 = new Anchor( lo.x, C.y, lo.z );
				Anchor TLF2 = new Anchor( C.x, hi.y, C.z );
				return new OctreeCellSpace( TLF1, TLF2 );
			case 4:
				Anchor BLB1 = new Anchor( lo.x, lo.y, C.z );
				Anchor BLB2 = new Anchor( C.x, C.y, hi.z );
				return new OctreeCellSpace( BLB1, BLB2 );
			case 5:
				Anchor BRB1 = new Anchor( C.x, lo.y, C.z );
				Anchor BRB2 = new Anchor( hi.x, C.y, hi.z );
				return new OctreeCellSpace( BRB1, BRB2 );
			case 6:
				return new OctreeCellSpace( C, TRB2 );
			case 7:
				Anchor TLB1 = new Anchor( lo.x, C.y, C.z );
				Anchor TLB2 = new Anchor( C.x, hi.y, hi.z );
				return new OctreeCellSpace( TLB1, TLB2 );
			default:
				throw new RuntimeException( "invalid subcell space index '"+i+"' for oct tree" );
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
		sb.append( ", " );
		sb.append( lo.z );
		sb.append( " -> " );
		sb.append( hi.x );
		sb.append( ", " );
		sb.append( hi.y );
		sb.append( ", " );
		sb.append( hi.z );
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