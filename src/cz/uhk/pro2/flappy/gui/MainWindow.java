package cz.uhk.pro2.flappy.gui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import cz.uhk.pro2.flappy.game.GameBoard;
import cz.uhk.pro2.flappy.game.servise.BoardLoader;
import cz.uhk.pro2.flappy.game.servise.CsvBoardLoader;

public class MainWindow extends JFrame {

	BoardPanel pnl = new BoardPanel();
	GameBoard gameBoard;
	long x = 0;

	class BoardPanel extends JPanel {
		@Override
		public void paint(Graphics g) {
			super.paint(g);

			gameBoard.drawAndDetectCollisions(g);
		}
	}

	public MainWindow() {
		try (InputStream is = new FileInputStream("level.csv")) {
			// vytvorime si loader
			BoardLoader loader = new CsvBoardLoader(is);
			gameBoard = loader.loadLevel();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			gameBoard = new GameBoard();
		} catch (IOException e1) {
			e1.printStackTrace();
			gameBoard = new GameBoard();
		}

		
		pnl.setPreferredSize(new Dimension(200, gameBoard.getHeightPix())); // TODO
		add(pnl, BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
        gameBoard.setWidthPix(pnl.getWidth());
        
		Timer t = new Timer(20, new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				gameBoard.tick(x++);
				pnl.repaint();
			}
		});
		//t.start();
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("Mys");
				if(e.getButton()==MouseEvent.BUTTON1){
						// zavolame metodu kickTheBird
				if(!t.isRunning()){
					t.start();
				}else{
					gameBoard.kickTheBird();
				}
					
				}else if (e.getButton()==MouseEvent.BUTTON3){
					gameBoard.reset();
					x=0;
					gameBoard.tick(0);
					pnl.repaint();
					t.stop();
				}
			
			}
		});
		
		// z balicku Java.Swing - z dùvodu kompatibility se swing oknem
		

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			MainWindow mainWindow = new MainWindow();

			mainWindow.setVisible(true);

		});
	}

}
