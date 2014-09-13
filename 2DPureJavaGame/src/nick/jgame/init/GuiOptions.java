package nick.jgame.init;

import nick.jgame.*;
import nick.jgame.gfx.Render;
import nick.jgame.gui.GuiWithThread;
import nick.jgame.gui.parts.Button;
import nick.jgame.input.*;
import nick.jgame.opts.Options;

public final class GuiOptions extends GuiWithThread {

	private Button			adaptThreads;

	private final int		blue	= 0xff;

	private final Button	exit;

	private boolean			wasChanged;

	private final int		white	= 0xffffff;

	public GuiOptions( ) {

		super("optMenu");
		exit = new Button(Constants.getMidWidth( ) - (buttonWidth / 2), Constants.windowHeight - 35, buttonWidth, buttonHeight, white, "Exit", blue, false);
		adaptThreads = new Button(15, 100, buttonWidth + 75, buttonHeight, white, "Adapt Threads:", blue, false);
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

		if (this.wasChanged) {
			rend.dumpBuffs( );
			rend.dumpQueuedTxt( );
			wasChanged = false;
		}
		final String title = "Settings";
		exit.render(rend);
		adaptThreads.render(rend);

		rend.renderTxt(title, white, Constants.getCenter(title, rend, Render.bigFont), (short) 30, false);
		rend.renderTxt("Version:" + GameVersion.getVersion( ), white, (short) 0, Constants.windowHeight, true);
		rend.renderTxt(String.valueOf(Options.getBoolOption("adaptthreads")), white, (short) 300, (short) 120, true);
	}

	@Override
	public void update( ) {

		if (MainGame.getCurrentGui( ) != this) { return; }
		if (exit.isClicked( ) || KeyBinding.isDown(Bindings.exit)) {

			MainGame.gotoGui(Guis.mainMenu);
		} else if (adaptThreads.isClicked( )) {
			boolean toDo = Options.getBoolOption("adaptThreads");
			Options.addBoolOption("adaptthreads", !toDo);
			wasChanged = true;
		}
	}

}
