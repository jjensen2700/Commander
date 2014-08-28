package nick.jgame.net;

import java.util.ArrayList;

public abstract class Packet {

	public static enum Type {
		DISCONNECT, INVALID, LOGIN, MOVE, WORLD;

		private static final ArrayList<Type>	typesList	= new ArrayList<>( );
		static {
			typesList.add(0, LOGIN);
			typesList.add(1, DISCONNECT);
			typesList.add(2, MOVE);
			typesList.add(3, WORLD);

		}

		public static Type getType(final byte num) {

			return typesList.get(num);
		}

		public static Type getType(final String toGet) {

			byte num = Byte.parseByte(toGet);
			return getType(num);
		}

		public static byte getTypeNum(final Type t) {

			for (byte i = 0; i < typesList.size( ); i++) {
				if (typesList.get(i) == t) { return i; }
			}
			return -1;
		}
	}

	public abstract byte[ ] getData( );

	public abstract Type getType( );

	public final String readData(final byte[ ] data) {

		final String message = new String(data).trim( );
		return message.split("|")[1];
	}

	public abstract void writeDataToClient(Server s);

	public abstract void writeDataToServer(Client c);

}
