package org.util;

/**
 * 
 * InvalidOperationException.
 *
 * <p>
 * Raised when an operation is not supported.
 * </p>
 *
 * @author Antoine Dutot
 * @since 20061226
 */
public class InvalidOperationException extends RuntimeException
{
	private static final long serialVersionUID = -2883757740020104927L;

	public InvalidOperationException()
	{
	}
	
	public InvalidOperationException( String message )
	{
		super( message );
	}
}