package org.util.geom;


/**
 * A four component vector made of floats.
 *
 * @author Antoine Dutot
 * @since 20000613
 * @version 0.1
 */
public class Vector4
	implements java.io.Serializable
{
// Attributes:
	
	private static final long serialVersionUID = 2165036252206338694L;

	/**
	 * Sequence of 4 coefficients.
	 */
	public float data[];

// Constructors
	
	/**
	 * New zero vector.
	 */
	public
	Vector4()
	{
		data    = new float[4];
		data[0] = 0;
		data[1] = 0;
		data[2] = 0;
		data[3] = 0;
	}
	
	/**
	 * New (x, y, z, w) vector.
	 */
	public
	Vector4( float x, float y, float z, float w )
	{
		data    = new float[4];
		data[0] = x;
		data[1] = y;
		data[2] = z;
		data[3] = w;
	}
	
	/**
	 * New vector copy of other.
	 */
	public
	Vector4( Vector4 other )
	{
		data = new float[4];
		copy( other );
	}

	/**
	 * New vector copy of other.
	 *
	 * w is set to 1.
	 */
	public
	Vector4( Vector3 other )
	{
		data    = new float[4];
		data[0] = other.data[0];
		data[1] = other.data[1];
		data[2] = other.data[2];
		data[3] = 1;
	}

// Predicates

	/**
	 * Are all components to zero?.
	 */
	public boolean
	isZero()
	{
		return( data[0] == 0 && data[1] == 0 && data[2] == 0 && data[3] == 0 );
	}
	
	/**
	 * Is this equal to other ?
	 */
	@Override
	public boolean
	equals( Object other ) 
	{
		Vector4 v;

		if( ! ( other instanceof Vector4 ) )
		{
			return false;
		}

		v = (Vector4) other;
	
		return( data[0] == v.data[0]
			&&  data[1] == v.data[1]
			&&  data[2] == v.data[2]
			&&  data[3] == v.data[3] );
	}
	
	/**
	 * Is this equal to other ?
	 */
	public boolean
	equals( Vector4 other ) 
	{
		return( data[0] == other.data[0]
			&&  data[1] == other.data[1]
			&&  data[2] == other.data[2]
			&&  data[3] == other.data[3] );
	}
	
	/**
	 * Is i the index of a component ?
	 *
	 * In other words, is i &gt;= 0 and &lt; than #count() ?
	 */
	public boolean
	validComponent( int i ) 
	{
		return( i >= 0 && i < 4 );
	}

// Accessors:
	
	/**
	 * i-th element.
	 */
	public float
	at( int i )
	{
		return data[i];
	}
	
	@Override
	public Object
	clone()
	{
		return new Vector4( this );
	}

// Accessors
	
	/**
	 * Dot product of this and other.
	 */
	public float
	dotProduct( Vector4 other ) 
	{
		return( ( data[0] * other.data[0] )
		      + ( data[1] * other.data[1] )
		      + ( data[2] * other.data[2] )
			  + ( data[3] * other.data[3] ) );
	}
	
	/**
	 * Cartesian length for only the three first components.
	 * @see #length()
	 */
	public float
	length3() 
	{
		return (float) Math.sqrt( ( data[0] * data[0] ) + ( data[1] * data[1] ) + ( data[2] * data[2] ) );
	}
	
	/**
	 * Cartesian length (for the four components).
	 * @see #length3()
	 */
	public float
	length() 
	{
		return (float) Math.sqrt( ( data[0] * data[0] ) + ( data[1] * data[1] ) + ( data[2] * data[2] ) + ( data[3] * data[3] ) );
	}

// Commands
	
	/**
	 * Assign value to all elements.
	 */
	public void
	fill( float value )
	{
		data[0] = data[1] = data[2] = data[3] = value;
	}

	/**
	 * Explicitly set the i-th component to value.
	 */
	public void
	set( int i, float value )
	{
			data[i] = value;
	}
	
	/**
	 * Explicitly set the four components.
	 */
	public void
	set( float x, float y, float z, float w )
	{
		data[0] = x;
		data[1] = y;
		data[2] = z;
		data[3] = w;
	}
	
	/**
	 * Add each element of other to the corresponding element of this.
	 */
	public void
	add( Vector4 other )
	{
		data[0] += other.data[0];
		data[1] += other.data[1];
		data[2] += other.data[2];
		data[3] += other.data[3];
	}
	
	/**
	 * Substract each element of other to the corresponding element of this.
	 */
	public void
	sub( Vector4 other )
	{
		data[0] -= other.data[0];
		data[1] -= other.data[1];
		data[2] -= other.data[2];
		data[3] -= other.data[3];
	}
	
	/**
	 * Multiply each element of this by the corresponding element of other.
	 */
	public void
	mult( Vector4 other )
	{
		data[0] *= other.data[0];
		data[1] *= other.data[1];
		data[2] *= other.data[2];
		data[3] *= other.data[3];
	}
	
	/**
	 * Add value to each element.
	 */
	public void
	scalarAdd( float value )
	{
		data[0] += value;
		data[1] += value;
		data[2] += value;
		data[3] += value;
	}

	/**
	 * Substract value to each element.
	 */
	public void
	scalarSub( float value )
	{
		data[0] -= value;
		data[1] -= value;
		data[2] -= value;
		data[3] -= value;
	}
	
	/**
	 * Multiply each element by value.
	 */
	public void
	scalarMult( float value )
	{
		data[0] *= value;
		data[1] *= value;
		data[2] *= value;
		data[3] *= value;
	}
	
	/**
	 * Divide each element by value.
	 */
	public void
	scalarDiv( float value )
	{
		data[0] /= value;
		data[1] /= value;
		data[2] /= value;
		data[3] /= value;
	}
	
	/**
	 * Divide the first three components by the fourth.
	 */
	public float
	normalize()
	{
		if( data[3] != 0 )
		{
			data[0] /= data[3];
			data[1] /= data[3];
			data[2] /= data[3];
		}
		
		return data[3];
	}

// Utility
	
	/**
	 * Make this a copy of other.
	 */
	public void
	copy(  Vector4 other )
	{
		data[0] = other.data[0];
		data[1] = other.data[1];
		data[2] = other.data[2];
		data[3] = other.data[3];
	}
	
// Miscellany

	@Override
	public String
	toString()
	{
		StringBuffer sb = new StringBuffer( "[" );
		
		sb.append( data[0] );
		sb.append( '|' );
		sb.append( data[1] );
		sb.append( '|' );
		sb.append( data[2] );
		sb.append( '|' );
		sb.append( data[3] );
		sb.append( ']' );

		return sb.toString();
	}
}