package talonos.biomescanner.block;

import cpw.mods.fml.common.registry.GameRegistry;
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
	    monitorLeft = new GaugeBlock().setPos(3);
	    monitorRight = new GaugeBlock().setPos(4);
	    islandMapper = new BlockIslandMapper();
	    scannerController = new BlockScannerController();
	    gaugeBot = new GaugeBlock().setPos(0);
	    gaugeMid = new GaugeBlock().setPos(1);
	    gaugeTop = new GaugeBlock().setPos(2);
	    bedrockBrick = new BedrockBrick();

        GameRegistry.registerBlock(gaugeBot, gaugeBot.getUnlocalizedName());
        GameRegistry.registerBlock(gaugeMid, gaugeMid.getUnlocalizedName());
        GameRegistry.registerBlock(gaugeTop, gaugeTop.getUnlocalizedName());
        GameRegistry.registerBlock(monitorLeft, monitorLeft.getUnlocalizedName());
        GameRegistry.registerBlock(monitorRight, monitorRight.getUnlocalizedName());
	}

}
