package nick.jgame.gui;

import nick.jgame.gfx.Renderable;

public abstract class GuiHolder implements Renderable {

	protected static final byte		buttonHeight	= 34;

	protected static final short	buttonWidth		= (32 * 6) + 4;

	private boolean					doneInit		= false;

	private final String			name;

	protected GuiHolder(final String guiName) {

		name = guiName;
	}

	public abstract void close(String guiName);

	protected final void finInit( ) {

		doneInit = true;
	}

	public final String getName( ) {

		return name;
	}

	public abstract boolean hasOwnThread( );

	public abstract void initGui( );

	public final boolean isInit( ) {

		return doneInit;
	}

	public abstract void open(String guiName);

	public abstract void update( );

}
