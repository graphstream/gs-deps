package org.miv.pherd;

public class IdAlreadyInUseException extends RuntimeException
{
	public IdAlreadyInUseException( String message )
	{
		super( message );
	}
	
	public IdAlreadyInUseException()
	{
	}
}