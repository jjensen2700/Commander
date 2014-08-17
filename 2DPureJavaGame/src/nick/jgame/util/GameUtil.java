package nick.jgame.util;

import java.io.*;
import java.util.*;

import nick.jgame.util.debug.GameLog;

public final class GameUtil {

	public static ArrayList<String> loadTxt(final File loc) {

		if (!loc.exists( )) {
			System.err.println("File " + loc.getAbsolutePath( ) + " does not exist!");
			return new ArrayList<String>( );
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(loc));) {

			String line = null;
			ArrayList<String> contents = new ArrayList<String>( );

			while ((line = reader.readLine( )) != null) {
				contents.add(line);
			}
			contents.trimToSize( );
			return contents;
		} catch (Exception e) {

			GameLog.warn(e.getMessage( ));
		}
		return new ArrayList<String>( );
	}

	public static void writeTxt(final File loc, final ArrayList<String> txt) {

		txt.trimToSize( );
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(loc));) {
			writer.write("Written on " + Calendar.getInstance( ).getTime( ));
			for (String s : txt) {
				writer.newLine( );
				writer.write(s);
			}
		} catch (Exception e) {
			GameLog.warn(e.getMessage( ));
		}
	}
}
