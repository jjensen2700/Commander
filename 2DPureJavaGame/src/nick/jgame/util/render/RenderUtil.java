package nick.jgame.util.render;

import java.awt.Graphics;

import nick.jgame.MainGame;

public final class RenderUtil {

	public static final Graphics getGameGraphics( ) {

		return MainGame.getInst( ).getBufferStrategy( ).getDrawGraphics( );
	}
}
