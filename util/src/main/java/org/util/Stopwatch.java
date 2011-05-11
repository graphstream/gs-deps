package org.util;

/**
 * Allows to mesure time between two calls to a method.
 * 
 * <p>Allows to measure time intervals, averages time intervals, maximum
 * and minimum time intervals.</p>
 *
 * <p>Works only with JDK1.5 and later, needs System.nanoTime().</p>
 *
 * @author Antoine Dutot
 * @since 20040607
 */
public class Stopwatch
{
// Attributes

	/**
	 * Last measurement taken in nanoseconds.
	 */
	protected long time = 0;

	/**
	 * Used during the measurement.
	 */
	protected long t1 = 0;

	/**
	 * Used to compute the average time. This variable accumulates the values
	 * of {@link #time}.
	 */
	protected long avg_acc = 0;

	/**
	 * Average of the measurement on {@link #ncalls} calls.
	 */
	protected double avg_time = 0;

	/**
	 * Minimum measurements.
	 */
	protected long min_time = 100000000;

	/**
	 * Maximum measurements.
	 */
	protected long max_time = 0;

	/**
	 * Number of calls, used to compute the average measurement.
	 */
	protected int ncalls = 0;

// Constructors

	public
	Stopwatch()
	{
	}

// Accessors

	/**
	 * Last interval measurement in nanoseconds. This is the time elapsed
	 * between the two last calls to {@link #measure()}.
	 * @return A number of nanoseconds.
	 */
	public long
	getTime()
	{
		return time;
	}

	/**
	 * Last interval measurement in milliseconds. This is the time elapsed
	 * between the two last calls to {@link #measure()}.
	 * @return A number of milliseconds.
	 */
	public double
	getTimeMillis()
	{
		return (time/1000000.0);
	}
	
	/**
	 * The time in a nicely formated string in seconds.
	 * @return The last time measurement formated as a string.
	 */
	public String
	getTimeStringInSeconds()
	{
		return String.format( "%.3fs", time/1000000000.0 );
	}

	/**
	 * Average time in nanoseconds.
	 * @return The average number of nanoseconds between measurements.
	 * @see #getCallCount()
	 */
	public double
	getAverageTime()
	{
		return avg_time;
	}

	/**
	 * Average time in milliseconds.
	 * @return The average number of milliseconds between measurements.
	 * @see #getCallCount()
	 */
	public double
	getAverageTimeMillis()
	{
		return (avg_time/1000000.0);
	}

	/**
	 * Minimum time measurement.
	 * @return The minimum measurement in nanoseconds.
	 */
	public long
	getMinTime()
	{
		return min_time;
	}

	/**
	 * Maximum time measurement.
	 * @return The maximum measurement in nanoseconds.
	 */
	public long
	getMaxTime()
	{
		return max_time;
	}

	/**
	 * Minimum time measurement.
	 * @return The minimum measurement in milliseconds.
	 */
	public double
	getMinTimeMillis()
	{
		return (((double)min_time)/1000000);
	}

	/**
	 * Maximum time measurement.
	 * @return The maximum measurement in milliseconds.
	 */
	public double
	getMaxTimeMillis()
	{
		return (((double)max_time)/1000000);
	}

	/**
	 * Number of calls to {@link #measure()}. Used to compute the average time.
	 * @return The number of measurements.
	 * @see #getAverageTime()
	 */
	public long
	getCallCount()
	{
		return ncalls;
	}

	/**
	 * Time ellapsed since the last call to {@link #measure()}. This does not
	 * modify the result of {@link #getTime()} or {@link #getCallCount()},
	 * however naturally this takes time!.
	 * @return The number of nanoseconds since the last measurement.
	 */
	public long
	getTimeSinceLastMeasure()
	{
		return( System.nanoTime() - t1 );
	}

	/**
	 * Like {@link #getTimeSinceLastMeasure()} but in milliseconds.
	 * @return The number of milliseconds since the last measurement.
	 */
	public double
	getTimeSinceLastMeasureMillis()
	{
		return ((( System.nanoTime() - t1 )) / 1000000.0);
	}

// Commands

	/**
	 * If called for the first only store the current time, else, measure the
	 * time between this call and the previous one and store the current time
	 * for the next call.
	 */
	public void
	measure()
	{
		if( ncalls > 0 )
		{
			// Mesure time since the previous call.

			time      = System.nanoTime() - t1;
			avg_acc  += time;
			avg_time  = ((double)avg_acc) / ((double)ncalls);
			min_time  = time < min_time ? time : min_time;
			max_time  = time > max_time ? time : max_time;
		}

		// For the next call.

		t1 = System.nanoTime();
		ncalls++;
	}

	/**
	 * Reset the number of calls and the average to zero.
	 */
	public void
	resetAverage()
	{
		avg_acc  = 0;
		avg_time = 0;
		ncalls   = 0;
	}
}
