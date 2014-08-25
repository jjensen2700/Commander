package nick.jgame.gfx;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import nick.jgame.util.debug.GameLog;
import nick.jgame.util.math.MathUtil;

public final class ScreenShotHandler {

	private static short	coolDown	= 0;

	private static short	shotCounter	= 0;

	public static void takeScreenShot(final BufferedImage img) {

		if (coolDown > 0) {
			GameLog.info("Need to cooldown for " + coolDown + " more tick(s)!", true);
			return;
		}
		GameLog.info("Taking a screenshot...", false);
		final File loc = new File("res/screenshots/screenshot" + MathUtil.addFrontZero(shotCounter) + ".png");

		try {
			boolean made = loc.createNewFile( );
			if (!made) {
				GameLog.warn("?File is made?" + loc.getAbsolutePath( ));
				return;
			}
			ImageIO.write(img, "png", loc);
			shotCounter++;
		} catch (Exception e) {

			GameLog.warn(e.getMessage( ));
		} finally {
			coolDown = 200;
		}
	}

	public static void update( ) {

		coolDown--;
	}

}
