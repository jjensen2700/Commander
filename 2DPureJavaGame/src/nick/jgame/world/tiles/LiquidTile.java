package nick.jgame.world.tiles;

import nick.jgame.gfx.Sprite;
import nick.jgame.init.*;
import nick.jgame.world.*;
import nick.jgame.world.util.WorldUtil;

public final class LiquidTile extends Tile {

	public LiquidTile(final Sprite s, final String name) {

		super(Materials.liquid, s, true, name);

	}

	@Override
	public void update(final World w, final short x, final short y) {

		if (WorldUtil.isTouchingAir(w, x, y)) {

			Tile[ ] around = WorldUtil.getTouching(w, x, y);
			int index = 0;

			for (byte i = 0; i < around.length; i++) {

				if (around[i] == Tiles.air) {
					break;
				}
				index++;
			}

			switch (index) {
				case 0:
					if (!w.isOutOfBounds(x, (short) (y - 1))) {
						w.setTile(this, x, (short) (y - 1));
					}
					break;
				case 1:
					if (!w.isOutOfBounds((short) (x + 1), y)) {
						w.setTile(this, (short) (x + 1), y);
					}
					break;
				case 2:
					if (!w.isOutOfBounds(x, (short) (y + 1))) {
						w.setTile(this, x, (short) (y + 1));
					}
					break;
				case 3:
					if (!w.isOutOfBounds((short) (x - 1), y)) {
						w.setTile(this, (short) (x - 1), y);
					}
					break;

			}

		}

	}

}
