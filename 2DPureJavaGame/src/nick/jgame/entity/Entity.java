package nick.jgame.entity;

import nick.jgame.gfx.*;
import nick.jgame.world.*;

public abstract class Entity implements Renderable {

	private byte			height;

	protected final World	home;

	protected Sprite		img;

	protected boolean		isStatic;

	private byte			width;

	private short			xLoc;

	private short			yLoc;

	public Entity(final World w, final short xSpawn, final short ySpawn, final byte wide, final byte high) {

		home = w;
		this.height = high;
		this.width = wide;
		setLoc(xSpawn, ySpawn);
	}

	public final byte getHeight( ) {

		return height;
	}

	public String getSaveText( ) {

		return "loc:" + xLoc + ", " + yLoc;
	}

	public final short getTileXLoc( ) {

		return (short) (xLoc / 32);
	}

	public final short getTileYLoc( ) {

		return (short) (yLoc / 32);
	}

	public final byte getWidth( ) {

		return width;
	}

	public final int getXLoc( ) {

		return xLoc;
	}

	public final int getYLoc( ) {

		return yLoc;
	}

	public final void move(final int x, final int y) {

		if (isStatic) { return; }
		xLoc += x;
		yLoc += y;

	}

	public void onCollide(final Tile t, final int xLoc, final int yLoc) {

	}

	@Override
	public void render(final Render rend) {

		if (img != null) {
			img.render(rend, (short) (xLoc + rend.getxOff( )), (short) (yLoc + rend.getyOff( )));
		}
	}

	public final void setLoc(final short x, final short y) {

		setxLoc(x);
		setyLoc(y);
	}

	public final void setxLoc(final short x) {

		xLoc = x;
	}

	public final void setyLoc(final short y) {

		yLoc = y;
	}

	public void update( ) {

		if (home.isOutOfBounds(getTileXLoc( ), getTileYLoc( ))) {
			home.despawn(this);
		}
	}

}
