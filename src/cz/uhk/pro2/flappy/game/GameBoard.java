package cz.uhk.pro2.flappy.game;

import java.awt.Graphics;
import java.awt.Image;

import javax.security.auth.callback.TextInputCallback;

import cz.uhk.pro2.flappy.game.tiles.AbstractTile;
import cz.uhk.pro2.flappy.game.tiles.WallTile;

public class GameBoard implements TickAware {

	Tile[][] tiles;
	int shiftX = 0;
	int viewportWidth = 200;   // TODO upravit
	int widthPix; // sirka hraci plochy v pixelech
	Bird bird;

	boolean gameOver;
	Image imageOfTheBird;
	
	public GameBoard() {
		

		tiles = new Tile[20][20]; // TODO vylepsit

			reset();	
		
	}
	public GameBoard ( Tile[][] tiles, Image imageOfTheBird){
		this.tiles = tiles;
		this.imageOfTheBird = imageOfTheBird;
		reset();		
	}
	
	public void setWidthPix(int widthPix) {
		this.widthPix = widthPix;
	}
	
	public int getHeightPix() {
		return tiles.length * Tile.SIZE;
	}
	
	public void kickTheBird() {
		bird.kick();
	}

	/**
	 * Kresli cely herni svet (zdi, bonusy, ptaka) na platno g
	 * 
	 * @param g
	 */
	public void drawAndDetectCollisions(Graphics g) {

		int minJ = shiftX / Tile.SIZE; // index prvního viditelného sloupce
		int maxJ = minJ + viewportWidth / Tile.SIZE + 2; // zajištìní zobrazení
															// všech vpravo

		for (int i = 0; i < tiles.length; i++) {
			for (int j = minJ; j < maxJ; j++) {
				
				// chceme, aby se svìt toèil dokola, takže j2 se pohybuje od 0 do poèet sloupcu pole -1
				int j2 = j %  tiles[i].length;
				
				
				Tile t = tiles[i][j2];

				if (t == null)
					continue;

				int screenX = j * Tile.SIZE - shiftX;
				int screenY = i * Tile.SIZE;

				t.draw(g, screenX, screenY);
				
				//otestujeme moznou kolizi dlazdice s ptakem
				if(t instanceof WallTile){
				         if (bird.collidesWithRectangle(screenX, screenY, Tile.SIZE, Tile.SIZE)){
						       gameOver = true;
					}
				}
			}
		}

		// kresli ptáka
		bird.draw(g);
		
	}

	@Override
	public void tick(long ticksSinceStart) {

		if(!gameOver){
		// s kazdym tikem posuneme hru o jeden pixel
		// tj. pocet ticku a pixelu posunu se rovnaji
		shiftX = (int) ticksSinceStart;

		// dame vedet jeste ptakovi, ze hodiny tickly
		bird.tick(ticksSinceStart);
		}
	}

	public void reset(){
		gameOver = false;
		bird = new Bird(100,100, imageOfTheBird);
		
	}
	
	

}
