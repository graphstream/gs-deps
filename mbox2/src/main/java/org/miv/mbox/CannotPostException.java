package org.miv.mbox;

/**
 * Raised when a message cannot be posted for any reason
 *
 * @author Antoine Dutot
 * @since 20061210
 */
public class CannotPostException extends Exception
{
	private static final long serialVersionUID = -2683110426482658162L;

	/**
	 * New post exception.
	 * @param message The reason.
	 */
	public CannotPostException( String message )
	{
		super( message );
	}
}