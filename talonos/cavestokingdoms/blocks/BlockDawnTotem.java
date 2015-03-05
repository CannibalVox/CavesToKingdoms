package talonos.cavestokingdoms.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import talonos.cavestokingdoms.blocks.entities.AltarEntity;
import talonos.cavestokingdoms.blocks.entities.DawnTotemEntity;
import talonos.cavestokingdoms.lib.DEFS;

public class BlockDawnTotem extends CtKBlock
{
	public BlockDawnTotem(Material m)
	{
		super(m);
		this.setBlockName(DEFS.MODID+"_"+DEFS.DawnTotemBlockName);
		this.setStepSound(soundTypeWood);
		this.setBlockTextureName("dawnTotem");
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setLightLevel(.875f);
		GameRegistry.registerBlock(this, this.getUnlocalizedName());
	}
	
    @Override
    public boolean hasTileEntity(int meta)
    {
        return true;
    }
 
    @Override
    public TileEntity createTileEntity(World world, int meta)
    {
        return new DawnTotemEntity();
    }
    
    //Icon stuff:
    
	/* ICONS */
	public static final int TOP_BOT = 0;
	public static final int SIDES = 1;

	@SideOnly(Side.CLIENT)
	private static IIcon[] icons;

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) 
	{
		icons = new IIcon[2];
		for (int x = 0; x < 2; x++)
		{
			icons[x] = iconRegister.registerIcon(DEFS.MODID+":"+DEFS.DawnTotemBlockName+x);
		}
		this.blockIcon = icons[0];
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) 
	{
		if (side == ForgeDirection.UP.ordinal()||side==ForgeDirection.DOWN.ordinal())
		{
			return icons[1];
		}
		return icons[0]; 
	}
}
