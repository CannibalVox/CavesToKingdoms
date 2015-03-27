package talonos.blightbuster;

import net.minecraft.item.Item;
import talonos.cavestokingdoms.items.ItemManual;

public class BBItems 
{
    public static Item purityFocus;
    public static Item silverPotion;
    public static Item worldTainter;
    public static Item worldSuperTainter;
    public static Item worldOreKiller;

	public static void init() 
	{
		purityFocus = new ItemPurityFocus();
		silverPotion = new ItemSilverPotion();
		worldTainter = new ItemWorldTainter();
		worldOreKiller = new ItemWorldOreKiller();
		worldSuperTainter = new ItemSuperTestWorldTainter();
	}
}

