package org.util.test;

import java.io.File;
import java.util.ArrayList;

import org.util.Environment;

/**
 * Tests the {@link org.util.Environment} class and shows an example of use.
 *
 * @author Yoann Pigné
 * @author Antoine Dutot
 * @since  20061108
 */
public class TestEnvironment
{
// Attributes
	
	protected Environment env;
	
// Constructors
	
	public static void
	main( String args[] )
	{
		new TestEnvironment( args );
	}
	
	public
	TestEnvironment( String args[] )
	{
		firstTest( args );
		secondTest();
		thirdTest();
		fourthTest();
	}
	
	protected void
	firstTest( String args[] )
	{
		env = Environment.getGlobalEnvironment();
	
		ArrayList<String> trashcan = new ArrayList<String>();
		
		env.readCommandLine( args, trashcan );
		
		System.out.printf( "Test1: I read the command line and here is my state :%n\t" );
		env.printParameters();
		System.out.printf( "Test1: Here are the unparsed parameters in the command line:%n" );
		System.out.printf( "\t%s%n", trashcan.toString() );
	}
	
	protected void
	secondTest()
	{
		Environment env2 = new Environment();
		
		env2.setParameter( "param1", "val1" );
		env2.setParameter( "param2", "value2" );
		env2.setParameter( "param3", "value3" );
		env2.lockEnvironment( true );
		env2.setParameter( "param1", "value1" );
		env2.setParameter( "param4", "value4" );
		
		if( ! env2.getParameter( "param1" ).equals( "value1" ) ) System.err.printf( "test2: error 1%n" );
		if( ! env2.getParameter( "param2" ).equals( "value2" ) ) System.err.printf( "test2: error 2%n" );
		if( ! env2.getParameter( "param3" ).equals( "value3" ) ) System.err.printf( "test2: error 3%n" );
		if( ! env2.getParameter( "param4" ).equals( "" ) )       System.err.printf( "test2: error 4%n" );
		
		System.out.printf( "Test2: env = %s%n", env2.toString() );
		
		try
		{
			env2.writeParameterFile( "TOTO" );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	protected void
	thirdTest()
	{
		Environment env3 = new Environment();
	
		try
		{
			env3.readParameterFile( "TOTO" );
		
			System.out.printf( "Test3: env = %s%n", env3.toString() );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		File file = new File( "TOTO" );
		file.delete();
	}
	
	protected void
	fourthTest()
	{
		Environment env4 = new Environment();
		TestContainer tc = new TestContainer();
		
		env4.setParameter( "param1", "value1" );
		env4.setParameter( "param2", "12345678" );
		env4.setParameter( "param3", "12345678" );
		env4.setParameter( "param4", "1234.5678" );
		env4.setParameter( "param5", "1234.5678" );
		env4.setParameter( "param6", "true" );
		env4.setParameter( "param7", "invalid!!" );
		
		env4.initializeFieldsOf( tc );
		
		System.out.printf( "Test4: env = %s%n", env4.toString() );
		System.out.printf( "Test4: tc  = %s%n", tc.toString() );
	}
	
// Nested classes

	
protected static class TestContainer
{
	protected String param1;
	protected int param2;
	protected long param3;
	protected float param4;
	protected double param5;
	protected boolean param6;
	protected Object param7nonFunctional;
	
	public void
	setParam1( String value )
	{
		param1 = value;
	}
	
	public void
	setParam2( int value )
	{
		param2 = value;
	}
	
	public void
	setParam3( long value )
	{
		param3 = value;
	}
	
	public void
	setParam4( float value )
	{
		param4 = value;
	}
	
	public void
	setParam5( double value )
	{
		param5 = value;
	}
	
	public void
	setParam6( boolean value )
	{
		param6 = value;
	}
	
	public void
	setParam7( Object value )
	{
		param7nonFunctional = value;
	}
	
	@Override
    public String
	toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append( "param1="+param1 );
		sb.append( ", param2="+param2 );
		sb.append( ", param3="+param3 );
		sb.append( ", param4="+param4 );
		sb.append( ", param5="+param5 );
		sb.append( ", param6="+param6 );
		
		return sb.toString();
	}
}

}