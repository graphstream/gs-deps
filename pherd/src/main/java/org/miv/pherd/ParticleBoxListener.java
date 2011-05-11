package org.miv.pherd;

/**
 * Listener for particle events.
 *
 * @author Antoine Dutot
 * @since 2007
 */
public interface ParticleBoxListener
{
	/**
	 * A new particle appeared at (x,y,z).
	 * @param id The particle unique identifier.
	 * @param x The particle abscissa.
	 * @param y The particle ordinate.
	 * @param z The particle depth.
	 */
	void particleAdded( Object id, double x, double y, double z );
	
	/**
	 * A particle moved.
	 * @param id The particle unique identifier.
	 * @param x The new abscissa.
	 * @param y The new ordinate.
	 * @param z The new depth.
	 */
	void particleMoved( Object id, double x, double y, double z );

	/**
	 * One of the particle attributes changed value.
	 * @param id The particle identifier.
	 * @param attribute The attribute name.
	 * @param newValue The attribute new value (null if removed).
	 * @param removed If true the attribute is being removed.
	 */
	void particleAttributeChanged( Object id, String attribute, Object newValue, boolean removed );
	
	/**
	 * A particle disappeared.
	 * @param id The particle unique identifier.
	 */
	void particleRemoved( Object id );
	
	/**
	 * All particles moved and a new step is about to begin.
	 * @param time The finished step time.
	 */
	void stepFinished( int time );
}