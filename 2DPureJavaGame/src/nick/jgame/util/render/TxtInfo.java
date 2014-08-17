package nick.jgame.util.render;

public final class TxtInfo {

	private final int		col;

	private final String	txt;

	private boolean			useSmall;

	private final short		x, y;

	public TxtInfo(final String toRend, final int color, final short x, final short y, final boolean small) {

		this.txt = toRend;
		col = color;
		this.x = x;
		this.y = y;
		this.useSmall = small;
	}

	public final int getColor( ) {

		return col;
	}

	public final String getTxt( ) {

		return txt;
	}

	public final short getX( ) {

		return x;
	}

	public final short getY( ) {

		return y;
	}

	public boolean useSmallFont( ) {

		return useSmall;
	}

}
