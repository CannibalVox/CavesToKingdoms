package talonos.cavestokingdoms.blocks;

import talonos.cavestokingdoms.lib.DEFS;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;

public class BedrockBrick extends CtKBlock
{

	public BedrockBrick()
	{
		this.setBlockName(DEFS.MODID+"_"+DEFS.bedrockBrickName);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
		this.setStepSound(soundTypePiston);
		this.disableStats();
		this.setCreativeTab(CreativeTabs.tabBlock);
		GameRegistry.registerBlock(this, this.getUnlocalizedName());
	}
	
    /**
     * Overrides the registerBlockIcon method.
     * This method handles all the textures.
     * Call registerIcon() and pass it a
     * Format: [modid]:[blockname]
     * @param iconRegister
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon(DEFS.MODID+":"+DEFS.bedrockBrickName);
    }
}
