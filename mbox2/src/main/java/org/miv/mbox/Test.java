package org.miv.mbox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Test
{
	public static void main( String args[] )
	{
		try
		{
			new Test();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	public Test() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		long t1, t2;
		float T1, T2;
		int  N = 100000;
		
		t1 = System.nanoTime();
		
		for( int i=0; i<N; i++ )
		{
			truc();
		}
		
		t2 = System.nanoTime();
		T1 = t2 - t1;
		
		Class<?> c = getClass();
		Method m = c.getMethod( "truc" );
	
		t1 = System.nanoTime();
		
		for( int i=0; i<N; i++ )
		{
			m.invoke( this );
		}
		
		t2 = System.nanoTime();
		T2 = t2 - t1;
		
		T1 /= 1000000000;
		T2 /= 1000000000;
	
		System.out.printf( "Times :%n" );
		System.out.printf( "   normal %f secs%n", T1 );
		System.out.printf( "   invoke %f secs%n", T2 );
		System.out.flush();
	}
	
	public float truc()
	{
		float a = 5;
		float b = 2;
		
		for( int i=0; i<10000; i++ )
		{
			a = a + b;
		}
		
		return a;
	}
}
