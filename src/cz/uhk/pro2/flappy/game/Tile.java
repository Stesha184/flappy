package cz.uhk.pro2.flappy.game;

import java.awt.Graphics;

/**
 * spolecny interface pro vsechny druhy dlazdic na herci plose
 * 
 * @author pichkst1
 *
 */

public interface Tile {

	/**
	 * Sirzka a vyska dlazdice v pixeluch
	 */

	public static final int SIZE = 20;

	/**
	 * vykreslim dlazdici na platno g;
	 * 
	 * @param g
	 * param x je x-ova souradnice dlazdice ve viewportu v pixelech
	 * param y je y-ova souradnice dlazdice ve viewportu v pixelech
	 */

	void draw(Graphics g,int x,int y);
	
	

}
