package talonos.blightbuster.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import talonos.blightbuster.blocks.BBBlock;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.IWandTriggerManager;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.wands.ItemWandCasting;

public class TalonosWandTriggerManager implements IWandTriggerManager {

    @Override
    public boolean performTrigger(World world, ItemStack wand,
                                  EntityPlayer player, int x, int y, int z, int side, int event) {

        switch (event) {
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
                                      World world, int x, int y, int z) {
        if (world.isRemote)
            return false;

        ItemWandCasting wand = (ItemWandCasting) stack.getItem();
        int orientation = fitDawnMachine(world, x, y, z);

        if (orientation == 0)
            return false;

        if (wand.consumeAllVisCrafting(stack, player, new AspectList().add(Aspect.ORDER, 20), true)) {
            if (!world.isRemote) {
                replaceDawnMachine(player, world, x, y, z, orientation);
                return true;
            }
            return false;
        }
        return false;
    }

    private void replaceDawnMachine(EntityPlayer player, World world, int x, int y, int z, int orientation) {
        int orientationX = (orientation == 3) ? 1 : 0;
        int orientationZ = (orientation == 5) ? 1 : 0;

        world.setBlock(x-orientationX, y-1, z-orientationZ, BBBlock.dawnMachineBuffer, 0, 3);
        world.addBlockEvent(x-orientationX, y-1, z-orientationZ, BBBlock.dawnMachineBuffer, 1, 0);
        world.setBlock(x+orientationX, y-1, z+orientationZ, BBBlock.dawnMachineBuffer, 0, 3);
        world.addBlockEvent(x+orientation, y-1, z+orientationZ, BBBlock.dawnMachineBuffer, 1, 0);

        int metaDir = (orientation-2) % 4;
        world.setBlock(x-orientationX, y, z-orientationZ, BBBlock.dawnMachineInput, metaDir, 3);
        world.addBlockEvent(x-orientationX, y, z-orientationZ, BBBlock.dawnMachineInput, 1, 0);
        world.setBlock(x+orientationX, y, z+orientationZ, BBBlock.dawnMachineInput, 4+metaDir, 3);
        world.addBlockEvent(x+orientationX, y, z+orientationZ, BBBlock.dawnMachineInput, 1, 0);
        world.setBlock(x-orientationX, y+1, z-orientationZ, BBBlock.dawnMachineInput, 8+metaDir, 3);
        world.addBlockEvent(x-orientationX, y+1, z-orientationZ, BBBlock.dawnMachineInput, 1, 0);
        world.setBlock(x+orientationX, y+1, z+orientationZ, BBBlock.dawnMachineInput, 12+metaDir, 3);
        world.addBlockEvent(x+orientationX, y+1, z+orientationZ, BBBlock.dawnMachineInput, 1, 0);

        world.setBlock(x, y, z, BBBlock.dawnMachine, orientation, 3);
        world.addBlockEvent(x, y, z, BBBlock.dawnMachine, 1, 0);
    }

    private int fitDawnMachine(World world, int x, int y, int z) {
        if (world.getBlock(x, y, z).equals(BBBlock.dawnTotem)) ;
        {
            for (int orientation = 2; orientation < 6; orientation++) {
                if (checkDawnMachine(world, x, y, z, orientation)) {
                    return orientation;
                }
            }
        }
        return 0;
    }

    private boolean checkDawnMachine(World world, int x, int y, int z, int orientation) {
        //Hack for now, ideally we'll allow all 4 directions eventually
        if (orientation != 3 && orientation != 5)
            return false;

        int orientationX = (orientation == 3) ? 1 : 0;
        int orientationZ = (orientation == 5) ? 1 : 0;

        for (int yy = y - 1; yy <= y + 1; yy++) {
            if (!(world.getBlock(x - orientationX, yy, z - orientationZ).equals(ConfigBlocks.blockMagicalLog) && world.getBlockMetadata(x - orientationX, yy, z - orientationZ) == 1 &&
                    world.getBlock(x + orientationX, yy, z + orientationZ).equals(ConfigBlocks.blockMagicalLog) && world.getBlockMetadata(x + orientationX, yy, z + orientationZ) == 1)) {
                return false;
            }
        }

        if (world.getBlock(x, y + 1, z).isOpaqueCube())
            return false;
        if (world.getBlock(x, y - 1, z).isOpaqueCube())
            return false;

        return true;
    }
}
