package talonos.biomescanner.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import talonos.biomescanner.BSStrings;
import talonos.biomescanner.BiomeScanner;
import talonos.biomescanner.block.BSBlock;

public class GaugeBlock extends BSBlock
{
	int pos;

	public GaugeBlock()
	{
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
		this.setStepSound(soundTypePiston);
		this.disableStats();
		this.setCreativeTab(CreativeTabs.tabBlock);
		GameRegistry.registerBlock(this, this.getUnlocalizedName());
		this.setBlockUnbreakable();
	}

    public GaugeBlock setPos(int pos) {
        this.pos=pos;
        this.setBlockName(BiomeScanner.MODID+"_"+ BSStrings.GaugeBlockName+pos);
        return this;
    }
	
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	
	@SideOnly(Side.CLIENT)
	private IIcon[] biomeIcons;
	      
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
	   icons = new IIcon[48];
	   biomeIcons = new IIcon[22*4];
	   for(int i = 0; i < 48; i++)
	   {
		   icons[i] = par1IconRegister.registerIcon(BiomeScanner.MODID + ":" + "statbar" + (i));
	   }
	   for (int x = 0; x < 1; x++)
	   {
		   for (int y = 0; y < 1; y++)
		   {
			   biomeIcons[x*4+y] = par1IconRegister.registerIcon(BiomeScanner.MODID+":"+"biomeMon" +x+"."+y);			   
		   }
	   }
	}
	
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public IIcon getIcon(int par1, int par2)
    {
    	if (pos < 3)
    	{
    		return icons[par2+(16*pos)];
    	}
    	//return biomeIcons[(pos-3)*44+par2*4];
    	return biomeIcons[0];
    }
}
