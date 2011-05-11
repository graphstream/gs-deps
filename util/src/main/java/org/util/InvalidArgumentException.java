package org.util;

/**
 * Thrown when an argument to a method is not usable.
 *
 * @author Antoine Dutot
 * @author Yoann Pigné
 * @since 20000703
 * @version 0.1
 */
public class InvalidArgumentException
	extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7888596242044798399L;

	public
	InvalidArgumentException()
	{
		super( "invalid argument" );
	}

	public
	InvalidArgumentException( String message )
	{
		super( "invalid argument: " + message );
	}
}
