package talonos.blightbuster.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import talonos.blightbuster.BBStrings;
import talonos.blightbuster.BlightBuster;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileArcaneFurnace;

import java.util.Random;

public class BlockDawnMachine extends Block {

    private IIcon top;
    private IIcon side;

    public BlockDawnMachine() {
        super(Material.wood);
        this.setLightLevel(.875f);
        this.setHardness(10.0F);
        this.setResistance(500.0F);
        this.setBlockName(BlightBuster.MODID + "_" + BBStrings.dawnMachineName);
        this.setStepSound(soundTypeWood);
        GameRegistry.registerBlock(this, this.getUnlocalizedName());
    }

    private void restoreBlocks(World world, int x, int y, int z, int orientation) {
        if (world.isRemote)
            return;

        int side = orientation + 2;
        ForgeDirection sideDir = ForgeDirection.VALID_DIRECTIONS[side];
        Block silverwood = ConfigBlocks.blockMagicalLog;
        restoreBlock(world, x-sideDir.offsetX, y+1, z-sideDir.offsetZ, BBBlock.dawnMachineInput, silverwood, 1);
        restoreBlock(world, x+sideDir.offsetX, y+1, z+sideDir.offsetZ, BBBlock.dawnMachineInput, silverwood, 1);
        restoreBlock(world, x-sideDir.offsetX, y, z-sideDir.offsetZ, BBBlock.dawnMachineInput, silverwood, 1);
        restoreBlock(world, x+sideDir.offsetX, y, z+sideDir.offsetZ, BBBlock.dawnMachineInput, silverwood, 1);
        restoreBlock(world, x-sideDir.offsetX, y-1, z-sideDir.offsetZ, BBBlock.dawnMachineBuffer, silverwood, 1);
        restoreBlock(world, x+sideDir.offsetX, y-1, z+sideDir.offsetZ, BBBlock.dawnMachineBuffer, silverwood, 1);
        restoreBlock(world, x, y, z, BBBlock.dawnMachine, BBBlock.dawnTotem, 0);
    }

    private void restoreBlock(World world, int x, int y, int z, Block convertedBlock, Block restoredBlock, int meta) {
        if (world.getBlock(x, y, z) == convertedBlock) {
            world.setBlock(x, y, z, restoredBlock, meta, 3);
        }
    }

//    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
//        int meta = par1World.getBlockMetadata(par2, par3, par4);
//        if (meta == 0) {
//            for (int yy = -1; yy <= 1; yy++) {
//                for (int xx = -1; xx <= 1; xx++) {
//                    for (int zz = -1; zz <= 1; zz++) {
//                        if (((yy == 1) || (yy == 0)) && (zz == 0) && (xx == 0)) {
//                            break;
//                        }
//                        Block block = par1World.getBlock(par2 + xx, par3 + yy, par4 + zz);
//                        if (block != this) {
//                            restoreBlocks(par1World, par2, par3, par4);
//                            par1World.setBlockToAir(par2, par3, par4);
//                            par1World.notifyBlocksOfNeighborChange(par2, par3, par4, par1World.getBlock(par2, par3, par4));
//                            par1World.markBlockForUpdate(par2, par3, par4);
//                            return;
//                        }
//                    }
//                }
//            }
//        }
//        super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
//    }

    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        int orientation = meta % 4;
        restoreBlocks(world, x, y, z, orientation);

        for (int yy = -1; yy <= 1; yy++) {
            for (int xx = -1; xx <= 1; xx++) {
                for (int zz = -1; zz <= 1; zz++) {
                    world.notifyBlocksOfNeighborChange(x + xx, y + yy, z + zz, this);
                }
            }
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    public Item getItemDropped(int meta, Random par2Random, int par3) {
        return Item.getItemFromBlock(BBBlock.dawnTotem);
    }

    public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
        if (par5 == 1) {
            if (par1World.isRemote) {
                Thaumcraft.proxy.blockSparkle(par1World, par2, par3, par4, 16736256, 5);
            }
            return true;
        }
        return super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister registry) {
        top = registry.registerIcon("blightbuster:dawnMachineTop");
        side = registry.registerIcon("blightbuster:dawnMachineSide");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        if (side == 0 || side == 1)
            return top;
        return this.side;
    }
}
