package org.util;

/**
 * Mem.
 *
 * @author Antoine Dutot
 * @since 20040929
 */
public class Mem
{
	public static long
	getMemoryUsed()
	{
		return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}
	
	public static String
	getMemoryUsedString()
	{
		return getMemoryUsedString( getMemoryUsed() );
	}

	public static String
	getMemoryUsedString( long mem )
	{
		String M;

		if( mem > 1024*1024 )
		{
			M = Long.toString( mem / (1024*1024) ) + "Mb";
		}
		else if( mem > 1024 )
		{
			M = Long.toString( mem / 1024 ) + "Kb";
		}
		else
		{
			M = Long.toString( mem ) + "b";
		}

		return M;
	}
}