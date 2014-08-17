package nick.jgame.init;

import java.io.File;

import nick.jgame.gfx.Sprite;

public final class Sprites {

	public static final String	allTileSprites	= "res/textures/tiles/";

	public static final Sprite	dirt			= new Sprite(new File(allTileSprites + "dirt.png"), "dirt");

	public static final Sprite	grass			= new Sprite(new File(allTileSprites + "grass.png"), "grass");

	public static final Sprite	stone			= new Sprite(new File(allTileSprites + "stone.png"), "stone");

	public static final Sprite	water			= new Sprite(new File(allTileSprites + "water.png"), "water");
}
