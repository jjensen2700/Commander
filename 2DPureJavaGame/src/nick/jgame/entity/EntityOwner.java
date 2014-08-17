package nick.jgame.entity;

import nick.jgame.world.World;

public abstract class EntityOwner extends Entity {

	private float	money;

	public EntityOwner(final World w, final short xSpawn, final short ySpawn, final byte wide, final byte high) {

		super(w, xSpawn, ySpawn, wide, high);
	}

	public void addMoney(final float toAdd) {

		money += toAdd;
	}

	public float getMoney( ) {

		return money;
	}
}
