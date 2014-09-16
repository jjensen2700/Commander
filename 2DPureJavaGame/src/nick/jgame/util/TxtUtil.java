package nick.jgame.util;

import java.util.ArrayList;

public final class TxtUtil {

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

}
