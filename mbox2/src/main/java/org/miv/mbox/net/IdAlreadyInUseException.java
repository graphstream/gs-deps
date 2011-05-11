package org.miv.mbox.net;

public class IdAlreadyInUseException extends Exception
{
	public IdAlreadyInUseException( String message )
	{
		super( message );
	}
}