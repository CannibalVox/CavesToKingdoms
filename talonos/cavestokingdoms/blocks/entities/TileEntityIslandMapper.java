package talonos.cavestokingdoms.blocks.entities;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TileEntityIslandMapper extends TileEntity
{
	public static final int blockWidth = 176;
	public static final int blockHeight = 180;
	
	byte[] mapData = new byte[176*180];
	
	public TileEntityIslandMapper()
	{
		super();
		
		Random r = new Random();
		r.nextBytes(mapData);
	}

	
    @Override
    public void writeToNBT(NBTTagCompound par1)
    {
    	super.writeToNBT(par1);
    	par1.setByteArray("mapData", mapData);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1)
    {
    	super.readFromNBT(par1);
    	mapData = par1.getByteArray("mapData");
    	if (mapData.length>(176*180))
    	{
    		mapData = new byte[176*180];
    	}
    }
    
    @Override
    public void updateEntity()
    {
    }
    
    @Override
    public Packet getDescriptionPacket() 
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
    }
    
    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for
     * sending the packet.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	readFromNBT(pkt.func_148857_g());
    }
}