package talonos.blightbuster;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import cpw.mods.fml.client.registry.RenderingRegistry;
import talonos.blightbuster.entities.EntitySilverPotion;
import talonos.blightbuster.items.BBItems;

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
