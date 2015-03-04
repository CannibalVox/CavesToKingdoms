package talonos.cavestokingdoms;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import talonos.cavestokingdoms.blocks.CtKBlock;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.IWandTriggerManager;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.research.ResearchManager;

public class TalonosWandTriggerManager implements IWandTriggerManager 
{

	@Override
	public boolean performTrigger(World world, ItemStack wand,
			EntityPlayer player, int x, int y, int z, int side, int event) 
	{
		player.addChatMessage(new ChatComponentText("Testing Dawn Machine for Validity..."));
		switch (event) 
		{
		case 0:
			//if (ResearchManager.isResearchComplete(
			//		player.getCommandSenderName(), "DAWNMACHINE")) 
			{
				return createDawnMachine(wand, player, world, x, y, z);
			}
			//break;
		}
		return false;
	}

	private boolean createDawnMachine(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z) 
	{
		ItemWandCasting wand = (ItemWandCasting) stack.getItem();
		switch(fitDawnMachine(world, x, y, z))
		{
		case 0:
			return false;
		case 1:
			if (wand.consumeAllVisCrafting(stack, player, new AspectList().add(Aspect.ORDER, 20), true))
			{
				if (!world.isRemote)
				{
					replaceDawnMachineZ(player, world, x, y, z);
					return true;
				}
				return false;
			}
			return false;
		case 2:
			if (wand.consumeAllVisCrafting(stack, player, new AspectList().add(Aspect.ORDER, 20), true))
			{
				if (!world.isRemote)
				{
					replaceDawnMachineX(player, world, x, y, z);
					return true;
				}
				return false;
			}
			return false;
		default:
			return false;
		} 
	}

	private void replaceDawnMachineZ(EntityPlayer player, World world, int x, int y, int z) 
	{
		player.addChatMessage(new ChatComponentText("Valid Z Dawn Machine"));
	}
	
	private void replaceDawnMachineX(EntityPlayer player, World world, int x, int y, int z) 
	{
		player.addChatMessage(new ChatComponentText("Valid Z Dawn Machine"));
	}

	private int fitDawnMachine(World world, int x, int y, int z) 
	{
		if (world.getBlock(x, y, z).equals(CtKBlock.dawnTotem));
		{
			if (checkZAlignedDawnMachine(world, x, y, z))
			{
				return 1;
			}
			if (checkXAlignedDawnMachine(world, x, y, z))
			{
				return 2;
			}
		}
		return 0;
	}

	private boolean checkXAlignedDawnMachine(World world, int x, int y, int z) 
	{
		for (int yy = y-1; yy <= y+1; yy++)
		{
			if (!(world.getBlock(x, yy, z-1).equals(ConfigBlocks.blockMagicalLog)&&world.getBlockMetadata(x, yy, z-1)==1&&
			     world.getBlock(x, yy, z+1).equals(ConfigBlocks.blockMagicalLog)&&world.getBlockMetadata(x, yy, z+1)==1))
			{
				return false;
			}
		}
		if (!world.getBlock(x, y+1, z).equals(Blocks.air)||!world.getBlock(x, y-1, z).equals(Blocks.air))
		{
			return false;
		}
		return true;
	}

	private boolean checkZAlignedDawnMachine(World world, int x, int y, int z) 
	{
		for (int yy = y-1; yy <= y+1; yy++)
		{
			if (!(world.getBlock(x-1, yy, z).equals(ConfigBlocks.blockMagicalLog)&&world.getBlockMetadata(x-1, yy, z)==1&&
			     world.getBlock(x+1, yy, z).equals(ConfigBlocks.blockMagicalLog)&&world.getBlockMetadata(x+1, yy, z)==1))
			{
				return false;
			}
		}
		if (!world.getBlock(x, y+1, z).equals(Blocks.air)||!world.getBlock(x, y-1, z).equals(Blocks.air))
		{
			return false;
		}
		return true;
	}

}
