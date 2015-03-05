package talonos.cavestokingdoms.blocks;

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
    public static Block bedrockBrick;
    public static Block monitorLeft;
    public static Block monitorRight;
    public static Block islandMapper;
    public static Block scannerController;
    public static Block gaugeBot;
    public static Block gaugeMid;
    public static Block gaugeTop;
    public static Block dawnTotem;

	public static void init() 
	{
	    altarBlock = new AltarBlock();
	    spiritStoneBlock = new SpiritStoneBlock();
	    bedrockBrick = new BedrockBrick();
	    monitorLeft = new BiomeMonitorLeft();
	    monitorRight = new BiomeMonitorRight();
	    islandMapper = new BlockIslandMapper();
	    scannerController = new BlockScannerController();
	    gaugeBot = new GaugeBlockBot();
	    gaugeMid = new GaugeBlockMid();
	    gaugeTop = new GaugeBlockTop();
	    dawnTotem = new BlockDawnTotem(Material.wood);
	}

}