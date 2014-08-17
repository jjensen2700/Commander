package nick.jgame.util.debug;

import nick.jgame.Constants;

public final class GameProfiler {

	// Instance variables (All private)
	private int				invocations	= 0;

	private boolean			isTiming;

	private final String	name;

	private long			startTime	= 0;

	private long			totalTime	= 0;

	public GameProfiler(final String name) {

		this.name = name;
	}

	// Instance methods (All public)
	/**
	 * Call to print out the data the profiler collected and reset it.
	 *
	 * @param message
	 *            A string appended to the beginning of the printout.
	 * @param inDividend
	 *            A number to divide the time called.
	 * @return The time in milliseconds.
	 */
	public final double displayAndReset(final String message, final int inDividend) {

		if (isTiming) {
			GameLog.warn("Forgot to stop timing " + name + "! Stopping for you.");
			stopTiming( );
		}
		int dividend = inDividend;
		if (dividend == 0) {
			dividend = invocations;
		}
		long time;
		if (dividend == 0) {
			time = 0;
		} else {
			time = (totalTime / dividend);
		}
		final double ms = (time / Constants.nsInMs);
		GameLog.info(message + ms + " ms", true);
		totalTime = 0;
		invocations = 0;
		return ms;
	}

	public final void startTiming( ) {

		if (isTiming) {
			GameLog.warn("Still timing " + name + "!");
			return;
		}

		startTime = System.nanoTime( );
		isTiming = true;
	}

	public final void stopTiming( ) {

		if (!isTiming) {
			GameLog.warn("StartTiming was not called before StopTiming!!!");
			return;
		}
		isTiming = false;
		invocations++;
		totalTime += (System.nanoTime( ) - startTime);
		startTime = 0;
	}
}
