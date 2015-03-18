package talonos.cavestokingdoms.blocks;

import talonos.biomescanner.BedrockBrick;
import talonos.biomescanner.BiomeMonitorLeft;
import talonos.biomescanner.BiomeMonitorRight;
import talonos.biomescanner.BlockIslandMapper;
import talonos.biomescanner.BlockScannerController;
import talonos.biomescanner.GaugeBlockBot;
import talonos.biomescanner.GaugeBlockMid;
import talonos.biomescanner.GaugeBlockTop;
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

public class CtKBlock extends Block
{
    /**
     * Constructor for when no material is passed on.
     * Default material: rock
     */
    public CtKBlock()
    {
        super(Material.rock);
    }

    /**
     * Constructor for defined material.
     * @param material
     */
    public CtKBlock(Material material)
    {
        super(material);
    }
    
    public static Block altarBlock;
    public static Block spiritStoneBlock;

	public static void init() 
	{
	    altarBlock = new AltarBlock();
	    spiritStoneBlock = new SpiritStoneBlock();
	}

}