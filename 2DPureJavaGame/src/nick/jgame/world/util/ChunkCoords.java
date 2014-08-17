package nick.jgame.world.util;

public final class ChunkCoords {

	private byte	x;

	private byte	y;

	public ChunkCoords(final byte x, final byte y) {

		this.setX(x);
		this.setY(y);
	}

	public byte getX( ) {

		return x;
	}

	public byte getY( ) {

		return y;
	}

	public void set(final byte both) {

		setX(both);
		setY(both);

	}

	public void setX(final byte x) {

		this.x = x;
	}

	public void setY(final byte y) {

		this.y = y;
	}
}