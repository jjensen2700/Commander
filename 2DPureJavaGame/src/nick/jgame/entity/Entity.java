package nick.jgame.entity;

import java.util.ArrayList;

import nick.jgame.gfx.*;
import nick.jgame.world.*;

public class Entity implements Renderable {

	public static Entity formEntity(final World w, final EnumEntityType enumtype, final String nameParse,
			final short xParse, final short yParse) {

		return new Entity(w, enumtype, nameParse, xParse, yParse, (byte) 16, (byte) 16);
	}

	private byte			height;

	protected final World	home;

	protected Sprite		img;

	protected boolean		isStatic;

	private String			name;

	private EnumEntityType	type;

	private byte			width;

	private short			xLoc;

	private short			yLoc;

	public Entity(final World w, final EnumEntityType type, final String name, final short xSpawn, final short ySpawn,
			final byte wide,
			final byte high) {

		home = w;
		this.setType(type);
		this.setName(name);
		this.height = high;
		this.width = wide;
		setLoc(xSpawn, ySpawn);
	}

	public final byte getHeight( ) {

		return height;
	}

	public String getName( ) {

		return name;
	}

	public ArrayList<String> getSaveTxt( ) {

		ArrayList<String> toRet = new ArrayList<>( );
		toRet.add("new " + type.name( ));
		toRet.add("name:" + name);
		toRet.add("loc:" + xLoc + "," + yLoc);
		return toRet;
	}

	public final short getTileXLoc( ) {

		return (short) (xLoc / 32);
	}

	public final short getTileYLoc( ) {

		return (short) (yLoc / 32);
	}

	public final EnumEntityType getType( ) {

		return type;
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

	public void setName(final String name) {

		this.name = name;
	}

	protected final void setType(final EnumEntityType type) {

		this.type = type;
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
