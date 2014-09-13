package nick.jgame.entity;

import java.util.ArrayList;

import nick.jgame.world.World;
import nick.jgame.world.structures.*;

public abstract class EntityOwner extends Entity {

	private float							money;

	private final String					name;

	private final ArrayList<WorldStruct>	owned	= new ArrayList<>( );

	private float							subjugHappy;

	public EntityOwner(final World w, final String name) {

		super(w, (short) 0, (short) 0, (byte) 0, (byte) 0);
		this.isStatic = true;
		this.name = name;
	}

	public final void addMoney(final float toAdd) {

		money += toAdd;
	}

	/**
	 * Use WorldStruct.setOwner() to set ownership.
	 *
	 * @param struct
	 */
	public final void addToOwned(final WorldStruct struct) {

		this.owned.add(struct);

	}

	private void calcAvgHappyness( ) {

		int overallPop = 0;
		int overallHappy = 0;
		for (WorldStruct s : owned) {
			if (!(s instanceof Town)) {
				continue;
			}
			Town t = (Town) s;
			overallHappy += t.getHappyCount( );
			overallPop += t.getPop( );

		}
		subjugHappy = (overallHappy / overallPop);

	}

	public final float getMoney( ) {

		return money;
	}

	public final String getName( ) {

		return name;
	}

	public final ArrayList<WorldStruct> getOwned( ) {

		return owned;
	}

	@Override
	public ArrayList<String> getSaveTxt( ) {

		ArrayList<String> toRet = super.getSaveTxt( );
		toRet.add("name:" + name);
		toRet.add("{");
		toRet.add("money:" + money);
		for (WorldStruct s : owned) {
			toRet.addAll(s.getSaveTxt( ));
		}
		toRet.add("}");
		return toRet;
	}

	public final float getSubjugHappy( ) {

		return subjugHappy;
	}

	public final void unown(final WorldStruct struct) {

		this.owned.remove(struct);
	}

	@Override
	public void update( ) {

		super.update( );
		calcAvgHappyness( );
	}
}
