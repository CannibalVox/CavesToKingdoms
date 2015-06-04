package talonos.blightbuster;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.biome.BiomeGenBase;

public class DawnTotemEntity extends TileEntity 
{
	public long lastWorldTick = 0;
	public long offset = -1;

	public boolean canUpdate()
	{
		return true;
	}

	public void updateEntity()
	{
		super.updateEntity();

		if (this.lastWorldTick == 0)
		{
			this.lastWorldTick = worldObj.getWorldTime();
		}

		if (this.offset < 0)
			this.offset	= worldObj.rand.nextInt(20);

		long currentWorldTick = worldObj.getWorldTime();

		int cleansedSquares = 0;
		while (cleansedSquares < 5 && lastWorldTick < currentWorldTick) {
			lastWorldTick++;
			if ((lastWorldTick + offset) % 20 == 0) {
				cleansedSquares++;
				cleanseSquare();
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		lastWorldTick = tag.getLong("LastWorldTick");
		offset = tag.getLong("TickOffset");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);

		tag.setLong("LastWorldTick", lastWorldTick);
		tag.setLong("TickOffset", offset);
	}

	protected void cleanseSquare() {
		int centerX = this.worldObj.rand.nextInt(51)-25;
		int centerZ = this.worldObj.rand.nextInt(51)-25;

		if (!this.worldObj.isRemote)
		{
			for (int x = centerX-1; x < centerX+2; x++)
			{
				for (int z = centerZ-1; z < centerZ+2; z++)
				{
					if ((this.worldObj.getBiomeGenForCoords(x + this.xCoord, z + this.zCoord).biomeID == Config.biomeTaintID) ||
							(this.worldObj.getBiomeGenForCoords(x + this.xCoord, z + this.zCoord).biomeID == Config.biomeEerieID) ||
							(this.worldObj.getBiomeGenForCoords(x + this.xCoord, z + this.zCoord).biomeID == Config.biomeMagicalForestID))
					{
						BiomeGenBase[] biomesForGeneration = null;
						biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, x + this.xCoord, z + this.zCoord, 1, 1);
						if ((biomesForGeneration != null) && (biomesForGeneration[0] != null))
						{
							BiomeGenBase biome = biomesForGeneration[0];
							Utils.setBiomeAt(this.worldObj, x + this.xCoord, z + this.zCoord, biome);
						}
					}
				}
			}
		}
		else
		{
			//System.out.println("Trying to spawn particles:");
			Random r = new Random();
			for (int y = 0; y < 1000; y++)
			{
				double d0 = r.nextGaussian() * 0.02D;
				double d1 = r.nextGaussian() * 0.02D;
				double d2 = r.nextGaussian() * 0.02D;
				worldObj.spawnParticle("smoke", this.xCoord+centerX+r.nextDouble(), r.nextDouble()*255, this.zCoord+centerZ+r.nextDouble(), 0, d1,d2);
			}
		}
	}
}
