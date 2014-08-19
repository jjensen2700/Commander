package nick.jgame;

public final class GameVersion {

	// Static variables (All public)

	/**
	 * The bugfix version number (Resets every minor version)
	 */
	public static final byte	bugfix			= 1;

	/**
	 * The minor bugfix version letter (Resets every bugfix)
	 */
	public static final char	bugfixChar		= 'c';

	/**
	 * The major version number (Resets every release).
	 */
	public static final byte	majorVersion	= 0;

	/**
	 * The minor version number (Resets every major version).
	 */
	public static final byte	minorVersion	= 0;

	/**
	 * The release version number (Never resets).
	 */
	public static final byte	release			= 0;

	// Static Methods
	/**
	 * @category Convenience
	 * @return A string of how the version should be displayed.
	 */
	public static String getVersion( ) {

		return release + "." + majorVersion + '.' + minorVersion + "." + bugfix + bugfixChar;
	}

}
