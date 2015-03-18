package talonos.cavestokingdoms;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import talonos.biomescanner.BedrockBrick;
import talonos.cavestokingdoms.blocks.AltarBlock;
import talonos.cavestokingdoms.blocks.CtKBlock;
import talonos.cavestokingdoms.blocks.SpiritStoneBlock;
import talonos.cavestokingdoms.items.ItemManual;
import cpw.mods.fml.common.registry.GameRegistry;

public class CtKItems 
{
    public static ItemManual manual;

	public static void init() 
	{
		manual = new ItemManual();
	}
}
