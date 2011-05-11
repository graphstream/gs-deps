package org.util.set;

/**
 * Raised when an index was out of its range of possible values.
 *
 * @author Antoine Dutot
 * @since 20000701
 * @version 0.1
 */
public class InvalidIndexException
	extends RuntimeException
{
	private static final long serialVersionUID = 5843059698058058951L;

	/**
	 * The invalid index.
	 */
	public long index;

	public
	InvalidIndexException()
	{
		super( "invalid index" );
	}
	
	public
	InvalidIndexException( long index )
	{
		super( "invalid index " + index );
		this.index = index;
	}

	public
	InvalidIndexException( String message )
	{
		super( "invalid index: " + message );
	}

	public
	InvalidIndexException( String message, long index )
	{
		super( "invalid index " + index + ": " + message );
		this.index = index;
	}
}