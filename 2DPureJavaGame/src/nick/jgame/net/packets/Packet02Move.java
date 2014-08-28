package nick.jgame.net.packets;

import nick.jgame.net.*;

public final class Packet02Move extends Packet {

	private boolean	isMoving;

	private int		movingDir	= 1;

	private String	name;

	private int		numSteps	= 0;

	private short	x;

	private short	y;

	public Packet02Move(final byte[ ] data) {

		super( );
		String[ ] dataArray = readData(data).split(";");
		this.name = dataArray[0];
		this.x = Short.parseShort(dataArray[1]);
		this.y = Short.parseShort(dataArray[2]);
		this.numSteps = Integer.parseInt(dataArray[3]);
		this.isMoving = Integer.parseInt(dataArray[4]) == 1;
		this.movingDir = Integer.parseInt(dataArray[5]);
	}

	public Packet02Move(final String name, final short x, final short y, final int numSteps, final boolean isMoving,
			final int movingDir) {

		super( );
		this.name = name;
		this.x = x;
		this.y = y;
		this.numSteps = numSteps;
		this.isMoving = isMoving;
		this.movingDir = movingDir;
	}

	@Override
	public byte[ ] getData( ) {

		return ("02|" + this.name + ";" + this.x + "," + this.y + ";" + this.numSteps + ";" + (isMoving ? 1 : 0)
				+ ";" + this.movingDir).getBytes( );

	}

	public int getMovingDir( ) {

		return movingDir;
	}

	public int getNumSteps( ) {

		return numSteps;
	}

	public String getSaveTxt( ) {

		return "loc:" + x + ", " + y;
	}

	@Override
	public Type getType( ) {

		return Type.MOVE;
	}

	public String getUsername( ) {

		return name;
	}

	public short getX( ) {

		return this.x;
	}

	public short getY( ) {

		return this.y;
	}

	public boolean isMoving( ) {

		return isMoving;
	}

	@Override
	public void writeDataToClient(final Server s) {

		s.sendDataToAllClients(getData( ));
	}

	@Override
	public void writeDataToServer(final Client c) {

		c.sendData(getData( ));
	}

}
