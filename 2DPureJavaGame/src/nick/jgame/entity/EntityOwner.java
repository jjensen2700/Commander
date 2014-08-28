package nick.jgame.entity;

import java.util.ArrayList;

import nick.jgame.world.World;
import nick.jgame.world.structures.WorldStruct;

public abstract class EntityOwner extends Entity {

	private float							money;

	private final String					name;

	private final ArrayList<WorldStruct>	owned	= new ArrayList<>( );

	public EntityOwner(final World w, final String name) {

		super(w, (short) 0, (short) 0, (byte) 0, (byte) 0);
		this.isStatic = true;
		this.name = name;
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

	public String getName( ) {

		return name;
	}
}
