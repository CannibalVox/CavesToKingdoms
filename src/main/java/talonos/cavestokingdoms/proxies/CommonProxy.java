package talonos.cavestokingdoms.proxies;

import talonos.cavestokingdoms.blocks.entities.AltarEntity;
import talonos.cavestokingdoms.blocks.entities.DawnTotemEntity;
import talonos.cavestokingdoms.blocks.entities.TileEntityIslandMapper;
import talonos.cavestokingdoms.blocks.entities.TileEntityIslandScanner;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy 
{
    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityIslandMapper.class, "islandMapper");
        GameRegistry.registerTileEntity(TileEntityIslandScanner.class, "islandScanner");
        GameRegistry.registerTileEntity(AltarEntity.class, "AltarEntity");
        GameRegistry.registerTileEntity(DawnTotemEntity.class, "DawnTotemEntity");
    }
    
    public void registerRenderers() 
    {
    }
}
