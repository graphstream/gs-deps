package org.miv.pherd;

/**
 * Generator for particles. 
 *
 * @author Antoine Dutot
 * @since 2007
 */
public interface ParticleFactory
{
	/**
	 * Generate a new particle.
	 * @param id Particle unique identifier.
	 * @param x Particle start abscissa.
	 * @param y Particle start ordinate.
	 * @param z Particle start depth.
	 * @return The new particle.
	 */
	public Particle newParticle( Object id, double x, double y, double z );
}