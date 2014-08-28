package nick.jgame.world;

import java.io.File;
import java.util.*;

import nick.jgame.*;
import nick.jgame.entity.Entity;
import nick.jgame.entity.owners.*;
import nick.jgame.gfx.Render;
import nick.jgame.gui.GuiWithThread;
import nick.jgame.init.Tiles;
import nick.jgame.net.packets.Packet02Move;
import nick.jgame.util.io.FileUtil;
import nick.jgame.util.math.Perlin;
import nick.jgame.world.structures.*;
import nick.jgame.world.util.*;

public final class World extends GuiWithThread {

	private Chunk[ ][ ]						chunks;

	private final ArrayList<Entity>			entities	= new ArrayList<>( );

	private boolean							firstUpdate	= true;

	private boolean							generated	= false, loadFromFile;

	private Random							rand;

	private File							saveLoc;

	private long							seed;

	private ChunkCoords						size;

	private final ArrayList<WorldStruct>	structs		= new ArrayList<>( );

	private byte							tenCounter	= 0;

	private float[ ][ ]						tileSet;

	private ChunkCoords						toUpdate	= new ChunkCoords((byte) 0, (byte) 0);

	private String							worldName;

	private World(final String name, final boolean fileLoad) {

		super("world_" + name);
		worldName = name;
		loadFromFile = fileLoad;

	}

	public World(final String name, final byte xLength, final byte yLength) {

		this(name, false);

		chunks = new Chunk[xLength][yLength];
		size = new ChunkCoords(xLength, yLength);
		fillChunks( );

		seed = WorldUtil.calcSeed(this);

		rand = new Random(seed);
		this.spawnIn(new EntityPlayer(this, "test"));
	}

	public World(final String name, final File loc) {

		this(name, true);
		saveLoc = loc;
		load( );
		this.spawnIn(new EntityPlayer(this, "test"));
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

		for (byte x = 0; x < size.getX( ); x++) {
			for (byte y = 0; y < size.getY( ); y++) {
				chunks[x][y] = new Chunk(x, y);
			}
		}

	}

	private void fillStructList( ) {

		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				structs.add(new Town(this, (short) (x * 32), (short) (y * 32), "test"));
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

		tileSet = Perlin.getNoise(rand, getTileWidth( ), getTileHeight( ), (byte) 4);
		tileSet = Perlin.roundNoise(tileSet, (byte) 2);
		WorldUtil.parsePerlinToTiles(this, tileSet, true);

		fillStructList( );

		generated = true;

	}

	private Chunk getChunk(final byte chunkX, final byte chunkY) {

		if ((chunkX < 0)
				|| (chunkY < 0)
				|| (chunkX >= size.getX( ))
				|| (chunkY >= size.getY( ))) { return null; }
		return chunks[chunkX][chunkY];
	}

	private Chunk getChunk(final ChunkCoords loc) {

		return getChunk(loc.getX( ), loc.getY( ));
	}

	public int getNumOfStructs( ) {

		return this.structs.size( );
	}

	public Random getRand( ) {

		return rand;
	}

	public WorldStruct getStruct(final int index) {

		return this.structs.get(index);
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

		return Chunk.sideLength * size.getY( );
	}

	public int getTileWidth( ) {

		return Chunk.sideLength * size.getX( );
	}

	public String getWorldName( ) {

		return worldName;
	}

	@Override
	public void initGui( ) {

		generate( );

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
		final ArrayList<String> txt = FileUtil.loadTxt(saveLoc);

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

	public void moveEntity(final Packet02Move packet) {

		for (Entity e : this.entities) {
			if (e.getSaveTxt( ).equals(packet.getSaveTxt( ))) {
				e.setLoc(packet.getX( ), packet.getY( ));
			}
		}
	}

	public void removePlayerMP(final String username) {

		for (Entity e : this.entities) {
			if (e instanceof EntityPlayerMP) {
				EntityPlayerMP mp = (EntityPlayerMP) e;
				if (mp.getName( ).equals(username)) {
					despawn(mp);
				}
			}
		}
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
			if (t == null) {
				break;
			}
			t.render(rend);
		}
	}

	public void save( ) {

		if (!loadFromFile) {
			saveLoc = new File(Constants.assetsLoc + "worlds" + File.separator + seed + ".txt");
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
				text.add(t.getName( ) + ':' + x + ", " + y);
			}
		}
		text.add("entities:");
		for (Entity e : entities) {
			text.add(e.getSaveTxt( ));
		}
		text.add("structs:");
		for (WorldStruct ws : structs) {
			text.add(ws.getSaveText( ));
		}
		FileUtil.writeTxt(saveLoc, text);
	}

	private void setNextChunk( ) {

		toUpdate.setY((byte) (toUpdate.getY( ) + 1));
		if (toUpdate.getY( ) > 4) {
			toUpdate.setY((byte) 0);
			toUpdate.setX((byte) (toUpdate.getX( ) + 1));
		}
		if (toUpdate.getX( ) > 4) {
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

		}
	}

	@Override
	public void update( ) {

		if (MainGame.getCurrentGui( ) != this) { return; }
		if (tenCounter > 10) {
			tenCounter = 0;
		}
		if (!generated) {
			generate( );

		}
		if (firstUpdate) {

			for (byte x = 0; x < size.getX( ); x++) {
				for (byte y = 0; y < size.getY( ); y++) {
					getChunk(x, y).update(this);
				}
			}
			firstUpdate = false;
		}

		if ((tenCounter % 5) == 0) {

			getChunk(toUpdate).update(this);

			setNextChunk( );
		}

		for (Entity e : entities) {
			e.update( );

		}
		if ((tenCounter % 2) == 0) {
			for (WorldStruct t : structs) {
				if (t == null) {
					break;
				}
				t.update( );
			}
		}

		tenCounter++;
	}

}
