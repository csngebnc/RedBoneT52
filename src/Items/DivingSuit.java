package Items;
import Player.Player;

public class DivingSuit extends Throwable
{
	//Dani
	public void use(Player p) 
	{
		System.out.println("DivingSuit.use");
		//Ha nincs a p Playeren DivingSuit, akkor tud felh�zni egyet.
		if(p.getdSuitOn()==false)
		{
			p.changeSuit(this);
			p.setdSuitOn(true);
			p.drainStamina();
		}
	}
}