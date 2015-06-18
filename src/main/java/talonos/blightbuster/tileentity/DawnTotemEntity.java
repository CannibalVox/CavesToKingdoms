package talonos.blightbuster.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.biome.BiomeGenBase;
import talonos.blightbuster.network.BlightbusterNetwork;
import talonos.blightbuster.network.packets.SpawnCleanseParticlesPacket;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.utils.Utils;

public class DawnTotemEntity extends TileEntity {
    public long lastWorldTick = 0;
    public long offset = -1;

    public boolean canUpdate() {
        return true;
    }

    public void updateEntity() {
        super.updateEntity();

        if (this.lastWorldTick == 0) {
            this.lastWorldTick = worldObj.getWorldTime();
        }

        if (this.offset < 0)
            this.offset = worldObj.rand.nextInt(20);

        long currentWorldTick = worldObj.getWorldTime();

        int cleansedSquares = 0;
        while (cleansedSquares < 5 && lastWorldTick < currentWorldTick) {
            lastWorldTick++;
            if ((lastWorldTick + offset) % 20 == 0) {
                cleansedSquares++;
                cleanseSquare(currentWorldTick == lastWorldTick);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
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

    protected void cleanseSquare(boolean sendParticleSpawn) {
        if (this.worldObj.isRemote)
            return;

        int centerX = this.worldObj.rand.nextInt(51) - 25;
        int centerZ = this.worldObj.rand.nextInt(51) - 25;

        if (sendParticleSpawn) {
            int absolutePointX = centerX + this.xCoord;
            int absolutePointZ = centerZ + this.zCoord;
            BlightbusterNetwork.sendToNearbyPlayers(new SpawnCleanseParticlesPacket(absolutePointX, absolutePointZ, false), worldObj.provider.dimensionId, absolutePointX, 128.0f, absolutePointZ, 150);
        }

        for (int x = centerX - 1; x < centerX + 2; x++) {
            for (int z = centerZ - 1; z < centerZ + 2; z++) {
                if ((this.worldObj.getBiomeGenForCoords(x + this.xCoord, z + this.zCoord).biomeID == Config.biomeTaintID) ||
                        (this.worldObj.getBiomeGenForCoords(x + this.xCoord, z + this.zCoord).biomeID == Config.biomeEerieID) ||
                        (this.worldObj.getBiomeGenForCoords(x + this.xCoord, z + this.zCoord).biomeID == Config.biomeMagicalForestID)) {
                    BiomeGenBase[] biomesForGeneration = null;
                    biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, x + this.xCoord, z + this.zCoord, 1, 1);
                    if ((biomesForGeneration != null) && (biomesForGeneration[0] != null)) {
                        BiomeGenBase biome = biomesForGeneration[0];
                        Utils.setBiomeAt(this.worldObj, x + this.xCoord, z + this.zCoord, biome);
                    }
                }
            }
        }
    }
}
