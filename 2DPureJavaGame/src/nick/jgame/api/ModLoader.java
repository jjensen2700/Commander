package nick.jgame.api;

import nick.jgame.util.debug.GameLog;

public class ModLoader {

	public static void loadMod(final Object modMain) {

		if (!(modMain instanceof ModInfoHeader) || !(modMain instanceof BasicMod)) {
			GameLog.warn(modMain + " has not been properly setup!");
			return;
		}
		BasicMod mod = (BasicMod) modMain;
		mod.initMod( );
	}
}
