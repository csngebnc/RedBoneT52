package Visual;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import Core.Game;
import Map.Map;
import Map.Field;

public class View extends JFrame{
	
	private Graphics g;
	private Canvas cv;
	private Game game;
	
	public View(Game game) {
		this.game = game;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(1280,720));
		this.setVisible(true);
		cv = new Canvas();
		cv.setSize(1280,720);
		cv.setPreferredSize(cv.getSize());
		this.add(cv);
		cv.createBufferStrategy(2);
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				game.InputCame(e);
				
			}
		});
	}
	
	public void revalidate(Map map) {
		g = cv.getBufferStrategy().getDrawGraphics();
		g.drawImage(new ImageIcon("./assets/fields/backg.png").getImage(),0,0, null);
		for(Field f : map.getFields()) {
			f.draw(this);
		}
		cv.getBufferStrategy().show();
	}
	
	
	public void drawThing(int x, int y, Image img) {
		g.drawImage(img, x, y, null);
	}
	
	/**
	 * Sz�veg vastag�t�sa majd ki�r�sa egy j�gt�bla sark�ra
	 * 
	 * @param x mez� x koordin�t�ja
	 * @param y mez� y koordin�t�ja
	 * @param limit mez� maxplayers tulajdons�ga
	 * @author Norbi
	 */
	public void drawThing(int x, int y, String limit) {
		g.setFont(new Font("default", Font.BOLD, 11));
		g.drawString(limit, x, y);
	}

}