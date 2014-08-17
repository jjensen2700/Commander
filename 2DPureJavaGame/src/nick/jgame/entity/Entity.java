package nick.jgame.entity;

import nick.jgame.gfx.*;
import nick.jgame.world.*;

public abstract class Entity implements Renderable {

	private byte		height;

	protected World		home;

	protected Sprite	img;

	private boolean		inited;

	protected boolean	isStatic;

	private byte		width;

	private short		xLoc;

	private short		yLoc;

	public Entity(final short xSpawn, final short ySpawn, final byte wide, final byte high) {

		this.height = high;
		this.width = wide;
		setLoc(xSpawn, ySpawn);
	}

	public final short getTileXLoc( ) {

		return (short) (xLoc / 32);
	}

	public final short getTileYLoc( ) {

		return (short) (yLoc / 32);
	}

	public final int getXLoc( ) {

		return xLoc;
	}

	public final int getYLoc( ) {

		return yLoc;
	}

	public void init(final World w) {

		home = w;
		inited = true;
	}

	protected final boolean isColliding( ) {

		final short tX = getTileXLoc( );
		final short tY = getTileYLoc( );
		final int x = getXLoc( );
		final int y = getYLoc( );

		if (home.getTile((short) ((x - width) / 32), tY).getTileMat( ).isSolid( )) {
			int xIn = x - width;
			Tile t = home.getTile((short) (xIn / 32), tY);

			onCollide(t, xIn, y);
			t.onCollide(this, (short) (xIn / 32), tY);
			return true;
		}
		if (home.getTile((short) ((x + width) / 32), tY).getTileMat( ).isSolid( )) {
			int xIn = x + width;
			Tile t = home.getTile((short) (xIn / 32), tY);

			onCollide(t, xIn, y);
			t.onCollide(this, (short) (xIn / 32), tY);
			return true;
		}
		if (home.getTile(tX, (short) ((y - height) / 32)).getTileMat( ).isSolid( )) {
			int yIn = y - height;
			Tile t = home.getTile(tX, (short) (yIn / 32));

			onCollide(t, x, yIn);
			t.onCollide(this, tX, (short) (yIn / 32));
			return true;
		}
		if (home.getTile(tX, (short) ((y + height) / 32)).getTileMat( ).isSolid( )) {
			int yIn = y + height;
			Tile t = home.getTile(tX, (short) (yIn / 32));

			onCollide(t, x, yIn);
			t.onCollide(this, tX, (short) (yIn / 32));
			return true;
		}
		return false;
	}

	public boolean isInited( ) {

		return inited;
	}

	public final void move(final int x, final int y) {

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
