package talonos.blightbuster.handlers;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import talonos.blightbuster.blocks.BBBlock;
import talonos.blightbuster.multiblock.Multiblock;
import talonos.blightbuster.multiblock.entries.MultiblockEntry;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.IWandTriggerManager;
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

        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        Pair<MultiblockEntry, Integer> entry = BBBlock.dawnMachineMultiblock.getEntry(world, x, y, z, -1, block, meta);

        if (entry != null) {
            if (wand.consumeAllVisCrafting(stack, player, new AspectList().add(Aspect.ORDER, 20), true)) {
                if (!world.isRemote) {
                    BBBlock.dawnMachineMultiblock.convertMultiblockWithOrientationFromSideBlock(world, x, y, z, entry.getValue(), false, entry.getKey());
                    return true;
                }
                return false;
            }
        }

        return false;
    }
}
