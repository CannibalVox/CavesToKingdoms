package talonos.blightbuster;

import mantle.client.MProxyClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Blocks;
import talonos.blightbuster.EntitySilverPotion;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers() 
    {
    	super.registerRenderers();	
        
        RenderingRegistry.registerEntityRenderingHandler(EntitySilverPotion.class, new RenderSnowball(BBItems.silverPotion));
        RenderManager.instance.entityRenderMap.put(EntitySilverPotion.class, new RenderSnowball(BBItems.silverPotion));

    }

    @Override
    public double getBestCleanseSpawnHeight() {
        return Minecraft.getMinecraft().thePlayer.posY;
    }
}
