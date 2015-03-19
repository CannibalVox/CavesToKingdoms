package talonos.blightbuster;

import talonos.biomescanner.TileEntityIslandMapper;
import talonos.biomescanner.TileEntityIslandScanner;
import talonos.blightbuster.DawnTotemEntity;
import talonos.cavestokingdoms.blocks.entities.AltarEntity;
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
