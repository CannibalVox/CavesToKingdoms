package talonos.blightbuster.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.tuple.Pair;
import talonos.blightbuster.blocks.BBBlock;
import talonos.blightbuster.multiblock.entries.MultiblockEntry;
import thaumcraft.common.Thaumcraft;

import java.util.Random;

public abstract class BlockMultiblock extends Block {
    private Multiblock multiblock;

    public BlockMultiblock(Material material, Multiblock multiblock) {
        super(material);
        this.multiblock = multiblock;
    }

    public Multiblock getMultiblock() { return this.multiblock; }

    public int transformSide(int side, int meta) {
        int orientation = meta & 0x3;
        ForgeDirection sideDir = ForgeDirection.VALID_DIRECTIONS[side];

        for (int i = 0; i < orientation; i++) {
            sideDir = sideDir.getRotation(ForgeDirection.DOWN);
        }

        return sideDir.ordinal();
    }

    @Override
    public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
        if (par5 == 1) {
            if (par1World.isRemote) {
                Thaumcraft.proxy.blockSparkle(par1World, par2, par3, par4, 16736256, 5);
            }
            return true;
        }
        return super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
    }

    public int getOrientation(int meta) { return meta & 0x3; }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {

        Pair<MultiblockEntry, Integer> entry = getMultiblock().getEntry(world, x, y, z, getOrientation(meta), block, meta);

        if (entry != null)
            getMultiblock().convertMultiblockWithOrientationFromSideBlock(world, x, y, z, entry.getValue(), true, entry.getKey());

        super.breakBlock(world, x, y, z, block, meta);
    }
}
