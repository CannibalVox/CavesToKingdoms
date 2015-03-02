package talonos.cavestokingdoms.proxies;

import mantle.client.MProxyClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Blocks;
import talonos.cavestokingdoms.CtKItems;
import talonos.cavestokingdoms.blocks.entities.TileEntityIslandMapper;
import talonos.cavestokingdoms.blocks.entities.TileEntityMapperRenderer;
import talonos.cavestokingdoms.client.pages.ExtMaterialsPage;
import talonos.cavestokingdoms.client.pages.ExtMaterialsUsagePage;
import talonos.cavestokingdoms.items.EntitySilverPotion;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers() 
    {
    	super.registerRenderers();	
        //EntityRegistry.registerGlobalEntityID(EntitySilverPotion.class, "SilverPotion", ModLoader.getUniqueEntityId());
        
        RenderingRegistry.registerEntityRenderingHandler(EntitySilverPotion.class, new RenderSnowball(CtKItems.silverPotion));
        RenderManager.instance.entityRenderMap.put(EntitySilverPotion.class, new RenderSnowball(CtKItems.silverPotion));
        System.out.println(RenderManager.instance.entityRenderMap);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityIslandMapper.class, new TileEntityMapperRenderer());
        
        MProxyClient.registerManualPage("extMaterialPage", ExtMaterialsPage.class);
        MProxyClient.registerManualPage("extMaterialUsePage", ExtMaterialsUsagePage.class);
    }
}
