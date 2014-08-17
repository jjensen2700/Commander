package nick.jgame.world.structures;

import nick.jgame.entity.EntityOwner;
import nick.jgame.gfx.Renderable;
import nick.jgame.world.World;

public abstract class WorldStruct implements Renderable {

	protected String		name;

	protected EntityOwner	owner;

	protected final short	xLoc, yLoc;

	protected WorldStruct(final short x, final short y, final String name) {

		this.xLoc = x;
		this.yLoc = y;
		this.name = name;
	}

	public abstract boolean canBePlacedHere(World w, short tileX, short tileY);

	public final String getName( ) {

		return name;
	}

	public final EntityOwner getOwner( ) {

		return owner;
	}

	public final int getxLoc( ) {

		return xLoc;
	}

	public final int getyLoc( ) {

		return yLoc;
	}

	public final void setName(final String name) {

		this.name = name;
	}

	public final void setOwner(final EntityOwner owner) {

		this.owner = owner;
	}

	public abstract void update(World w);
}
