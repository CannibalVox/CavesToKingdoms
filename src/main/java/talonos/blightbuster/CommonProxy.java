package talonos.blightbuster;

import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy 
{
    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(DawnTotemEntity.class, "DawnTotemEntity");
    }
    
    public void registerRenderers() 
    {
    }
}
