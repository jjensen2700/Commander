package nick.jgame.entity;

import java.util.ArrayList;

import nick.jgame.world.World;
import nick.jgame.world.structures.WorldStruct;

public abstract class EntityOwner extends Entity {

	private float							money;

	private final ArrayList<WorldStruct>	owned	= new ArrayList<>( );

	public EntityOwner(final World w, final short xSpawn, final short ySpawn, final byte wide, final byte high) {

		super(w, xSpawn, ySpawn, wide, high);
		this.isStatic = true;
	}

	public final void addMoney(final float toAdd) {

		money += toAdd;
	}

	/**
	 * Use WorldStruct.setOwner() to set ownership.
	 * 
	 * @param struct
	 */
	public final void addToOwned(final WorldStruct struct) {

		this.owned.add(struct);
	}

	public final float getMoney( ) {

		return money;
	}

	public final ArrayList<WorldStruct> getOwned( ) {

		return owned;
	}

	public final void unown(final WorldStruct struct) {

		this.owned.remove(struct);
	}
}
