package nick.jgame.entity;

import java.util.ArrayList;

import nick.jgame.gfx.*;
import nick.jgame.util.math.Vec2i;
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

	private Vec2i			loc;

	private String			name;

	private EnumEntityType	type;

	private byte			width;

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
		toRet.add("loc:" + loc.toString( ));
		return toRet;
	}

	public final short getTileXLoc( ) {

		return (short) (loc.getX( ) / 32);
	}

	public final short getTileYLoc( ) {

		return (short) (loc.getY( ) / 32);
	}

	public final EnumEntityType getType( ) {

		return type;
	}

	public final byte getWidth( ) {

		return width;
	}

	public final int getXLoc( ) {

		return loc.getX( );
	}

	public final int getYLoc( ) {

		return loc.getY( );
	}

	public final void move(final int x, final int y) {

		if (isStatic) { return; }
		loc.add(new Vec2i(x, y));

	}

	public void onCollide(final Tile t, final int xLoc, final int yLoc) {

	}

	@Override
	public void render(final Render rend) {

		if (img != null) {
			img.render(rend, (short) (loc.getX( ) + rend.getxOff( )), (short) (loc.getY( ) + rend.getyOff( )));
		}
	}

	public final void setLoc(final short x, final short y) {

		loc.set(x, y);
	}

	public void setName(final String name) {

		this.name = name;
	}

	protected final void setType(final EnumEntityType type) {

		this.type = type;
	}

	public final void setxLoc(final short x) {

		loc.setX(x);

	}

	public final void setyLoc(final short y) {

		loc.setY(y);

	}

	public void update( ) {

		if (home.isOutOfBounds(getTileXLoc( ), getTileYLoc( ))) {
			home.despawn(this);
		}
	}

}
