package nick.jgame.world.structures;

import java.util.ArrayList;

import nick.jgame.entity.EntityOwner;
import nick.jgame.gfx.Renderable;
import nick.jgame.world.World;

public abstract class WorldStruct implements Renderable {

	public static enum Status {
		NORMAL, SHUTDOWN, UNDER_ATTACK;
	}

	private final World	home;

	private String		name;

	private EntityOwner	owner;

	private Status		status;

	private final short	xLoc, yLoc;

	protected WorldStruct(final World w, final short x, final short y, final String name) {

		this.xLoc = x;
		this.yLoc = y;
		this.name = name;
		this.home = w;
		setStatus(Status.NORMAL);
	}

	public abstract boolean canBePlacedHere(World w, short tileX, short tileY);

	public final World getHome( ) {

		return home;
	}

	public final String getName( ) {

		return name;
	}

	public final EntityOwner getOwner( ) {

		return owner;
	}

	public ArrayList<String> getSaveTxt( ) {

		ArrayList<String> toRet = new ArrayList<>( );
		toRet.add("loc:" + xLoc + "," + yLoc);
		return toRet;
	}

	public Status getStatus( ) {

		return status;
	}

	public final short getxLoc( ) {

		return xLoc;
	}

	public final short getyLoc( ) {

		return yLoc;
	}

	public final void setName(final String newName) {

		name = newName;
	}

	public final void setOwner(final EntityOwner newOwner) {

		owner.unown(this);
		owner = newOwner;
		owner.addToOwned(this);
	}

	public void setStatus(final Status status) {

		this.status = status;
	}

	public abstract void update( );
}
