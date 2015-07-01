package talonos.cavestokingdoms;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import talonos.cavestokingdoms.blocks.CtKBlock;
import talonos.cavestokingdoms.client.pages.OreDiscoveryRegistry;
import talonos.cavestokingdoms.command.DiscoverAll;
import talonos.cavestokingdoms.command.ListShaftMaterials;
import talonos.cavestokingdoms.command.WipeDiscoveryProgress;
import talonos.cavestokingdoms.extendedproperties.OrbEventHandler;
import talonos.cavestokingdoms.lib.DEFS;
import talonos.cavestokingdoms.proxies.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.ToolMaterial;

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

        manualInfo = new ManualInfo();
    }

    @Mod.EventHandler
    public static void serverLoad(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new ListShaftMaterials());
        event.registerServerCommand(new DiscoverAll());
        event.registerServerCommand(new WipeDiscoveryProgress());
    }
 
    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event)
    {
        proxy.registerRenderers();
        OreDiscoveryRegistry.getInstance(); //Constructs and therefore loads all discovery registry stuff.
    }
}
