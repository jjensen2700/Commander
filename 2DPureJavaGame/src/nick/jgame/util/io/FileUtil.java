package nick.jgame.util.io;

import java.io.*;
import java.util.*;

import nick.jgame.util.debug.GameLog;

public final class FileUtil {

	public static ArrayList<String> formatToSave(final ArrayList<String> text) {

		byte lvl = 0;

		final ArrayList<String> toRet = new ArrayList<>( );
		for (String s : text) {
			String tabs = "";
			if (lvl > 0) {
				for (byte tabLoop = 0; tabLoop < lvl; tabLoop++) {
					tabs += "\t";
				}
			}
			toRet.add(tabs + s);
			if (s.indexOf('{') != -1) {

				lvl++;
			} else if (s.indexOf('}') != -1) {

				lvl--;
			}
		}

		return toRet;
	}

	public static ArrayList<String> loadTxt(final File loc) {

		if (!loc.exists( )) {
			System.err.println("File " + loc.getAbsolutePath( ) + " does not exist!");
			return new ArrayList<String>( );
		}

		try (final BufferedReader reader = new BufferedReader(new FileReader(loc));) {

			String line = null;
			final ArrayList<String> contents = new ArrayList<String>( );

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
		try (final BufferedWriter writer = new BufferedWriter(new FileWriter(loc));) {
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
