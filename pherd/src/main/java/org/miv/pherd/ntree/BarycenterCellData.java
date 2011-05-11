package org.miv.pherd.ntree;

import java.util.*;

import org.miv.pherd.geom.*;
import org.miv.pherd.*;

/**
 * Cell data that represent the barycenter of a cell.
 * 
 * <p>
 * For leaf cells the barycenter is computed from the particles in the cell. For
 * non-lead cell, the barycenter represents the barycenters of each subcell.
 * </p>
 *
 * @author Antoine Dutot
 * @since 2007
 */
public class BarycenterCellData implements CellData
{
// Attributes
	
	/**
	 * The barycenter.
	 */
	public Point3 center;
	
	/**
	 * The importance of this centre (in other words, the number of particles
	 * represented by this barycenter).
	 */
	public double weight;
	
	/**
	 * The cell this data is attached to.
	 */
	public Cell cell;
	
	/**
	 * Allows debugging.
	 */
	public Object marked = null;
	
// Constructors
	
	public BarycenterCellData()
	{
		center = new Point3( 0, 0, 0 );
	}

// Access
	
	/**
	 * The barycenter.
	 * @return a point.
	 */
	public Point3 getCenter()
	{
		return center;
	}
	
	/**
	 * The importance of the barycenter. This is the number of particles this
	 * barycenter represents.
	 * @return The weight.
	 */
	public double getWeight()
	{
		return weight;
	}
	
	/**
	 * Distance between this barycentre and a point p. 
	 * @param p The point to consider (the other is the barycentre).
	 * @return The distance between the point and this barycentre.
	 */
	public double distanceFrom( Point3 p )
	{
		return p.distance( center );
	}
	
	public CellData newCellData()
	{
		return new BarycenterCellData();
	}
	
// Command

	public void setCell( Cell cell )
	{
		this.cell = cell;
	}

	public void recompute()
	{
		double x = 0;
		double y = 0;
		double z = 0;
		double n = 0;
		
		weight = 0;
		
		if( cell.isLeaf() )
		{
			Iterator<? extends Particle> particles = cell.getParticles();
			
			while( particles.hasNext() )
			{
				Particle particle = particles.next();
				
				x += particle.getPosition().x; 
				y += particle.getPosition().y; 
				z += particle.getPosition().z; 
				
				weight += particle.getWeight();
				
				n++;
			}
			
			if( n > 0 )
			{
				x /= n;
				y /= n;
				z /= n;
			}
			
			center.set( x, y, z );
		}
		else
		{
			double subcnt = cell.getSpace().getDivisions();
			double totpop = cell.population;
			int   verif  = 0;
			
			if( totpop > 0 )
			{
				for( int i=0; i<subcnt; ++i )
				{
					Cell               subcell = cell.getSub( i );
					BarycenterCellData data    = (BarycenterCellData) subcell.getData();
					double              pop    = subcell.population;
		
					verif += pop;
					
					x += data.center.x * pop; 
					y += data.center.y * pop;
					z += data.center.z * pop;
					
					weight += data.weight;
				}
				
				assert verif == totpop : "Discrepancy in population counts ?";
				
				x /= totpop;
				y /= totpop;
				z /= totpop;
			}

			center.set( x, y, z );
		}
		
		for( NTreeListener listener: cell.tree.listeners )
		{
			listener.cellData( cell.getId(), "barycenter", this );
		}
	}
}