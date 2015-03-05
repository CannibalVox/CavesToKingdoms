package talonos.cavestokingdoms.items;

import java.util.HashSet;
import java.util.Set;

import talonos.cavestokingdoms.lib.DEFS;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exterminatorJeff.undergroundBiomes.api.UBAPIHook;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemWorldTainter extends Item
{
	private String name = "worldTainter";
	
	private Set<Block> toRandomlyReplace;
	
	public ItemWorldTainter() 
	{
		setUnlocalizedName(DEFS.MODID + "_" + name);
		GameRegistry.registerItem(this, name);
		setCreativeTab(CreativeTabs.tabMaterials);
		setTextureName(DEFS.MODID + ":" + name);
	}
	
	@Override
    public ItemStack onItemRightClick(ItemStack itemStack, World theWorld, EntityPlayer thePlayer)
    {
		if (toRandomlyReplace == null)
		{
			toRandomlyReplace = new HashSet<Block>();
	        toRandomlyReplace.add(Blocks.snow_layer);
	        toRandomlyReplace.add(GameRegistry.findBlock("Natura", "N Crops"));
		}
		if (!theWorld.isRemote)
		{
			int x = ((int)thePlayer.posX/16)*16;
			int z = ((int)thePlayer.posZ/16)*16;
			
			thePlayer.addChatMessage(new ChatComponentText("Tainting and UBifying world based on coords: "));
			thePlayer.addChatMessage(new ChatComponentText("  xSection: "+x+", zSection: "+z));
			
			for (int xLoc = x-80; xLoc<x+96; xLoc++)
			{
				for (int zLoc = z-80; zLoc<z+96; zLoc++)
				{
					//System.out.println("Here");
					Utils.setBiomeAt(theWorld, xLoc, zLoc, ThaumcraftWorldGenerator.biomeTaint);
					/*for (int yLoc = 254; yLoc > 1; yLoc--)
					{
						if (theWorld.getBlock(xLoc, yLoc, zLoc).equals(Blocks.sand)&&
						    !theWorld.getBlock(xLoc, yLoc+1, zLoc).equals(Blocks.air)&&
						    !theWorld.getBlock(xLoc, yLoc+1, zLoc).equals(Blocks.water)&&
						    !theWorld.getBlock(xLoc, yLoc+1, zLoc).equals(Blocks.flowing_water)&&
						    !theWorld.getBlock(xLoc, yLoc+1, zLoc).equals(Blocks.sand)&&
						    !theWorld.getBlock(xLoc, yLoc+1, zLoc).equals(Blocks.cactus)&&
						    !theWorld.getBlock(xLoc, yLoc+1, zLoc).equals(Blocks.deadbush)&&
						    !theWorld.getBlock(xLoc, yLoc+1, zLoc).equals(Blocks.glass)&&
						    !theWorld.getBlock(xLoc, yLoc+1, zLoc).equals(Blocks.sandstone)&&
						    !theWorld.getBlock(xLoc, yLoc+1, zLoc).equals(ConfigBlocks.blockTaintFibres))
						{
							theWorld.setBlock(xLoc, yLoc, zLoc, Blocks.stone);
						}
						if (theWorld.getBlock(xLoc, yLoc, zLoc).equals(Blocks.sandstone)&&
							theWorld.getBlock(xLoc, yLoc+1, zLoc).equals(Blocks.stone))
							{
								theWorld.setBlock(xLoc, yLoc, zLoc, Blocks.stone);
							}
					}*/
					
					for (int yLoc = 1; yLoc < 254; yLoc++)
					{
						if (theWorld.isSideSolid(xLoc, yLoc-1, zLoc, ForgeDirection.UP, false)&&theWorld.getBlock(xLoc, yLoc, zLoc)==Blocks.air)
						{
							theWorld.setBlock(xLoc, yLoc, zLoc, ConfigBlocks.blockTaintFibres);
						}
						if (toRandomlyReplace.contains(theWorld.getBlock(xLoc, yLoc, zLoc)))
						{
							if (theWorld.rand.nextBoolean())
							{
								theWorld.setBlock(xLoc, yLoc, zLoc, ConfigBlocks.blockTaintFibres);
							}
						}
					}
				}
			}
			
			thePlayer.addChatMessage(new ChatComponentText("  World should now be tainted."));
			
			for (int xLoc = x-80; xLoc<x+96; xLoc+=16)
			{
				for (int zLoc = z-80; zLoc<z+96; zLoc+=16)
				{
					UBAPIHook.ubAPIHook.ubOreTexturizer.redoOres(xLoc, zLoc, theWorld);    			
				}
			}
			
			thePlayer.addChatMessage(new ChatComponentText("  Ores should now be fixed."));
			
			theWorld.getChunkProvider().unloadQueuedChunks();

			IntCache.resetIntCache();
		}
        
        return itemStack;
    }
}