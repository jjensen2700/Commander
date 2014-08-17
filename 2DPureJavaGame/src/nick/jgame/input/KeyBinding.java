package nick.jgame.input;

import java.util.*;

public final class KeyBinding {

	private static final HashMap<String, KeyBinding>	bindings	= new HashMap<>( );

	private static final ArrayList<String>				keys		= new ArrayList<>( );

	public static final boolean isDown(final KeyBinding action) {

		return Keyboard.isKeyDown(action.boundKey);
	}

	public static final void updateAll( ) {

		for (String s : keys) {
			bindings.get(s).update( );
		}
	}

	private String	action;

	private int		boundKey;

	private boolean	pressed;

	public KeyBinding(final int key, final String name) {

		boundKey = key;
		action = name;
		bindings.put(action, this);
	}

	public boolean bindingEqual(final KeyBinding binding) {

		return (binding.action == this.action) & (binding.boundKey == this.boundKey);
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof KeyBinding) {
			return bindingEqual((KeyBinding) obj);
		} else {
			return false;
		}
	}

	public String getName( ) {

		return action;
	}

	public boolean isPressed( ) {

		return pressed;
	}

	public void update( ) {

		pressed = KeyBinding.isDown(this);
	}

}
