package nick.jgame.util.debug;

import nick.jgame.opts.Options;

public final class GameLog {

	private enum Levels {
		DEBUG("debug"), INFO("info"), WARN("warning");

		private final String	type;

		private Levels(final String title) {

			type = title.toUpperCase( ).trim( );
		}

		public String getTitle( ) {

			return "[" + type + "]";
		}
	}

	public static final void info(final String txt, final boolean isDebug) {

		if (isDebug && !Options.getBoolOption("debugprinting")) { return; }

		if (isDebug) {
			System.out.println(Levels.DEBUG.getTitle( ) + " " + txt);
		} else {
			System.out.println(Levels.INFO.getTitle( ) + " " + txt);
		}
	}

	public static final void warn(final Exception e) {

		System.err.println(Levels.WARN.getTitle( ) + " " + e.getMessage( ));
	}

	public static final void warn(final String txt) {

		System.err.println(Levels.WARN.getTitle( ) + " " + txt);
	}

}
