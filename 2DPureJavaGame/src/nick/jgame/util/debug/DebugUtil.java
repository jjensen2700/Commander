package nick.jgame.util.debug;

import nick.jgame.gfx.Render;
import nick.jgame.opts.Options;

public final class DebugUtil {

	public static void drawRect(final Render rend, final int x, final int y, final int width, final int height,
			final int color) {

		if (!Options.getBoolOption("debugrender")) { return; }
		for (int xL = x; xL < (x + width); xL++) {
			rend.setPixel(color, xL, y);
			rend.setPixel(color, xL, y + height);
		}
		for (int yL = y; yL < (y + height); yL++) {
			rend.setPixel(color, x, yL);
			rend.setPixel(color, x + width, yL);

		}
	}
}
