package talonos.blightbuster.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public abstract class BlockMultiblock extends Block {
    private Multiblock multiblock;

    public BlockMultiblock(Material material, Multiblock multiblock) {
        super(material);
        this.multiblock = multiblock;
    }

    public Multiblock getMultiblock() { return this.multiblock; }

    public abstract int getMultiblockPosition(World world, int x, int y, int z);

}
