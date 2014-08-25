package nick.jgame.world.tiles;

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

}
