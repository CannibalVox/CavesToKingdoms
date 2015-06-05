package talonos.blightbuster;

import cpw.mods.fml.common.registry.GameRegistry;
import talonos.blightbuster.tileentity.DawnTotemEntity;

public class CommonProxy 
{
    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(DawnTotemEntity.class, "DawnTotemEntity");
    }
    
    public void registerRenderers() 
    {
    }

    public double getBestCleanseSpawnHeight() {
        return 0;
    }
}
