package nick.jgame;

import java.awt.Dimension;
import java.io.File;

/**
 * A class to store gamewide static unchanging variables and methods.
 *
 * @author Nick
 * @category Game-Wide Stuff
 */
public final class Constants {

	// Static variables
	/**
	 * The resource folder location.
	 */
	public static final String	assetsLoc		= "res" + File.separator;

	/**
	 * The game's displayable name.
	 */
	public static final String	gameName		= "Commander (Working Title)";

	/**
	 * The number of nanoseconds in one millisecond (For conversion purposes).
	 */
	public static final int		nsInMs			= 1_000_000;

	/**
	 * The number of nanoseconds in one second (For conversion purposes).
	 */
	public static final int		nsInSec			= 1_000_000_000;

	/**
	 * The number of expected nanoseconds to render a frame.
	 */
	public static final double	nsInTick		= Constants.getNSPerTick((short) 60);

	public static final short	windowHeight	= 800;

	public final static short	windowWidth		= 1000;

	// Static Methods

	/**
	 * @category Convenience
	 * @return The text in the title bar
	 */
	public static String getDisplayName( ) {

		return gameName + " | " + GameVersion.getVersion( );
	}

	/**
	 * @category Convenience
	 * @return The distance, in pixels, to the middle of the window.
	 */
	public static short getMidWidth( ) {

		return windowWidth / 2;
	}

	public static double getNSPerTick(final short ticksPerSecond) {

		return 1_000_000_000 / ticksPerSecond;
	}

	public static Dimension getSizeOfWindow( ) {

		return new Dimension(windowWidth, windowHeight);
	}

	public static float getWidthToHeight( ) {

		return windowWidth / windowHeight;
	}
}
