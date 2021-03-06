package Map;

import Player.Character;

/**
 * Az instabil mezo osztalya.
 * @author Csonge Bence
 */
public class UnstableField extends IceField
{
	/**
	 * Instabil mezo konstruktora, alapertelmezett ertek beallitasa.
	 * Jelenleg tesztek miatt alapertelmezetten nem tartalmaz targyat, paranccsal allithato be egy targy a mezore.
	 * @author Csonge Bence
	 */
	public UnstableField(int x, int y, int playerCount) {
		super(x, y, playerCount);
		maxplayers = (int)(Math.random()*(playerCount-1))+1;
	}
	
	/**
	 * Karakter �tv�tele instabil mez�re, ha t�bben vannak a mez�n mint a teherbir�sa, �tfordul a mez� �s meghal rajta mindenki
	 * @param c: atvett karakter
	 * @author Csonge Bence
	 */
	public void acceptCharacter(Character c){
		super.acceptCharacter(c);
		if(characters.size()>maxplayers)
			for(Character ci : characters)
				ci.alterHealth(-150);
	}
}