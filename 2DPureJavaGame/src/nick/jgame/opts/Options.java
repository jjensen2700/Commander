package nick.jgame.opts;

import java.io.File;
import java.util.*;

import nick.jgame.Constants;
import nick.jgame.util.debug.GameLog;
import nick.jgame.util.io.FileUtil;

public final class Options {

	private static final HashMap<String, Boolean>	boolOpts	= new HashMap<>( );

	private static final ArrayList<String>			listOpts	= new ArrayList<>( );

	public static final File						optsFile	= new File(Constants.assetsLoc + "Options.txt");

	private static final HashMap<String, Float>		valueOpts	= new HashMap<>( );

	static {
		// Boolean Options
		addBoolOption("adaptthreads", true);
		addBoolOption("bigdebug", false);
		addBoolOption("debugprinting", true);
		addBoolOption("quadbuff", false);
		addBoolOption("debugmode", false);
		addBoolOption("debugrender", false);
		addBoolOption("hypersmoothgen", false);
		addBoolOption("runserver", true);
		addBoolOption("displayfps", true);
		// Float Options
		addValueOption("minfps", 30);
	}

	/**
	 * Use to add and set true-false game options. Appears in listOpts as "b_xxxx"
	 *
	 * @param name
	 *            The name of the option (lowercase and trimmed)
	 * @param val
	 *            The value to start with or set to
	 */
	public static void addBoolOption(final String name, final boolean val) {

		final String toIn = name.toLowerCase( ).trim( );
		if (!listOpts.contains("b_" + toIn)) {
			listOpts.add("b_" + toIn);
		}
		boolOpts.put(toIn, val);
		GameLog.info("Option " + toIn + " has been set to " + val, false);
	}

	public static void addValueOption(final String name, final float val) {

		final String toIn = name.toLowerCase( ).trim( );
		if (!listOpts.contains("v_" + toIn)) {
			listOpts.add("v_" + toIn);
		}
		valueOpts.put(toIn, val);
		GameLog.info("Option " + toIn + " has been set to " + val, false);
	}

	public static boolean getBoolOption(final String name) {

		String toUse = name.toLowerCase( ).trim( );
		if (toUse.startsWith("v_")) {
			GameLog.warn("Wrong option type!");
			return false;
		} else if (toUse.startsWith("b_")) {
			toUse = toUse.substring(2);
		}
		final Boolean toRet = boolOpts.get(toUse);
		if (toRet == null) {
			GameLog.warn("Option " + name + " has not been set!");
			return false;
		}
		return toRet;
	}

	public static int getNumOfBoolOpts( ) {

		return boolOpts.size( );
	}

	public static int getNumOfOpts( ) {

		return getNumOfBoolOpts( ) + getNumOfValOpts( );
	}

	public static int getNumOfValOpts( ) {

		return valueOpts.size( );
	}

	public static float getValueOption(final String name) {

		String toUse = name.toLowerCase( ).trim( );
		if (toUse.startsWith("b_")) {
			GameLog.warn("Wrong option type!");
			return 0;
		} else if (toUse.startsWith("v_")) {
			toUse = toUse.substring(2);
		}
		final Float toRet = valueOpts.get(name);
		if (toRet == null) {
			GameLog.warn("Option " + name + " has not been set!");
			return 0;
		}
		return toRet;
	}

	public static void loadOptions(final File source) {

		final ArrayList<String> options = FileUtil.loadTxt(source);

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

	public static void saveOptions(final File toSaveTo) {

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

		FileUtil.writeTxt(toSaveTo, options);
		GameLog.info("Options Saved!", false);
	}
}
