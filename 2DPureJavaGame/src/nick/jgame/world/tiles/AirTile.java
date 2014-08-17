package nick.jgame.world.tiles;

import nick.jgame.gfx.Render;
import nick.jgame.init.Materials;
import nick.jgame.world.Tile;

public final class AirTile extends Tile {

	private static final AirTile	instance	= new AirTile( );

	public static final Tile getInstance( ) {

		return instance;
	}

	private AirTile( ) {

		super(Materials.air, null, false, "air");

	}

	@Override
	public boolean isVisible(final Render rend, final short x, final short y) {

		return false;
	}

}
