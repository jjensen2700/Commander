package nick.jgame.entity;

import nick.jgame.world.World;

public class EntityPlayer extends EntityOwner {

	public EntityPlayer(final World w, final short xSpawn, final short ySpawn, final byte wide, final byte high) {

		super(w, xSpawn, ySpawn, wide, high);
	}

}
