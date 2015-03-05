package talonos.cavestokingdoms.blocks;

import talonos.cavestokingdoms.blocks.entities.TileEntityIslandScanner;
import talonos.cavestokingdoms.lib.DEFS;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockScannerController extends CtKBlock implements ITileEntityProvider
{
	public BlockScannerController() 
	{
		this.setBlockName(DEFS.MODID+"_"+DEFS.blockScannerControllerName);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
		this.setStepSound(soundTypePiston);
		this.disableStats();
		this.setCreativeTab(CreativeTabs.tabBlock);
		GameRegistry.registerBlock(this, this.getUnlocalizedName());
	}
	      
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon(DEFS.MODID + ":" + DEFS.bedrockBrickName);
	}
	
    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }

	@Override
	public TileEntity createNewTileEntity(World world, int iDunno) 
	{
		return new TileEntityIslandScanner();
	}
}
