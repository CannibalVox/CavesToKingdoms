package talonos.blightbuster;

import talonos.cavestokingdoms.lib.DEFS;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

/**
 * Thanks to Martijn Woudstra for the code samples.
 * @Author Talonos
 */

public class BBBlock extends Block
{
    /**
     * Constructor for when no material is passed on.
     * Default material: rock
     */
    public BBBlock()
    {
        super(Material.rock);
    }

    /**
     * Constructor for defined material.
     * @param material
     */
    public BBBlock(Material material)
    {
        super(material);
    }
    
    public static Block dawnTotem;

	public static void init() 
	{
	    dawnTotem = new BlockDawnTotem(Material.wood);
	}

}