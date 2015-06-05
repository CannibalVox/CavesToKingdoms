package talonos.blightbuster;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
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
		if (world.isRemote)
			return false;

		ItemWandCasting wand = (ItemWandCasting) stack.getItem();
		int orientation = fitDawnMachine(world, x, y, z);

		if (orientation == 0)
			return false;

		if (wand.consumeAllVisCrafting(stack, player, new AspectList().add(Aspect.ORDER, 20), true))
		{
			if (!world.isRemote)
			{
				replaceDawnMachine(player, world, x, y, z, orientation);
				return true;
			}
			return false;
		}
		return false;
	}

	private void replaceDawnMachine(EntityPlayer player, World world, int x, int y, int z, int orientation)
	{
		player.addChatMessage(new ChatComponentText("Valid Dawn Machine"));
	}

	private int fitDawnMachine(World world, int x, int y, int z) 
	{
		if (world.getBlock(x, y, z).equals(BBBlock.dawnTotem));
		{
			for (int orientation = 1; orientation <= 2; orientation++) {
				if (checkDawnMachine(world, x, y, z, orientation)) {
					return orientation;
				}
			}
		}
		return 0;
	}

	private boolean checkDawnMachine(World world, int x, int y, int z, int orientation)
	{
		int orientationX = (orientation == 1)?1:0;
		int orientationZ = (orientation == 2)?1:0;

		for (int yy = y-1; yy <= y+1; yy++)
		{
			if (!(world.getBlock(x-orientationX, yy, z-orientationZ).equals(ConfigBlocks.blockMagicalLog)&&world.getBlockMetadata(x-orientationX, yy, z-orientationZ)==1&&
			     world.getBlock(x+orientationX, yy, z+orientationZ).equals(ConfigBlocks.blockMagicalLog)&&world.getBlockMetadata(x+orientationX, yy, z+orientationZ)==1))
			{
				return false;
			}
		}

		if (world.getBlock(x, y+1, z).isOpaqueCube())
			return false;
		if (world.getBlock(x, y-1, z).isOpaqueCube())
			return false;

		return true;
	}
}
