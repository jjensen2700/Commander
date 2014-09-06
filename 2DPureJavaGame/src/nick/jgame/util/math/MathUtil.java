package nick.jgame.util.math;

import nick.jgame.MainGame;
import nick.jgame.opts.Options;

public final class MathUtil {

	public static String addFrontZero(final short num) {

		if ((num >= 0) && (num < 10)) { return '0' + String.valueOf(num); }
		return String.valueOf(num);
	}

	public static long convertToLong(final String toChange) {

		long toRet = 1;
		final byte[ ] chars = toChange.getBytes( );

		for (final byte num : chars) {
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

		return getPrefPriority(MainGame.getFPS( ), updates, current);
	}

	public static byte getPrefPriority(final short fps, final int updates, final byte current) {

		if (!Options.getBoolOption("adaptthreads")) { return current; }
		if ((fps <= Options.getValueOption("minfps")) && (current < Thread.MAX_PRIORITY)) { return (byte) (current + 1); }

		return current;
	}

}
