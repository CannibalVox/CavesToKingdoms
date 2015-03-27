package talonos.biomescanner;

import java.util.HashSet;
import java.util.Set;

import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.biome.BiomeGenBase;

public class TileEntityIslandScanner extends TileEntity 
{
	public static final int mapWidthChunks = 110;

	int lastScannedChunk = 0;

	public TileEntityIslandScanner() 
	{
	}

	@Override
	public void updateEntity()
	{
		if (worldObj.isRemote) 
		{
			// lastScannedChunk++;
			BiomeMapColors.updateFlash(worldObj.getWorldTime());
			return;
		}
		if (lastScannedChunk == 0) 
		{
			System.out.println("I think this is a new instance.");
			// You have not started scanning yet. Initiate rescan: Darken the map.
			for (int x = -2; x <= 2; x++) 
			{
				for (int y = 0; y <= 6; y++) 
				{
					TileEntity hopefullyAMap = worldObj.getTileEntity(
							this.xCoord + x, this.yCoord + y, this.zCoord + 7);
					if (hopefullyAMap instanceof TileEntityIslandMapper) 
					{
						TileEntityIslandMapper map = (TileEntityIslandMapper) hopefullyAMap;

						for (int b = 0; b < map.mapData.length; b++) 
						{
							int usByte = 0 | map.mapData[b];
							if (usByte < 64 || (usByte >= 128 && usByte < 192)) 
							{
								// Darken the map spot.
								map.mapData[b] = (byte) (usByte + 64);
							}
						}

						worldObj.markBlockForUpdate(map.xCoord, map.yCoord,
								map.zCoord);
					} 
					else 
					{
					}
				}
			}
		}
		
		
		if (lastScannedChunk != -1) //If it's "on",
		{
			int chunkX = lastScannedChunk % mapWidthChunks;
			int chunkZ = lastScannedChunk / mapWidthChunks;
			BiomeGenBase[] biomesForGeneration = null;

			for (int xInChunk = 0; xInChunk < 16; xInChunk += 2) 
			{
				int x = chunkX * 16 + xInChunk;
				for (int zInChunk = 0; zInChunk < 16; zInChunk += 2) 
				{
					int z = chunkZ * 16 + zInChunk;
					biomesForGeneration = this.worldObj.getWorldChunkManager()
							.loadBlockGeneratorData(biomesForGeneration, x, z,
									1, 1);
					if ((biomesForGeneration != null)
							&& (biomesForGeneration[0] != null)) 
					{
						int biomeID = biomesForGeneration[0].biomeID;
						int color = BiomeMapColors.biomeLookup[biomeID];

						if (getTaintAt(x, z)) 
						{
							color += 128;
						}

						if (lastScannedChunk == 0) 
						{
							System.out.println("Chunk five: Scanning " + x
									+ ", " + z + ": got a "
									+ (getTaintAt(x, z) ? "tainted " : "")
									+ biomesForGeneration[0].biomeName);
						}

						int newx = (mapWidthChunks * 16) - x - 1;

						int monitorXHeight = (newx / 2 / 176) - 2;
						int monitorYHeight = 5 - (z / 2 / 180);

						if (lastScannedChunk == 0) 
						{
							System.out.println("Putting it at "
									+ monitorXHeight + ", " + monitorYHeight);
						}

						Set<TileEntityIslandMapper> toUpdate = new HashSet<TileEntityIslandMapper>();

						TileEntity hopefullyAMap = worldObj.getTileEntity(
								this.xCoord + monitorXHeight, this.yCoord
										+ monitorYHeight, this.zCoord + 7);
						if (hopefullyAMap instanceof TileEntityIslandMapper) 
						{
							TileEntityIslandMapper map = (TileEntityIslandMapper) hopefullyAMap;

							int xPix = (newx / 2) % map.blockWidth;
							int yPix = (z / 2) % map.blockHeight;
							map.mapData[xPix + map.blockWidth * yPix] = (byte) color;

							worldObj.markBlockForUpdate(map.xCoord, map.yCoord,
									map.zCoord);
							toUpdate.add(map);
						} 
						else 
						{
							System.out.println("Danger! Entity at "
									+ (this.xCoord + monitorXHeight) + ", "
									+ (this.yCoord + monitorYHeight) + ", "
									+ (this.zCoord + 7)
									+ " is not a map, but instead a "
									+ hopefullyAMap + "!");
						}

						for (TileEntityIslandMapper map : toUpdate) 
						{
							worldObj.setBlockMetadataWithNotify(map.xCoord,
									map.yCoord, map.zCoord,
									worldObj.getBlockMetadata(map.xCoord,
											map.yCoord, map.zCoord) + 1, 2);
						}
					} 
					else 
					{
						System.out.println("Error!");
						System.out.println(biomesForGeneration != null);
						System.out.println(biomesForGeneration[0] != null);
					}
				}
			}
			lastScannedChunk++;
			if (lastScannedChunk == 110 * 135) 
			{
				lastScannedChunk = -1;
			}
		}
	}

	private boolean getTaintAt(int x, int z) 
	{
		for (int y = 0; y < 255; y++) 
		{
			if (worldObj.getBlock(x, y, z) == ConfigBlocks.blockTaint
					|| worldObj.getBlock(x + 1, y, z) == ConfigBlocks.blockTaint
					|| worldObj.getBlock(x, y, z + 1) == ConfigBlocks.blockTaint
					|| worldObj.getBlock(x + 1, y, z + 1) == ConfigBlocks.blockTaint
					|| worldObj.getBlock(x, y, z) == ConfigBlocks.blockTaintFibres
					|| worldObj.getBlock(x + 1, y, z) == ConfigBlocks.blockTaintFibres
					|| worldObj.getBlock(x, y, z + 1) == ConfigBlocks.blockTaintFibres
					|| worldObj.getBlock(x + 1, y, z + 1) == ConfigBlocks.blockTaintFibres) 
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void writeToNBT(NBTTagCompound par1) 
	{
		super.writeToNBT(par1);
		par1.setInteger("lastScannedChunk", lastScannedChunk);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1) 
	{
		super.readFromNBT(par1);
		lastScannedChunk = par1.getInteger("lastScannedChunk");
	}

	/**
	 * Called when you receive a TileEntityData packet for the location this
	 * TileEntity is currently in. On the client, the NetworkManager will always
	 * be the remote server. On the server, it will be whomever is responsible
	 * for sending the packet.
	 * 
	 * @param net
	 *            The NetworkManager the packet originated from
	 * @param pkt
	 *            The data packet
	 */
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) 
	{
		readFromNBT(pkt.func_148857_g());
	}
}
