package org.util.geom;


/**
 * 2D rectangle.
 *
 * @author Antoine Dutot
 * @since 20001121
 * @version 0.1
 */
public class Rectangle
	implements java.io.Serializable
{
// Attributes

	private static final long serialVersionUID = -7996459628848481249L;

	public float x;

	public float y;

	public float w;

	public float h;

// Constructors

	public
	Rectangle()
	{
	}

	public
	Rectangle( float x, float y, float w, float h )
	{
		set( x, y, w, h );
	}
	
	public
	Rectangle( Rectangle other )
	{
		set( other.x, other.y, other.w, other.h );
	}

// Commands

	public void
	copy( Rectangle other )
	{
		x = other.x;
		y = other.y;
		w = other.w;
		h = other.h;
	}

	/**
	 * Set all the rectangle coordinates.
	 */
	public void
	set( float x, float y, float w, float h )
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	/**
	 * Move the rectangle at absolute position (x,y).
	 */
	public void
	move_to( float x, float y )
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Move the rectangle of vector (dx,dy).
	 */
	public void
	move( float dx, float dy )
	{
		x += dx;
		y += dy;
	}

	/**
	 * Set the size of the rectangle to (w,h).
	 */
	public void
	size( float w, float h )
	{
		this.w = w;
		this.h = h;
	}
}