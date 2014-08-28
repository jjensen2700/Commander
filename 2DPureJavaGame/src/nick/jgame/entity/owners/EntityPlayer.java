package nick.jgame.entity.owners;

import nick.jgame.MainGame;
import nick.jgame.entity.EntityOwner;
import nick.jgame.init.Guis;
import nick.jgame.input.*;
import nick.jgame.world.World;

public class EntityPlayer extends EntityOwner {

	public EntityPlayer(final World w, final String name) {

		super(w, name);
	}

	@Override
	public void update( ) {

		if (KeyBinding.isDown(Bindings.exit)) {

			MainGame.getInst( ).gotoGui(Guis.mainMenu);

		}
		byte speed = 1;
		if (KeyBinding.isDown(Bindings.speed)) {
			speed++;
		}

		if (KeyBinding.isDown(Bindings.moveUp)) {

			MainGame.getRend( ).moveOffsets((short) 0, (short) (-speed));

		}
		if (KeyBinding.isDown(Bindings.moveDown)) {

			MainGame.getRend( ).moveOffsets((short) 0, speed);

		}

		if (KeyBinding.isDown(Bindings.moveLeft)) {

			MainGame.getRend( ).moveOffsets((short) -speed, (short) 0);

		}
		if (KeyBinding.isDown(Bindings.moveRight)) {

			MainGame.getRend( ).moveOffsets(speed, (short) 0);

		}
	}
}
