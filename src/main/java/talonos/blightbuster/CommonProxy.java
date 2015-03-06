package talonos.blightbuster;

import talonos.blightbuster.DawnTotemEntity;
import talonos.cavestokingdoms.blocks.entities.AltarEntity;
import talonos.cavestokingdoms.blocks.entities.TileEntityIslandMapper;
import talonos.cavestokingdoms.blocks.entities.TileEntityIslandScanner;
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
