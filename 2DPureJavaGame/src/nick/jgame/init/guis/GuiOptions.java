package nick.jgame.init.guis;

import nick.jgame.*;
import nick.jgame.gfx.Render;
import nick.jgame.gui.GuiWithThread;
import nick.jgame.gui.parts.Button;
import nick.jgame.init.Guis;
import nick.jgame.input.Bindings;
import nick.jgame.opts.Options;

public final class GuiOptions extends GuiWithThread {

	private final int		blue	= 0xff;

	private final Button	exit;

	private final int		white	= 0xffffff;

	public GuiOptions( ) {

		super("optMenu");
		exit = new Button(Constants.getMidWidth( ) - (buttonWidth / 2), Constants.windowHeight - 35, buttonWidth, buttonHeight, white, "Exit", blue, false);

	}

	@Override
	public void close(final String name) {

		super.close(name);
		Options.saveOptions(Options.optsFile);
	}

	@Override
	public void initGui( ) {

		finInit( );

	}

	@Override
	public void render(final Render rend) {

		exit.render(rend);

		rend.renderTxt("Settings", white, (short) ((Constants.windowWidth / 2) - 130), (short) 30, false);
		rend.renderTxt("Version:" + GameVersion.getVersion( ), white, (short) 0, Constants.windowHeight, true);
	}

	@Override
	public void run( ) {

		super.run( );
	}

	@Override
	public void update( ) {

		if (exit.isClicked( ) || Bindings.exit.isPressed( )) {

			MainGame.getInst( ).gotoGui(Guis.mainMenu);
		}
	}

}
