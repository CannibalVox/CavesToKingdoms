package talonos.blightbuster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.CommonProxy;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.tiles.TileArcaneFurnace;
import thaumcraft.common.tiles.TileArcaneFurnaceNozzle;

public class BlockDawnMachine extends BlockContainer
{
  public BlockDawnMachine()
  {
    super(Material.wood);
    setHardness(10.0F);
    setResistance(500.0F);
    setLightLevel(0.5F);
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }
  
  public IIcon[] icon = new IIcon[31];
  
  public void registerBlockIcons(IIconRegister ir)
  {
    /*for (int a = 0; a < 31; a++) 
    {
        this.icon[a] = ir.registerIcon(BlightBuster.MODID+":dawnmachine" + a);
    }*/
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
  {
    return calculateTexture(world, x, y, z, side);
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon calculateTexture(IBlockAccess world, int x, int y, int z, int side)
  {
    int meta = world.getBlockMetadata(x, y, z);
    return this.icon[30];
  }
  
  public int getLightValue(IBlockAccess world, int x, int y, int z)
  {
    int meta = world.getBlockMetadata(x, y, z);
    if ((meta == 3) || (meta == 11)) {
      return 15;
    }
    return super.getLightValue(world, x, y, z);
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k)
  {
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }
  
  public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int i, int j, int k)
  {
    return AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
  }
  
  
  private void restoreBlocks(World par1World, int par2, int par3, int par4)
  {
    for (int yy = -1; yy <= 1; yy++) {
      for (int xx = -1; xx <= 1; xx++) {
        for (int zz = -1; zz <= 1; zz++)
        {
          Block block = par1World.getBlock(par2 + xx, par3 + yy, par4 + zz);
          int md = par1World.getBlockMetadata(par2 + xx, par3 + yy, par4 + zz);
          if (block == this)
          {
            block = Block.getBlockFromItem(getItemDropped(md, new Random(), 0));
            par1World.setBlock(par2 + xx, par3 + yy, par4 + zz, block, 0, 3);
            par1World.notifyBlocksOfNeighborChange(par2 + xx, par3 + yy, par4 + zz, par1World.getBlock(par2 + xx, par3 + yy, par4 + zz));
            par1World.markBlockForUpdate(par2 + xx, par3 + yy, par4 + zz);
          }
        }
      }
    }
  }
  
  public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5)
  {
    int meta = par1World.getBlockMetadata(par2, par3, par4);
    if (meta == 0) {
      for (int yy = -1; yy <= 1; yy++) {
        for (int xx = -1; xx <= 1; xx++) {
          for (int zz = -1; zz <= 1; zz++)
          {
            if (((yy == 1) || (yy == 0)) && (zz == 0) && (xx == 0)) {
              break;
            }
            Block block = par1World.getBlock(par2 + xx, par3 + yy, par4 + zz);
            if (block != this)
            {
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
  
  public void onBlockPreDestroy(World world, int x, int y, int z, int meta)
  {
    if ((meta == 0) && (!world.isRemote))
    {
      TileEntity te = world.getTileEntity(x, y, z);
      if ((te != null) && ((te instanceof TileArcaneFurnace)))
      {
        Entity newentity = EntityList.createEntityByName("Blaze", world);
        newentity.setLocationAndAngles(x + 0.5F, y + 1.0F, z + 0.5F, 0.0F, 0.0F);
        ((EntityLivingBase)newentity).addPotionEffect(new PotionEffect(Potion.regeneration.id, 6000, 2));
        ((EntityLivingBase)newentity).addPotionEffect(new PotionEffect(Potion.resistance.id, 12000, 0));
        world.spawnEntityInWorld(newentity);
      }
    }
    super.onBlockPreDestroy(world, x, y, z, meta);
  }
  
  public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
  {
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
  
  public Item getItemDropped(int meta, Random par2Random, int par3)
  {
    return (meta % 2 == 0) || (meta == 5) ? Item.getItemFromBlock(Blocks.obsidian) : meta == 10 ? Item.getItemFromBlock(Blocks.iron_bars) : meta == 0 ? Item.getItemById(0) : Item.getItemFromBlock(Blocks.nether_brick);
  }
  
  public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
  {
    if (world.getBlockMetadata(x, y, z) == 0) {
      return false;
    }
    return true;
  }
  
  public int getRenderType()
  {
    return ConfigBlocks.blockArcaneFurnaceRI;
  }
  
  public boolean isOpaqueCube()
  {
    return false;
  }
  
  public boolean renderAsNormalBlock()
  {
    return false;
  }
  
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
  {
    if ((par1World.getBlockMetadata(par2, par3, par4) == 0) && (par1World.getBlock(par2, par3 + 1, par4).getMaterial() == Material.air) && (!par1World.getBlock(par2, par3 + 1, par4).isOpaqueCube())) {
      for (int a = 0; a < 3; a++)
      {
        double var7 = par2 + par5Random.nextFloat();
        double var8 = par3 + 1.0F + par5Random.nextFloat() * 0.5F;
        double var9 = par4 + par5Random.nextFloat();
        par1World.spawnParticle("largesmoke", var7, var8, var9, 0.0D, 0.0D, 0.0D);
      }
    }
  }
  
  public TileEntity createTileEntity(World world, int metadata)
  {
    if (metadata == 0) {
      return new TileArcaneFurnace();
    }
    if ((metadata == 2) || (metadata == 4) || (metadata == 5) || (metadata == 6) || (metadata == 8)) {
      return new TileArcaneFurnaceNozzle();
    }
    return super.createTileEntity(world, metadata);
  }
  
  public TileEntity createNewTileEntity(World var1, int md)
  {
    return null;
  }
  
  public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6)
  {
    if (par5 == 1)
    {
      if (par1World.isRemote) {
        Thaumcraft.proxy.blockSparkle(par1World, par2, par3, par4, 16736256, 5);
      }
      return true;
    }
    return super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
  }
}
