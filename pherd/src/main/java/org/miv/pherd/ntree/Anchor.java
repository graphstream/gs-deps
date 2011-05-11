package org.miv.pherd.ntree;

import org.miv.pherd.geom.*;

/**
 * A constant point.
 * 
 * <p>
 * An anchor is guaranteed to remain at the same place, and therefore can be
 * passed between threads since once created, it is read-only.
 * </p>
 *
 * @author Antoine Dutot
 * @since 2007
 */
public class Anchor extends Point3
{
	private static final long serialVersionUID = -7743180985563247767L;

	public Anchor( double x, double y, double z )
	{
		super( x, y, z );
	}
}