package nick.jgame.entity;


public abstract class EntityOwner extends Entity {

	private float	money;

	public EntityOwner(final short xSpawn, final short ySpawn, final byte wide, final byte high) {

		super(xSpawn, ySpawn, wide, high);
	}

	public void addMoney(final float toAdd) {

		money += toAdd;
	}

	public float getMoney( ) {

		return money;
	}
}
