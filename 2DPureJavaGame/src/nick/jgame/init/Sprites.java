package nick.jgame.init;

import java.io.File;

import nick.jgame.Constants;
import nick.jgame.gfx.Sprite;

public final class Sprites {

	public static final String	allNormSpritePath		= Constants.assetsLoc + "textures" + File.separator;

	public static final String	allNormTileSpritePath	= allNormSpritePath + "tiles" + File.separator + "default";

	public static final Sprite	dirtBack				= new Sprite(new File(allNormTileSpritePath + "/dirt.png"), "def_dirtback");

	public static final Sprite	grass					= new Sprite(new File(allNormTileSpritePath + "/grass.png"), "def_grass");

	public static final Sprite	mountain				= new Sprite(new File(allNormTileSpritePath + "/mountain.png"), "def_mountain");

	public static final Sprite	normVillage				= new Sprite(new File(allNormSpritePath + "structs/village.png"), "normTown");

	public static final Sprite	water					= new Sprite(new File(allNormTileSpritePath + "/deep_water.png"), "def_deep_water");
}
