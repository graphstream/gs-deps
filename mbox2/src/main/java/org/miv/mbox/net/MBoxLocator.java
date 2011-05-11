package org.miv.mbox.net;

/**
 * Address of a message box.
 *
 * An object locator is made of three parts:
 * <ul>
 * 	<li>the host name,</li>
 * 	<li>the port,</li>
 * 	<li>the message box name.</li>
 * </ul>
 *
 * Some object locators only identify hosts when the name is not set. This
 * correspond to what is called the "hostId". An host id is made according to
 * the pattern "//" + hostname + ":" + port.
 *
 * @author Antoine Dutot
 * @since 20040624
 */
public class MBoxLocator
	implements java.io.Serializable
{
// Attributes

	private static final long serialVersionUID = 145871486067935485L;

	/**
	 * Host name of the URL.
	 */
	protected String hostname;

	/**
	 * Port of the URL.
	 */
	protected int port;

	/**
	 * Identifier part of the URL.
	 */
	protected String name;

	/**
	 * Computed host id (Host and port without the name) to avoid recreating it.
	 */
	protected String hostId;

// Constructors

	/**
	 * New locator according to the host name and port only.
	 * @param hostname Computer name.
	 * @param port Network port number.
	 */
	public
	MBoxLocator( String hostname, int port )
	{
		this( hostname, port, "" );
	}

	/**
	 * New locator according to the host name, port and message box name.
	 * @param hostname Computer name.
	 * @param port Network port number.
	 * @param name Message box identifier.
	 */
	public MBoxLocator( String hostname, int port, String name )
	{
		this.hostname = hostname;
		this.port     = port;
		this.name     = name;
		this.hostId   = "//" + hostname + ":" + port;
	}

	/**
	 * New locator created by cutting the given string into fields. The expected
	 * format can be one of:
	 * <ul>
	 * 		<li>"//" + hostname + ":" + port + "/" name</li>
	 * 		<li>"//" + hostname + "/" name</li>
	 * 		<li>"//" + hostname + ":" + port</li>
	 * 		<li>"//" + hostname</li>
	 * </ul>
	 * @param locator_string The string to cut in fields.
	 * @throws IllegalArgumentException If the string does not complies with the
	 * expected format.
	 */
	public MBoxLocator( String locator_string )
		throws IllegalArgumentException
	{
		int n1 = -1, n2 = -1, n3 = -1, n4 = -1;
		String s_port = "0";

		n1 = locator_string.indexOf( '/' );

		if( n1 < 0 )
			throw new IllegalArgumentException( "invalid locator string '" + locator_string + "'" );

		n2 = locator_string.indexOf( '/', n1 + 1 );

		if( n2 < 0 )
			throw new IllegalArgumentException( "invalid locator string '" + locator_string + "'" );
	
		n3 = locator_string.indexOf( ':', n2 + 1 );

		if( n3 < 0 )
		{
			n3 = locator_string.indexOf( '/', n2 + 1 );

			if( n3 < 0 )
				throw new IllegalArgumentException( "invalid locator string '" + locator_string + "'" );
		}
		else
		{
			n4 = locator_string.indexOf( '/', n3 + 1 );

			if( n4 < 0 )
				throw new IllegalArgumentException( "invalid locator string '" + locator_string + "'" );
		}

		hostname = locator_string.substring( n2 + 1, n3 );

		if( n4 >= 0 )
		{
			s_port = locator_string.substring( n3 + 1, n4 );

			if( n4 < locator_string.length() - 1 )
			     name = locator_string.substring( n4 + 1 );
			else name = "";
		}
		else
		{
			if( n3 < locator_string.length() - 1 )
			     name = locator_string.substring( n3 + 1 );
			else name = "";
		}

		try
		{
			port = Integer.parseInt( s_port );
		}
		catch( NumberFormatException e )
		{
			throw new IllegalArgumentException( "invalid locator string (invalid port) '" + locator_string + "'" );
		}

		this.hostId = "//" + hostname + ":" + port;
	}

// Access

	/**
	 * Host name part.
	 * @return the host name only.
	 */
	public String getHostname()
	{
		return hostname;
	}

	/**
	 * Port of this locator.
	 * @return the port only.
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * Name of this locator.
	 * @return the message box name only.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * String containing only the hostname and port under the form
	 * "//hostname:port". Two objects on the same host will have the same host
	 * identifier.
	 * @return The host identifier.
	 */
	public String getHostId()
	{
		return hostId;
	}

	@Override
	public String toString()
	{
		if( port > 0 )
		     return "//" + hostname + ":" + port + "/" + name;
		else return "//" + hostname + "/" + name;
	}

// Command

	/**
	 * Change the host name of this locator.
	 * @param hostname Hostname of this locator.
	 */
	public void setHostname( String hostname )
	{
		this.hostname = hostname;
		this.hostId   = "//" + hostname + ":" + port;
	}
	
	/**
	 * Change the port of this locator.
	 * @param port Port of this locator.
	 */
	public void setPort( int port )
	{
		this.port = port;
		this.hostId = "//" + hostname + ":" + port;
	}

	/**
	 * Change the message box name of this locator.
	 * @param name Name of this locator.
	 */
	public void setName( String name )
	{
		this.name = name;
	}
}