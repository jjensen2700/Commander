package nick.jgame.init;

import nick.jgame.world.World;

public final class Guis {

	public static final GuiMainMenu	mainMenu	= new GuiMainMenu( );

	public static final GuiOptions	optionsMenu	= new GuiOptions( );

	public static final World		world		= new World("test", (byte) 32, (byte) 32);
}
