package nick.jgame.init;

import nick.jgame.init.guis.*;
import nick.jgame.world.World;

public final class Guis {

	public static final GuiMainMenu	mainMenu	= new GuiMainMenu( );

	public static final GuiOptions	optionsMenu	= new GuiOptions( );

	public static final World		world		= new World("main", (short) 64, (short) 64);
}