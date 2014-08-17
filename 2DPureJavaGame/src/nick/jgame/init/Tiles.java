package nick.jgame.init;

import nick.jgame.world.Tile;
import nick.jgame.world.tiles.*;

public final class Tiles {

	public static final Tile	air		= AirTile.getInstance( );

	public static final Tile	dirt	= new Tile(Materials.dirt, Sprites.dirt, false, "dirt");

	public static final Tile	grass	= GrassTile.getInstance( );

	public static final Tile	stone	= new Tile(Materials.stone, Sprites.stone, false, "stone");

	public static final Tile	water	= new LiquidTile(Sprites.water, "water");
}
