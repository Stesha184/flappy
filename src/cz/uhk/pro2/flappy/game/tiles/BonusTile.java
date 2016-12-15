package cz.uhk.pro2.flappy.game.tiles;

import java.awt.Graphics;
import java.awt.Image;

import cz.uhk.pro2.flappy.game.Tile;

public class BonusTile extends AbstractTile {

	private Tile emptyTile;

	public BonusTile(Image image, Tile emptyTile) {
		super(image);
		this.emptyTile = emptyTile;
		
	}
	
	@Override
	public void draw(Graphics g,int x, int y){
		//emptyTile.draw(g,x,y);
		super.draw(g, x, y);
	}

}
  // esli zakontaktiruem s ptickoi to pozadi (empti) dodati boolean active