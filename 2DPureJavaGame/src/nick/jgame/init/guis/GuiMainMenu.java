package nick.jgame.init.guis;

import nick.jgame.*;
import nick.jgame.gfx.Render;
import nick.jgame.gui.GuiWithThread;
import nick.jgame.gui.parts.Button;
import nick.jgame.init.Guis;
import nick.jgame.input.Bindings;

public final class GuiMainMenu extends GuiWithThread {

	private final int		blue	= 0x0000ff;

	private final int		buttonX	= Constants.getMidWidth( ) - (buttonWidth / 2);

	private final Button	exit;

	private final Button	options;

	private final Button	play;

	private final int		white	= 0xffffff;

	public GuiMainMenu( ) {

		super("mainMenu");
		exit = new Button(buttonX, 150, buttonWidth, buttonHeight, white, "Exit", blue, false);
		play = new Button(buttonX, 50, buttonWidth, buttonHeight, white, "Play", blue, false);
		options = new Button(buttonX, 100, buttonWidth, buttonHeight, white, "Options", blue, false);
	}

	@Override
	public void initGui( ) {

		finInit( );

	}

	@Override
	public void render(final Render rend) {

		// GuiComp Rendering
		exit.render(rend);
		play.render(rend);
		options.render(rend);
		// Text Rendering
		rend.renderTxt(Constants.gameName, white, (short) ((Constants.windowWidth / 2) - 150), (short) 30, false);
		rend.renderTxt("Version:" + GameVersion.getVersion( ), white, (short) 0, Constants.windowHeight, true);
	}

	@Override
	public void update( ) {

		if (play.isClicked( ) || Bindings.confirm.isPressed( )) {

			MainGame.getInst( ).gotoGui(Guis.world);

		}
		if (options.isClicked( )) {
			MainGame.getInst( ).gotoGui(Guis.optionsMenu);
		}
		if (exit.isClicked( ) || Bindings.exit.isPressed( )) {
			MainGame.stop(false, null);
		}
	}
}
