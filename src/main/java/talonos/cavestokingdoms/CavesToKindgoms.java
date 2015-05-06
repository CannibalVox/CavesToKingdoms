package talonos.cavestokingdoms;

import talonos.cavestokingdoms.blocks.CtKBlock;
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

        TConstructRegistry.addtoolMaterial(getNextClearMaterialId(), new ToolMaterial("Soft1", "soft1", 0, 40, 150, 1, 1.0f, 0, 0, "\u00a7e", 0x755821));
        TConstructRegistry.addtoolMaterial(getNextClearMaterialId(), new ToolMaterial("HardNonMetal", "hardnonmetal", 1, 80, 150, 1, 0.30000001192092896f, 0, 1, "\u00a77", 0x7F7F7F));
        TConstructRegistry.addtoolMaterial(getNextClearMaterialId(), new ToolMaterial("SoftMetal", "softmetal", 2, 144, 500, 2, 1.100000023841858f, 0, 0, "\u00a7c", 0xCC6410));
        TConstructRegistry.addtoolMaterial(getNextClearMaterialId(), new ToolMaterial("HardMetal", "hardmetal", 3, 200, 600, 3, 1.2000000476837158f, 1, 0, "\u00a7f", 0xDADADA));
        TConstructRegistry.addtoolMaterial(getNextClearMaterialId(), new ToolMaterial("DiamondLevel", "diamondlevel", 4, 320, 700, 3, 1.3f, 2, 0, "\u00a77", 0xA0A0A0));
        TConstructRegistry.addtoolMaterial(getNextClearMaterialId(), new ToolMaterial("SupraDiamond", "supradiamond", 5, 534, 780, 3, 1.35f, 1, 0, "\u00a7c", 0xF0A8A4));
        TConstructRegistry.addtoolMaterial(getNextClearMaterialId(), new ToolMaterial("Alien", "alien", 6, 640, 1100, 4, 1.75f, 2, 0, "\u00a73", 0xFFFFFF));
        TConstructRegistry.addtoolMaterial(getNextClearMaterialId(), new ToolMaterial("Amazing", "amazing", 7, 960, 900, 5, 2.5f, 0, 0, "\u00a75", 0x7338A5));
        TConstructRegistry.addtoolMaterial(getNextClearMaterialId(), new ToolMaterial("Extreme", "extreme", 8, 1240, 1000, 5, 2.3f, 2, 0, "\u00a74", 0xFFFFFF));
        TConstructRegistry.addtoolMaterial(getNextClearMaterialId(), new ToolMaterial("Epic", "epic", 9, 1600, 1500, 6, 3.33f, 0, 0, "\u00a76", 0xFFFFFF));

        manualInfo = new ManualInfo();
    }

    private static int getNextClearMaterialId() {
        int id = 0;
        while (id < 1024) {
            ToolMaterial material = TConstructRegistry.getMaterial(id);

            if (material == null)
                return id;
            id++;
        }

        throw new RuntimeException("All TCon material ID's are taken.  What is this sorcery?");
    }
 
    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event)
    {
        proxy.registerRenderers();
    }
}
