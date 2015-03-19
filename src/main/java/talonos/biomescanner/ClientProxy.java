package talonos.biomescanner;

import mantle.client.MProxyClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Blocks;
import talonos.cavestokingdoms.CtKItems;
import talonos.cavestokingdoms.client.pages.ExtMaterialsPage;
import talonos.cavestokingdoms.client.pages.ExtMaterialsUsagePage;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers() 
    {
    	super.registerRenderers();	
        
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityIslandMapper.class, new TileEntityMapperRenderer());
        
    }
}
