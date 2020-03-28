package Items;

import Map.Field;

public abstract class Throwable extends Item 
{
	public boolean throwTo(Field f) 
	{
		System.out.println("Throwable.throwTo(Field f)");
		f.acceptItem(this);
		return true;
	}
	
	//Dominik
	public void pickUp() 
	{
		System.out.println("Throwable.pickUp()");
	}
}