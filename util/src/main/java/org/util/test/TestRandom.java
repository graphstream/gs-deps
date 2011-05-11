package org.util.test;

import org.util.Random;

/**
 * Tests The {@link Random} class.
 * @author yoann
 * 
 */
public class TestRandom
{
	public static void main(String[] str)
	{
		System.out.printf("--------------%n");
		Random.reinit(23);		
		System.out.printf( "3 random numbers : %1.4f %1.4f and %1.4f%n", (float)Random.next(), (float)Random.next(), (float)Random.next() );
		System.out.printf( "reinit the seed%n");
		Random.reinit();
		System.out.printf( "3 random numbers : %1.4f %1.4f and %1.4f%n", (float)Random.next(), (float)Random.next(), (float)Random.next() );
		

		System.out.printf("%n--------------%n");
		System.out.printf( "A multi-thread try%n");
		for(int i = 0; i< 2; i++)
		{
			Runnable t = new MyThread();
			( (Thread) t ).start();
		}	
	}
}
	class MyThread extends Thread
	{
		@Override
        public void run()
		{
			double id = Random.next();
			for(int i=0; i<5; i++)
			{
				yield();
				System.out.printf( "thread %3.0f  -> 3 random numbers : %1.4f %1.4f and %1.4f%n", (float)(id*1000.0), (float)Random.next(), (float)Random.next(), (float)Random.next() );
			}
		}
	}
