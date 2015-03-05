package talonos.cavestokingdoms;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import talonos.cavestokingdoms.blocks.AltarBlock;
import talonos.cavestokingdoms.blocks.BedrockBrick;
import talonos.cavestokingdoms.blocks.CtKBlock;
import talonos.cavestokingdoms.blocks.SpiritStoneBlock;
import talonos.cavestokingdoms.items.ItemPurityFocus;
import talonos.cavestokingdoms.items.ItemSilverPotion;
import talonos.cavestokingdoms.items.ItemWorldOreKiller;
import talonos.cavestokingdoms.items.ItemWorldTainter;
import talonos.cavestokingdoms.items.ItemManual;
import cpw.mods.fml.common.registry.GameRegistry;

public class CtKItems 
{
    public static Item purityFocus;
    public static Item silverPotion;
    public static Item worldTainter;
    public static Item worldOreKiller;
    public static ItemManual manual;

	public static void init() 
	{
		purityFocus = new ItemPurityFocus();
		silverPotion = new ItemSilverPotion();
		worldTainter = new ItemWorldTainter();
		worldOreKiller = new ItemWorldOreKiller();
		manual = new ItemManual();
	}
}
