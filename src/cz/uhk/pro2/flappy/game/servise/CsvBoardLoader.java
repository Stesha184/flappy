package cz.uhk.pro2.flappy.game.servise;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage; 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.GatheringByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import cz.uhk.pro2.flappy.game.GameBoard;
import cz.uhk.pro2.flappy.game.Tile;
import cz.uhk.pro2.flappy.game.tiles.AbstractTile;
import cz.uhk.pro2.flappy.game.tiles.BonusTile;
import cz.uhk.pro2.flappy.game.tiles.EmptyTile;
import cz.uhk.pro2.flappy.game.tiles.WallTile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import cz.uhk.pro2.flappy.game.GameBoard;
import cz.uhk.pro2.flappy.game.Tile;
import cz.uhk.pro2.flappy.game.tiles.AbstractTile;

public class CsvBoardLoader implements BoardLoader {
		InputStream is;
		public CsvBoardLoader(InputStream is){
			this.is = is;
		}
	@Override
	public GameBoard loadLevel() {		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(is))){
			
			String[] line = br.readLine().split(";");
			int typeCount = Integer.parseInt(line[0]);
			Map<String,Tile> tileTypes = new HashMap<>();
			BufferedImage imageOfTheBird = null;
			for(int i =0; i< typeCount;i++){
				line = br.readLine().split(";");
				String type = line[0];
				String clazz = line[1];
				int positionX = Integer.parseInt(line[2]);
				int positionY = Integer.parseInt(line[3]);
				int width = Integer.parseInt(line[4]);
				int height = Integer.parseInt(line[5]);
				String url = line[6];
				String extraInfo =(line.length >= 8) ?  line[7]: "";
				Tile referencedTile = tileTypes.get(extraInfo);
				if(clazz.equals("Bird")){
					imageOfTheBird = getImage(positionX,positionY,width,height,url);
				}else{
					Tile tile = createTile(clazz,positionX,positionY,width,height, url,referencedTile);
				tileTypes.put(type, tile);
				}
				
				
			}
			line = br.readLine().split(";");
			int rows = Integer.parseInt(line[0]);
			int columns = Integer.parseInt(line[1]);
			// vytvorime pole dlazdic odpovidajicich rozmeru
			Tile[][] tiles = new Tile[rows][columns];
			System.out.println(rows + "," + columns);
			for(int i =0; i<rows;i++){
				line = br.readLine().split(";");
				for(int j =0;j<columns;j++){
					String cell;
					if(j<line.length){
						//bunka v csv existuje
					cell = line[j];
					}
					else
						cell = "";
					//ziskame odpovidajici typ dlazdice
						tiles[i][j] = tileTypes.get(cell);
				}
				
			}
			GameBoard gb = new GameBoard(tiles, imageOfTheBird);
			return gb;
			
		} catch (IOException e) {
			throw new RuntimeException("Chyba pøi ètení souboru",e);
			
		}
				
	}
	private Tile createTile(String clazz, int spriteX, int spriteY, 
			int spriteWidth, int spriteHeight, 
			String url, Tile referencedTile) throws IOException {
		BufferedImage resizedImage = getImage(spriteX, spriteY, spriteWidth, spriteHeight, url);
		// podle typu (clazz) vytvorime instanci patricne tridy
		switch (clazz) {
		case "Wall":
			return new WallTile(resizedImage);
		case "Bonus":
			return new BonusTile(resizedImage,referencedTile); // TODO dodelat dlazdici typu bonus
		case "Empty":
			return new EmptyTile(resizedImage);  
		default:
			throw new RuntimeException("Neznama trida dlazdice " + clazz);
		}
	}
	private BufferedImage getImage(int spriteX, int spriteY, int spriteWidth, int spriteHeight, String url)
			throws IOException, MalformedURLException {
		// nacist obrazek z URL
		BufferedImage originalImage = ImageIO.read(new URL(url));
		// vyriznout z obrazku sprite podle x,y, a sirka vyska
		BufferedImage croppedImage = originalImage.getSubimage(spriteX, spriteY, spriteWidth, spriteHeight);
		// zvetsime/zmensime sprite tak, aby pasoval do naseho rozmeru dlazdice
		BufferedImage resizedImage = new BufferedImage(Tile.SIZE, Tile.SIZE, BufferedImage.TYPE_INT_ARGB);
		// TODO nastavit parametry pro scaling
		Graphics2D g = (Graphics2D)resizedImage.getGraphics();
		g.drawImage(croppedImage, 0, 0, Tile.SIZE, Tile.SIZE, null);
		return resizedImage;
	}

}