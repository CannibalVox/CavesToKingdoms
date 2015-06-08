package talonos.blightbuster.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import talonos.blightbuster.BBStrings;
import talonos.blightbuster.BlightBuster;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileArcaneFurnace;
import thaumcraft.common.tiles.TileArcaneFurnaceNozzle;

import java.util.Random;

public class BlockDawnMachine extends Block {

    private IIcon top;
    private IIcon side;

    public BlockDawnMachine() {
        super(Material.wood);
        this.setLightLevel(.875f);
        this.setHardness(10.0F);
        this.setResistance(500.0F);
        this.setBlockName(BlightBuster.MODID+"_"+ BBStrings.dawnMachineName);
        this.setStepSound(soundTypeWood);
        GameRegistry.registerBlock(this, this.getUnlocalizedName());
    }

    private void restoreBlocks(World par1World, int par2, int par3, int par4) {
        for (int yy = -1; yy <= 1; yy++) {
            for (int xx = -1; xx <= 1; xx++) {
                for (int zz = -1; zz <= 1; zz++) {
                    Block block = par1World.getBlock(par2 + xx, par3 + yy, par4 + zz);
                    int md = par1World.getBlockMetadata(par2 + xx, par3 + yy, par4 + zz);
                    if (block == this) {
                        block = Block.getBlockFromItem(getItemDropped(md, new Random(), 0));
                        par1World.setBlock(par2 + xx, par3 + yy, par4 + zz, block, 0, 3);
                        par1World.notifyBlocksOfNeighborChange(par2 + xx, par3 + yy, par4 + zz, par1World.getBlock(par2 + xx, par3 + yy, par4 + zz));
                        par1World.markBlockForUpdate(par2 + xx, par3 + yy, par4 + zz);
                    }
                }
            }
        }
    }

    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
        int meta = par1World.getBlockMetadata(par2, par3, par4);
        if (meta == 0) {
            for (int yy = -1; yy <= 1; yy++) {
                for (int xx = -1; xx <= 1; xx++) {
                    for (int zz = -1; zz <= 1; zz++) {
                        if (((yy == 1) || (yy == 0)) && (zz == 0) && (xx == 0)) {
                            break;
                        }
                        Block block = par1World.getBlock(par2 + xx, par3 + yy, par4 + zz);
                        if (block != this) {
                            restoreBlocks(par1World, par2, par3, par4);
                            par1World.setBlockToAir(par2, par3, par4);
                            par1World.notifyBlocksOfNeighborChange(par2, par3, par4, par1World.getBlock(par2, par3, par4));
                            par1World.markBlockForUpdate(par2, par3, par4);
                            return;
                        }
                    }
                }
            }
        }
        super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
    }

    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
        if ((meta == 0) && (!world.isRemote)) {
            TileEntity te = world.getTileEntity(x, y, z);
            if ((te != null) && ((te instanceof TileArcaneFurnace))) {
                Entity newentity = EntityList.createEntityByName("Blaze", world);
                newentity.setLocationAndAngles(x + 0.5F, y + 1.0F, z + 0.5F, 0.0F, 0.0F);
                ((EntityLivingBase) newentity).addPotionEffect(new PotionEffect(Potion.regeneration.id, 6000, 2));
                ((EntityLivingBase) newentity).addPotionEffect(new PotionEffect(Potion.resistance.id, 12000, 0));
                world.spawnEntityInWorld(newentity);
            }
        }
        super.onBlockPreDestroy(world, x, y, z, meta);
    }

    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
        if (par1World.getBlockMetadata(par2, par3, par4) == 0) {
            restoreBlocks(par1World, par2, par3, par4);
        }
        for (int yy = -1; yy <= 1; yy++) {
            for (int xx = -1; xx <= 1; xx++) {
                for (int zz = -1; zz <= 1; zz++) {
                    par1World.notifyBlocksOfNeighborChange(par2 + xx, par3 + yy, par4 + zz, this);
                }
            }
        }
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    public Item getItemDropped(int meta, Random par2Random, int par3) {
        return (meta % 2 == 0) || (meta == 5) ? Item.getItemFromBlock(Blocks.obsidian) : meta == 10 ? Item.getItemFromBlock(Blocks.iron_bars) : meta == 0 ? Item.getItemById(0) : Item.getItemFromBlock(Blocks.nether_brick);
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
