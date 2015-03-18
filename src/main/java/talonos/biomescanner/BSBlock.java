package talonos.biomescanner;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BSBlock extends Block
{
	
    /**
     * Constructor for when no material is passed on.
     * Default material: rock
     */
    public BSBlock()
    {
        super(Material.rock);
    }

    /**
     * Constructor for defined material.
     * @param material
     */
    public BSBlock(Material material)
    {
        super(material);
    }
    
    public static Block monitorLeft;
    public static Block monitorRight;
    public static Block islandMapper;
    public static Block scannerController;
    public static Block gaugeBot;
    public static Block gaugeMid;
    public static Block gaugeTop;
    public static Block bedrockBrick;

	public static void init() 
	{
	    monitorLeft = new BiomeMonitorLeft();
	    monitorRight = new BiomeMonitorRight();
	    islandMapper = new BlockIslandMapper();
	    scannerController = new BlockScannerController();
	    gaugeBot = new GaugeBlockBot();
	    gaugeMid = new GaugeBlockMid();
	    gaugeTop = new GaugeBlockTop();
	    bedrockBrick = new BedrockBrick();
	}

}
