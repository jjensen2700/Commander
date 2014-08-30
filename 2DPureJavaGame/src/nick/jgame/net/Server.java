package nick.jgame.net;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

import nick.jgame.*;
import nick.jgame.entity.owners.EntityPlayerMP;
import nick.jgame.init.*;
import nick.jgame.net.packets.*;
import nick.jgame.util.debug.GameLog;
import nick.jgame.world.Tile;

public final class Server extends Thread {

	private ArrayList<EntityPlayerMP>	connectedPlayers	= new ArrayList<>( );

	private boolean						running;

	private DatagramSocket				socket;

	public Server(final short port) {

		try {
			this.socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace( );
		}
	}

	public void addConnection(final EntityPlayerMP player, Packet00Login packet) {

		boolean alreadyConnected = false;
		for (EntityPlayerMP p : this.connectedPlayers) {
			if (player.getName( ).equalsIgnoreCase(p.getName( ))) {
				if (p.getIp( ) == null) {
					p.setIp(player.getIp( ));
				}
				if (p.getPort( ) == -1) {
					p.setPort(player.getPort( ));
				}
				alreadyConnected = true;
			} else {
				/*
				 * Relay to the current connected player that there is a new player
				 */
				sendData(packet.getData( ), p.getIp( ), p.getPort( ));

				/*
				 * Relay to the new player that the currently connect player exists
				 */
				packet = new Packet00Login(p.getName( ));
				sendData(packet.getData( ), player.getIp( ), player.getPort( ));
			}
		}
		if (!alreadyConnected) {
			this.connectedPlayers.add(player);
		}
	}

	public EntityPlayerMP getPlayerMP(final String username) {

		for (EntityPlayerMP player : this.connectedPlayers) {
			if (player.getName( ).equals(username)) { return player; }
		}
		return null;
	}

	public int getPlayerMPIndex(final String username) {

		int index = 0;
		for (EntityPlayerMP player : this.connectedPlayers) {
			if (player.getName( ).equals(username)) {
				break;
			}
			index++;
		}
		return index;
	}

	public final boolean isRunning( ) {

		return running;
	}

	private void parsePacket(final byte[ ] data, final Inet4Address address, final short port) {

		final String message = new String(data).trim( );
		final Packet.Type type = Packet.Type.getType(message.substring(0, 2));
		Packet packet = null;
		switch (type) {
			default:
			case INVALID:
				break;
			case LOGIN:
				packet = new Packet00Login(data);
				GameLog.info("[" + address.getHostAddress( ) + ":" + port + "] "
						+ ((Packet00Login) packet).getUsername( ) + " has connected...", false);
				EntityPlayerMP player = new EntityPlayerMP(Guis.world, ((Packet00Login) packet).getUsername( ), address, port);
				addConnection(player, (Packet00Login) packet);
				break;
			case DISCONNECT:
				packet = new Packet01Disconnect(data);
				System.out.println("[" + address.getHostAddress( ) + ":" + port + "] "
						+ ((Packet01Disconnect) packet).getUsername( ) + " has left...");
				removeConnection((Packet01Disconnect) packet);
				break;
			case MOVE:
				packet = new Packet02Move(data);
				Guis.world.moveEntity((Packet02Move) packet);
				break;
			case TILE:
				packet = new Packet03Tile(data);
				parseTilePacket((Packet03Tile) packet);
		}
	}

	private void parseTilePacket(final Packet03Tile p) {

		if ((p.getTile( ) != null) || (p.getTile( ) != Tiles.air)) {
			Tile t = Guis.world.getTile(p.getxLoc( ), p.getyLoc( ));
			new Packet03Tile(t, p.getxLoc( ), p.getyLoc( )).writeDataToClient(this);
		}

	}

	public void removeConnection(final Packet01Disconnect packet) {

		connectedPlayers.remove(getPlayerMPIndex(packet.getUsername( )));
		packet.writeDataToClient(this);
	}

	@Override
	public void run( ) {

		if (running) { return; }
		running = true;
		while (MainGame.isRunning( )) {
			final byte[ ] data = new byte[Constants.maxPacketLength];
			final DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (Exception e) {
				GameLog.warn(e);
			}
			parsePacket(packet.getData( ), (Inet4Address) packet.getAddress( ), (short) packet.getPort( ));
		}
		running = false;
	}

	public void sendData(final byte[ ] data, final InetAddress ipAddress, final int port) {

		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace( );
		}

	}

	public void sendDataToAllClients(final byte[ ] data) {

		for (EntityPlayerMP p : connectedPlayers) {
			sendData(data, p.getIp( ), p.getPort( ));
		}
	}
}
