package nick.jgame.gfx;

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import nick.jgame.*;
import nick.jgame.util.debug.GameLog;

public final class Window extends JFrame {

	private static final ArrayList<Image>	icons				= new ArrayList<>( );

	private static final long				serialVersionUID	= 1L;
	static {
		final String iconLoc = Constants.assetsLoc + "textures" + File.separator;
		try {
			icons.add(ImageIO.read(new File(iconLoc + "new_icon_64x.png")));

		} catch (Exception e) {
			GameLog.warn(e);
		}
		try {
			icons.add(ImageIO.read(new File(iconLoc + "new_icon_32x.png")));

		} catch (Exception e) {
			GameLog.warn(e);
		}
		try {
			icons.add(ImageIO.read(new File(iconLoc + "new_icon_16x.png")));
		} catch (Exception e) {
			GameLog.warn(e);
		}
		try {
			icons.add(ImageIO.read(new File(iconLoc + "new_icon_8x.png")));
		} catch (Exception e) {
			GameLog.warn(e);
		}
	}

	public void setup( ) {

		setTitle(Constants.getDisplayName( ));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(MainGame.getInst( ));
		pack( );
		setLocationRelativeTo(null);
		setIconImages(icons);

	}
}
