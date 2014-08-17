package nick.jgame.world;

import java.io.File;
import java.util.*;

import nick.jgame.*;
import nick.jgame.entity.Entity;
import nick.jgame.gfx.Render;
import nick.jgame.gui.GuiWithThread;
import nick.jgame.init.*;
import nick.jgame.input.*;
import nick.jgame.util.GameUtil;
import nick.jgame.util.math.Perlin;
import nick.jgame.world.structures.*;
import nick.jgame.world.util.*;

public final class World extends GuiWithThread {

	private Chunk[ ][ ]						chunks;

	private final ArrayList<Entity>			entities	= new ArrayList<>( );

	private boolean							generated	= false, loadFromFile;

	private Random							rand;

	private File							saveLoc;

	private long							seed;

	private final ArrayList<WorldStruct>	structs		= new ArrayList<>( );

	private ChunkCoords						toUpdate	= new ChunkCoords((byte) 0, (byte) 0);

	private String							worldName;

	private World(final String name, final boolean fileLoad) {

		super("world_" + name);
		worldName = name;
		loadFromFile = fileLoad;

	}

	public World(final String name, final File loc) {

		this(name, true);
		saveLoc = loc;
		load( );

	}

	public World(final String name, final short xLength, final short yLength) {

		this(name, false);

		chunks = new Chunk[5][5];
		fillChunks( );

		seed = WorldUtil.calcSeed(this);

		rand = new Random(seed);

	}

	@Override
	public void close(final String guiName) {

		super.close(guiName);
		save( );
	}

	public void despawn(final Entity e) {

		entities.remove(e);
	}

	private void fillChunks( ) {

		for (byte x = 0; x < chunks.length; x++) {
			for (byte y = 0; y < chunks[0].length; y++) {
				chunks[x][y] = new Chunk(this, x, y);
			}
		}

	}

	private void fillStructList( ) {

		for (int x = 0; x < chunks.length; x++) {
			for (int y = 0; y < chunks[0].length; y++) {
				this.structs.add(new Town(this, (short) (x * 32), (short) (y * 32), "test"));
			}
		}

	}

	public void generate( ) {

		if (generated) {
			System.err.println("World " + worldName + " is already generated!");
			return;
		}
		if (loadFromFile) {
			System.err.println("World " + worldName + " loads from a file!");
			return;
		}

		float[ ][ ] tileSet = Perlin.getNoise(rand, getTileWidth( ), getTileHeight( ), (byte) 4);
		WorldUtil.parsePerlinToTiles(this, tileSet, true);

		fillStructList( );

		generated = true;

	}

	private Chunk getChunk(final byte chunkX, final byte chunkY) {

		if ((chunkX < 0)
				|| (chunkY < 0)
				|| (chunkX >= chunks.length)
				|| (chunkY >= chunks[0].length)) { return null; }
		return chunks[chunkX][chunkY];
	}

	private Chunk getChunk(final ChunkCoords loc) {

		return getChunk(loc.getX( ), loc.getY( ));
	}

	public Random getRand( ) {

		return rand;
	}

	public Tile getTile(final short x, final short y) {

		if (isOutOfBounds(x, y)) { return Tiles.air; }

		Tile toRet = getChunk((byte) (x / 32), (byte) (y / 32)).getTile((short) (x % 32), (short) (y % 32));
		if (toRet == null) {
			setTile(Tiles.air, x, y);
			return Tiles.air;
		}
		return toRet;
	}

	public int getTileHeight( ) {

		return Chunk.sideLength * chunks[0].length;
	}

	public int getTileWidth( ) {

		return Chunk.sideLength * chunks.length;
	}

	public String getWorldName( ) {

		return worldName;
	}

	@Override
	public void initGui( ) {

		generate( );

		update( );

		finInit( );
	}

	public boolean isOutOfBounds(final short x, final short y) {

		return ((x < 0) || (y < 0) || (x >= getTileWidth( )) || (y >= getTileHeight( )));
	}

	public void load( ) {

		if (generated) {
			System.err.println("World " + worldName + " is already generated!");
			return;
		}
		if (!loadFromFile) {
			System.err.println("World " + worldName + " does not load from a file!");
			return;
		}
		final ArrayList<String> txt = GameUtil.loadTxt(saveLoc);

		for (String now : txt) {
			String[ ] comp1 = now.split(":");

			if (comp1[0].equals("seed")) {
				seed = Long.parseLong(comp1[1]);
				rand = new Random(seed);
				continue;
			}

			// Things that need coordinates
			String[ ] coords = comp1[1].split(",");
			if (comp1[0].equals("size")) {
				byte x = Byte.parseByte(coords[0]);
				byte y = Byte.parseByte(coords[1]);
				chunks = new Chunk[x][y];
				continue;
			}
			for (short i = 0; i < Tile.getTilesInited( ); i++) {
				if (comp1[0].equals(Tile.getAt(i))) {

					short x = Short.parseShort(coords[0]);
					short y = Short.parseShort(coords[1]);
					setTile(Tile.getAt(i), x, y);
				}

			}
		}

		generated = true;
	}

	public void removeStruct(final WorldStruct struct) {

		structs.remove(struct);

	}

	@Override
	public void render(final Render rend) {

		// Offsets
		short xOff = rend.getxOff( );
		short yOff = rend.getyOff( );
		// Corner Pins
		short tileXOff = (short) ((xOff / 32) - 1);
		short tileYOff = (short) ((yOff / 32) - 1);
		short tileXEnd = (short) (((rend.getWidth( ) + Math.abs(xOff)) / 32) + 1);
		short tileYEnd = (short) (((rend.getHeight( ) + Math.abs(yOff)) / 32) + 1);
		// Tile Counters
		short tileX = 0;
		short tileY = 0;

		for (short x = tileXOff; x < tileXEnd; x++)
		{
			for (short y = tileYOff; y < tileYEnd; y++)
			{
				Tile toRend = getTile(tileX, tileY);

				short rendAtX = (short) (xOff + (tileX * 32));
				short rendAtY = (short) (yOff + (tileY * 32));
				if (toRend.isVisible(rend, rendAtX, rendAtY)) {
					toRend.render(rend, rendAtX, rendAtY);
				}
				tileY++;
			}
			tileX++;
			tileY = 0;
		}
		for (Entity e : entities) {
			e.render(rend);
		}

		for (WorldStruct t : structs) {
			t.render(rend);
		}
	}

	public void save( ) {

		if (!loadFromFile) {
			saveLoc = new File("res/worlds/" + seed + ".txt");
		}
		final ArrayList<String> text = new ArrayList<>( );
		text.add("versionWrittenIn:" + GameVersion.getVersion( ));
		text.add("seed:" + seed);
		text.add("size:" + chunks.length + "," + chunks[0].length);
		text.add("----------------------------------------------------");
		text.add("worldData:");

		for (short x = 0; x < getTileWidth( ); x++) {
			for (short y = 0; y < getTileHeight( ); y++) {
				Tile t = getTile(x, y);
				text.add(t.getName( ) + ':' + x + ',' + y);
			}
		}

		GameUtil.writeTxt(saveLoc, text);
	}

	private void setNextChunk( ) {

		toUpdate.setY((byte) (toUpdate.getY( ) + 1));
		if (toUpdate.getY( ) >= chunks[0].length) {
			toUpdate.setY((byte) 0);
			toUpdate.setX((byte) (toUpdate.getX( ) + 1));
		}
		if (toUpdate.getX( ) >= chunks.length) {
			toUpdate.set((byte) 0);
		}

	}

	public void setTile(final Tile t, final short x, final short y) {

		if (isOutOfBounds(x, y)) {
			System.err.println(t.getName( ) + " can not be set!" + x + "," + y + " is not a valid world tile location!");
			return;
		}

		getChunk((byte) (x / 32), (byte) (y / 32)).setTile(t, (short) (x % 32), (short) (y % 32));
	}

	public void spawnIn(final Entity e) {

		if (!getTile(e.getTileXLoc( ), e.getTileYLoc( )).getTileMat( ).isSolid( ) && !isOutOfBounds(e.getTileXLoc( ), e.getTileYLoc( ))) {
			entities.add(e);
			e.init(this);
		}
	}

	@Override
	public void update( ) {

		if (!generated) {
			generate( );

		}

		getChunk(toUpdate).update(this);

		for (Entity e : entities) {
			e.update( );

		}

		for (WorldStruct t : structs) {
			t.update(this);
		}
		// KeyBindings
		KeyBinding.updateAll( );
		if (KeyBinding.isDown(Bindings.exit)) {

			MainGame.getInst( ).gotoGui(Guis.mainMenu);
		}

		if (KeyBinding.isDown(Bindings.moveUp)) {

			MainGame.getRend( ).moveOffsets((short) 0, (short) -1);

		}
		if (KeyBinding.isDown(Bindings.moveDown)) {

			MainGame.getRend( ).moveOffsets((short) 0, (short) 1);

		}

		if (KeyBinding.isDown(Bindings.moveLeft)) {

			MainGame.getRend( ).moveOffsets((short) -1, (short) 0);

		}
		if (KeyBinding.isDown(Bindings.moveRight)) {

			MainGame.getRend( ).moveOffsets((short) 1, (short) 0);

		}

		setNextChunk( );
	}

}
