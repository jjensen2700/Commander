package nick.jgame.util.math;

import nick.jgame.opts.Options;

public final class MathUtil {

	public static String addFrontZero(final short num) {

		if ((num >= 0) && (num < 10)) { return "0" + String.valueOf(num); }
		return String.valueOf(num);
	}

	public static int convertToArrayLoc(final int x, final int y, final int width) {

		return x + (y * width);
	}

	public static long convertToLong(final String toChange) {

		long toRet = 1;
		byte[ ] chars = toChange.getBytes( );

		for (byte num : chars) {
			if ((num * toRet) <= Long.MAX_VALUE) {
				toRet *= num;
			} else {
				if ((num + toRet) <= Long.MAX_VALUE) {
					toRet += num;
				} else {
					toRet -= num;
				}
			}
		}
		return toRet;
	}

	public static byte getPrefPriority(final int updates, final byte current) {

		return getPrefPriority((short) 60, updates, current);
	}

	public static byte getPrefPriority(final short fps, final int updates, final byte current) {

		if (!Options.getBoolOption("adaptthreads")) { return current; }
		if (((fps <= 30) || (updates < 57)) && (current < Thread.MAX_PRIORITY)) { return (byte) (current + 1); }

		return current;
	}

}
