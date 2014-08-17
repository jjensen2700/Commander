package nick.jgame.world.util;

import java.util.HashMap;

public final class PerlinHeights {

	private static final HashMap<String, Float>	perlinHeights	= new HashMap<>( );
	static {
		PerlinHeights.setSeaLevel((float) .395);
		PerlinHeights.setBareRockStart((float) .575);
		PerlinHeights.setArableZone((float) .5);
	}

	public static float getArableZone( ) {

		return perlinHeights.get("arablezone");
	}

	public static float getBareRockStart( ) {

		return perlinHeights.get("barerockstart");
	}

	public static float getSeaLevel( ) {

		return perlinHeights.get("sealvl");
	}

	public static void setArableZone(final float f) {

		perlinHeights.put("arablezone", f);

	}

	public static void setBareRockStart(final float f) {

		perlinHeights.put("barerockstart", f);

	}

	public static void setSeaLevel(final float f) {

		perlinHeights.put("sealvl", f);

	}

}
