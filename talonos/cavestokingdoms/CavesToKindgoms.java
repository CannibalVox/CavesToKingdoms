package talonos.cavestokingdoms;

import talonos.cavestokingdoms.blocks.CtKBlock;
import talonos.cavestokingdoms.blocks.entities.TileEntityIslandMapper;
import talonos.cavestokingdoms.blocks.entities.TileEntityIslandScanner;
import talonos.cavestokingdoms.extendedproperties.OrbEventHandler;
import talonos.cavestokingdoms.items.PurityFocusEventHandler;
import talonos.cavestokingdoms.lib.DEFS;
import talonos.cavestokingdoms.proxies.CommonProxy;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = DEFS.MODID, name = DEFS.MODNAME, version = DEFS.VERSION, dependencies = DEFS.DEPS)
public class CavesToKindgoms
{
	@SidedProxy(clientSide = DEFS.CLIENTPROXYLOCATION, serverSide = DEFS.COMMONPROXYLOCATION)
	public static CommonProxy proxy;
	
	public static CavesToKindgoms instance;
	
	public static ManualInfo manualInfo;
	
	@Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event)
    {
		CtKBlock.init();
		CtKItems.init();
		proxy.registerTileEntities();
    	UBCIntegration.init(event);
    }
 
    @Mod.EventHandler
    public static void init(FMLInitializationEvent event)
    {
    	MinecraftForge.EVENT_BUS.register(new OrbEventHandler());
    	
    	MinecraftForge.EVENT_BUS.register(new PurityFocusEventHandler());
        FMLCommonHandler.instance().bus().register(new PurityFocusEventHandler());
        
        manualInfo = new ManualInfo();
    }
 
    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event)
    {
    	AddedResearch.initResearch();
        proxy.registerRenderers();
        BiomeMapColors.initColors();
    }
}
