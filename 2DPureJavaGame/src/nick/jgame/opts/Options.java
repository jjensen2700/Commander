package nick.jgame.opts;

import java.io.File;
import java.util.*;

import nick.jgame.util.GameUtil;
import nick.jgame.util.debug.GameLog;

public final class Options {

	private static final HashMap<String, Boolean>	boolOpts	= new HashMap<>( );

	private static final ArrayList<String>			listOpts	= new ArrayList<>( );

	private static final HashMap<String, Float>		valueOpts	= new HashMap<>( );

	static {
		addBoolOption("adaptthreads", true);
		addBoolOption("bigdebug", false);
		addBoolOption("debugprinting", true);
		addBoolOption("quadbuff", false);
	}

	/**
	 * Use to add and set true-false game options. Appears in listOpts as "b_xxxx"
	 *
	 * @param name
	 *            The name of the option (lowercase and trimmed)
	 * @param defaultVal
	 *            The value to start with or set to
	 */
	public static final void addBoolOption(final String name, final boolean defaultVal) {

		final String toIn = name.toLowerCase( ).trim( );
		listOpts.add("b_" + toIn);
		boolOpts.put(toIn, defaultVal);
	}

	public static final void addValueOption(final String name, final float defaultVal) {

		final String toIn = name.toLowerCase( ).trim( );
		listOpts.add("v_" + toIn);
		valueOpts.put(toIn, defaultVal);
	}

	public static final boolean getBoolOption(final String name) {

		String toUse = name.toLowerCase( ).trim( );
		if (toUse.startsWith("v_")) {
			GameLog.warn("Wrong option type!");
			return false;
		}
		if (toUse.startsWith("b_")) {
			toUse = toUse.substring(2);
		}
		return boolOpts.get(toUse);
	}

	public static final int getNumOfBoolOpts( ) {

		return boolOpts.size( );
	}

	public static final int getNumOfOpts( ) {

		return getNumOfBoolOpts( ) + getNumOfValOpts( );
	}

	public static final int getNumOfValOpts( ) {

		return valueOpts.size( );
	}

	public static final float getValueOption(final String name) {

		String toUse = name.toLowerCase( ).trim( );
		if (toUse.startsWith("b_")) {
			GameLog.warn("Wrong option type!");
			return 0;
		}
		if (toUse.startsWith("v_")) {
			toUse = toUse.substring(2);
		}
		return valueOpts.get(name);
	}

	public static final void loadOptions(final File source) {

		final ArrayList<String> options = GameUtil.loadTxt(source);

		for (String opt : options) {
			final String[ ] parts = opt.split(":");
			if (parts.length != 2) {
				GameLog.warn("Incorrect format!");
				continue;
			}
			if (parts[0].toLowerCase( ).trim( ).startsWith("b_")) {
				final String inOpt = parts[0].trim( );
				final boolean val = Boolean.parseBoolean(parts[1]);
				addBoolOption(inOpt, val);
			}
			if (parts[0].toLowerCase( ).trim( ).startsWith("v_")) {
				final String inOpt = parts[0].trim( );
				final float val = Float.parseFloat(parts[1]);
				addValueOption(inOpt, val);
			}
		}
	}

	public static final void saveOptions(final File toSaveTo) {

		// Init
		GameLog.info("Saving Options...", false);
		final ArrayList<String> options = new ArrayList<>( );
		int boolDone = 0;
		int valDone = 0;
		// Header
		options.add("Game Options");
		options.ensureCapacity(getNumOfOpts( ) + 5);
		// Boolean Section
		options.add("True-False Options:");

		while (boolDone < getNumOfBoolOpts( )) {
			for (String s : listOpts) {
				if (s.startsWith("b_")) {
					options.add(s + ':' + getBoolOption(s));
				}
			}
		}
		// Value Section
		options.add("Value Options:");
		while (valDone < getNumOfValOpts( )) {
			for (String s : listOpts) {
				if (s.startsWith("v_")) {
					options.add(s + ':' + getValueOption(s));
				}
			}
		}

		GameUtil.writeTxt(toSaveTo, options);
		GameLog.info("Options Saved!", false);
	}
}
