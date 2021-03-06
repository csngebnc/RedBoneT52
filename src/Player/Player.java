package Player;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import Core.Game;
import Core.GameState;
import Items.DivingSuit;
import Items.Item;
import Map.Field;
import Visual.InventoryDialog;
import Visual.View;

/**
 * A j�t�kosokat reprezent�l� absztrakt oszt�ly.
 * Lesz�rmazottai az Eskimo �s a Scientist.
 * @author Zalan
 */
public abstract class Player extends Character{
	/**
	 * J�t�kos neve
	 * @author Csonge Bence
	 */
	protected String name;
	
	/**
	 * J�t�kos �letereje
	 * @author Csonge Bence
	 */
	protected int health;
	
	/**
	 * T�rolja, hogy a j�t�koson van-e b�v�rruha. True - igen, false - nem
	 * @author Csonge Bence
	 */
	protected boolean dSuitOn;
	
	/**
	 * J�t�kos h�tizs�kja, inventoryja. T�rolja az �sszes olyan t�rgyat, ami a j�t�kosn�l van.
	 * @author Csonge Bence
	 */
	protected ArrayList<Item> inventory;
	
	/**
	 * J�t�kos k�l�nleges k�pess�ge, lesz�rmazottank�nt elt�r� megval�s�t�ssal.
	 * @author Csonge Bence
	 */
	public abstract void doSkill();
	
	/**
	 * A megjelen�t�s sor�n a stamina �rt�k�t reprezent�l� k�pek.
	 * @author Csonge Bence
	 */
	protected Image staminaSprites[];
	
	/**
	 * A Player konstruktora.
	 * Inicializ�ljuk az inventory, isDrowning, dSuitOn, health tulajdons�gokat.
	 * @author Zalan
	 */
	public Player() {
		inventory = new ArrayList<Item>();
		isDrowning = false;
		dSuitOn = false;
		
		staminaSprites = new Image[3];
		staminaSprites[0] = new ImageIcon("./assets/HUD/stamina_1.png").getImage();
		staminaSprites[1] = new ImageIcon("./assets/HUD/stamina_2.png").getImage();
		staminaSprites[2] = new ImageIcon("./assets/HUD/stamina_3.png").getImage();
	}

	/**
	 * J�t�kos inventory-j�nak megjelen�t�se.
	 * @author Zalan
	 */
	protected void openInventory() {
		new InventoryDialog(this);
	}
	
	/**
	 * A j�t�kos k�re.
	 * A j�t�kos 3 stamin�val kezdi a k�r�t. Ha fuldoklik, �s nincs rajta b�v�rruha akkor meghalt, �s a j�t�kosok elvesztett�k a j�t�kot.
	 * Ezut�n, ha van m�g stamin�ja, akkor sz�nd�kainak megfelel�en cselekedhet.
	 * @author Zalan
	 */
	public void doTurn(KeyEvent e) {
		if(Game.getInstance().getState() != GameState.ONGOING)
			alterHealth(-150);

		if(isDrowning) {
			stamina = 0;
			Game.getInstance().nextCharacter();
			Game.notifyView();
			return;
		}
		
		move(e);
		Game.notifyView();
			
		if(isDrowning) {
			stamina = 0;
			Game.getInstance().nextCharacter();
			Game.notifyView();
			return;
		}
	}
	
	/**
	 * J�t�kos k�r�nek kezd�se, stamina �rt�k�nek be�ll�t�sa
	 * Amennyiben fullad �s nincs rajta b�v�rruha, �let�t cs�kkentj�k, hogy a j�t�k v�get �rjen.
	 * @author Csonge Bence
	 */
	public void startTurn() {
		stamina = 3;
		if(isDrowning && !dSuitOn)
			alterHealth(-health);
	}
	
	/**
	 * A n�zetre �rkezett billenty� lenyom�s feldolgoz�sa, annak megfelel�en t�rt�n� cselekv�s.
	 * @author Csonge Bence
	 */
	public void move(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_W:
				if(field.getNeighbour(Direction.UPPER_LEFT)!=null) 
					field.moveMeTo(this, Direction.UPPER_LEFT);
				break;
			case KeyEvent.VK_E:
				if(field.getNeighbour(Direction.UPPER_RIGHT)!=null) 
					field.moveMeTo(this, Direction.UPPER_RIGHT);
				break;
			case KeyEvent.VK_A:
				if(field.getNeighbour(Direction.LEFT)!=null) 
					field.moveMeTo(this, Direction.LEFT);
				break;
			case KeyEvent.VK_D:
				if(field.getNeighbour(Direction.RIGHT)!=null) 
					field.moveMeTo(this, Direction.RIGHT);
				break;
			case KeyEvent.VK_Y:
				if(field.getNeighbour(Direction.BOTTOM_LEFT)!=null) 
					field.moveMeTo(this, Direction.BOTTOM_LEFT);
				break;
			case KeyEvent.VK_X:
				if(field.getNeighbour(Direction.BOTTOM_RIGHT)!=null) 
					field.moveMeTo(this, Direction.BOTTOM_RIGHT);
				break;
			case KeyEvent.VK_S:
				doSkill();
				break;
			case KeyEvent.VK_Q:
				if(field.digSnow(1))
					drainStamina();
				break;
			case KeyEvent.VK_I:
				openInventory();
				break;
			case KeyEvent.VK_P:
				stamina = 1;
				drainStamina();
				return;
			case KeyEvent.VK_F:
				Item pickUpItem = field.pickUpItem(this);
				if(pickUpItem != null)
					inventory.add(pickUpItem);
				break;
			case KeyEvent.VK_G:
				field.removeItemFromIce(this);
				return;
			case KeyEvent.VK_C:
				if(changeSuit(null))
					drainStamina();
				return;
			default:
				break;
		}
	}
	
	/**
	 * B�v�rruha haszn�lat.
	 * Ha a j�t�koson volt m�r b�v�rruha, akkor azt leveszi �s az inventory-j�ba teszi, ha nem volt m�g rajta akkor felveszi.
	 * @author Zalan
	 * @param dsuit a a b�v�rruha amit felvesz�nk. Null �rt�k�, ha le kell venn�nk magunkr�l a b�v�rruh�t.
	 */
	public boolean changeSuit(DivingSuit dsuit) {
		if (dsuit == null) {
			if(dSuitOn) {
				dSuitOn = false;
				inventory.add(new DivingSuit());
				return true;
			}
			else
				return false;
		}
		else if (!dSuitOn) {
			dSuitOn = true;
			inventory.remove(dsuit);
			return true;
		}
		else
			return false;
	}
	
	/**
	 * J�t�kos �let�nek v�ltoztat�sa 'n' egys�ggel
	 * @author Zalan
	 * @param n ennyivel v�ltozik a j�t�kos �lete
	 */
	public void alterHealth(int n) {
		health += n;
		if(health <= 0)
			Game.loseGame();
	}
	
	/**
	 * A j�t�kos a param�terk�nt kapott karaktert �ssze�tk�zteti saj�t mag�val.
	 * @param c a karakter akivel �ssze�tk�ztetj�k magunkat
	 * @author Zalan
	 */
	public void collideWith(Character c) {
		c.hitBy(this);
	}

	/**
	 * Ha a j�t�kos jegesmedv�vel �tk�z�tt, akkor a j�t�kot elvesz�tett�k a j�t�kosok.
	 * @param pb a jegesmedve amivel a j�t�kos �ssze�tk�z�tt
	 * @author Zalan
	 */
	@Override
	public void hitBy(PolarBear pb) {
		health = 0;
		Game.loseGame();
	}
	
	/**
	 * J�t�kos stamin�j�nak cs�kkent�se 1-gyel.
	 * @author Zalan
	 */
	public void drainStamina() {
		stamina -= 1;
		if(stamina == 0) 
			Game.getInstance().nextCharacter();
	}
	
	/**
	 * A j�t�kos megment�sre ker�l fuldokl� helyzetb�l.
	 * A j�t�kos a param�terk�nt kapott field-re helyez�dik, �s az isDrowning �rt�ke false lesz.
	 * @author Zalan
	 * @param safeField a biztons�gos mez�, amire a j�t�kos ker�l.
	 */
	public boolean save(Field safeField) {
		field.moveMeTo(this, Direction.FromInt(field.getNeighbours().indexOf(safeField)));
		return true;
	}
	
	/**
	 * Egy t�rgy elt�vol�t�sa a j�t�kos inventory-j�b�l.
	 * @param i ezen a sorsz�m� Item-et t�vol�tjuk el az inventory-b�l
	 * @author Zalan
	 */
	public void removeItem(Item i) {
		inventory.remove(i);
	}
	
	/**
	 * A j�t�kos kirajzol�sa a param�terk�nt kapott megjelen�t�re a megfelel� avat�rral.
	 * @author Csonge Bence
	 */
	@Override
	public void draw(View v) {
		Image sprite = getAvatar();
		
		//Pozici� meghat�roz�sa
		int charPos = (52/field.getCharacters().size()) * 
				(int)Math.pow(-1, field.getCharacters().indexOf(this)) *
				(int)(Math.ceil(((double)field.getCharacters().indexOf(this))/2));
		
		//Player modell rajzol�s
		if(!field.hasBuilding()) {
			if(isDrowning) 
				v.drawThing(field.GetX()+40+charPos, field.GetY()+8, sprite);
			else 
				v.drawThing(field.GetX()+36+charPos, field.GetY(), sprite);
		}
		
		//Stamina rajzol�s
		if(stamina > 0) {
			if(field.hasBuilding())
				v.drawThing(field.GetX()+35, field.GetY()-30, staminaSprites[stamina-1]);
			else
				v.drawThing(field.GetX()+29+charPos, field.GetY()-15, staminaSprites[stamina-1]);
		}
	}
		
	/**
	 * J�t�kos mez�j�nek be�ll�t�sa.
	 * @param f a field �j �rt�ke
	 * @author Zalan
	 */
	public void setField(Field f) {
		field = f;
	}
	
	/**
	 * J�t�kos mez�j�nek lek�rdez�se.
	 * @author Zalan
	 */
	public Field getField() {
		return field;
	}
	
	/**
	 * A j�t�kos aktu�lis �llapot�nak megfelel�en visszaadja hozz� tartoz� k�pet.
	 * @author Csonge Bence
	 */
	public Image getAvatar() {
		if(isDrowning) {
			if(dSuitOn)
				return sprites[3];
			else
				return sprites[1];
		}
		else {
			if(dSuitOn) 
				return sprites[2];
			else
				return sprites[0];
		}
	}
	
	/**
	 * J�t�kos nev�nek lek�rdez�se.
	 * @author Zalan
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * J�t�kos nev�nek be�ll�t�sa.
	 * @param n az �j n�v
	 * @author Zalan
	 */
	public void setName(String n) {
		name = n;
	}
	
	/**
	 * A met�dus visszaadja, hogy van-e a j�t�koson b�v�rruha.
	 * @author Zalan
	 */
	public boolean getdSuitOn(){
		return dSuitOn;
	}
	
	/**
	 * A met�dus be�ll�tja, hogy van-e a j�t�koson b�v�rruha.
	 * @param b a dSuitOn �j �rt�ke
	 * @author Zalan
	 */
	public void setdSuitOn(boolean b){
		dSuitOn=b;
	}

	/**
	 * A met�dus visszaadja a j�t�kos inventory-j�nak i-edik elem�t.
	 * @param i ezen sorsz�m� elemet adjuk vissza
	 * @author Zalan
	 */
	public Item getItem(int i) {
		if(inventory.size() == 0 || i >= inventory.size())
			return null;
		return inventory.get(i);
	}
	
	/**
	 * Visszaadja a j�t�kos �leterej�nek �rt�k�t.
	 * @author Csonge Bence
	 */
	public int getHealth() {
		return health;
	}
	
	/**
	 * A met�dus visszat�r a j�t�kos inventory-j�val.
	 * @author Zalan
	 */
	public ArrayList<Item> getInventory(){
		return inventory;
	}
	
	/**
	 * A met�dus visszat�r a j�t�kos stamin�j�val.
	 * @author Zalan
	 */
	public int getStamina(){
		return stamina;
	}
}