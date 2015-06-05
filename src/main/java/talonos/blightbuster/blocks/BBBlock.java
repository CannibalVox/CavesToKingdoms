package talonos.blightbuster.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

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