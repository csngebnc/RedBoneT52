package Items;

import java.awt.Image;
import javax.swing.ImageIcon;
import Visual.ImgType;

/**
 * Pisztolycs�vet reprezent�l� oszt�ly
 * @author Norbi
 */
public class Barrel extends GunPart{
	
	/**
	 * Konstruktor, be�llitja a k�peket
	 * @author Balczer Dominik
	 */
	public Barrel() {
		sprites = new Image[3];
		sprites[ImgType.DROPPED.VALUE] = new ImageIcon("./assets/items_buildings/barrel.png").getImage();
		sprites[ImgType.FROZEN.VALUE] = new ImageIcon("./assets/items_buildings/barrel_frozen.png").getImage();
		sprites[ImgType.BUILT.VALUE] = new ImageIcon("./assets/items_buildings/barrel.png").getImage();
	}
	
	/**
	 * Visszaadja a t�rgy nev�t.
	 * @author Csonge Bence
	 */
	@Override
	public String getName() {
		return "Gun Barrel";
	}
}