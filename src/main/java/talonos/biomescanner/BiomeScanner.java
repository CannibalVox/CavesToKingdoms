package talonos.biomescanner;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import talonos.biomescanner.block.BSBlock;
import talonos.biomescanner.client.BiomeMapColors;

@Mod(modid = BiomeScanner.MODID, name = BiomeScanner.MODNAME, version = BiomeScanner.VERSION, dependencies = BiomeScanner.DEPS)
public class BiomeScanner
{
	
	public static final String MODID = "biomescanner";
	public static final String MODNAME = "Biome Scanner";
    public static final String VERSION = "0.1.0";
    public static final String DEPS = "before:UndergroundBiomes;after:ThermalFoundation;after:appliedenergistics2;after:Thaumcraft";
	public static final String COMMONPROXYLOCATION = "talonos."+MODID+".CommonProxy";
	public static final String CLIENTPROXYLOCATION = "talonos."+MODID+".ClientProxy";
	
	
	
	@SidedProxy(clientSide = BiomeScanner.CLIENTPROXYLOCATION, serverSide = BiomeScanner.COMMONPROXYLOCATION)
	public static CommonProxy proxy;
	
	public static BiomeScanner instance;
	
	@Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event)
    {
		BSBlock.init();
		BSItems.init();
		proxy.registerTileEntities();
    }
 
    @Mod.EventHandler
    public static void init(FMLInitializationEvent event)
    {

    }
 
    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event)
    {
        BiomeMapColors.initColors();
        proxy.registerRenderers();
    }
}
