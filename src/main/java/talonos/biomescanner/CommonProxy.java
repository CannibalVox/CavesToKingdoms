package talonos.biomescanner;

import talonos.cavestokingdoms.blocks.entities.AltarEntity;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy 
{
    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityIslandMapper.class, "islandMapper");
        GameRegistry.registerTileEntity(TileEntityIslandScanner.class, "islandScanner");
    }
    
    public void registerRenderers() 
    {
    }
}
