package talonos.blightbuster.network.packets;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import talonos.blightbuster.network.BlightbusterNetwork;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.utils.Utils;

public class BiomeChangePacket extends BlightbusterPacketBase {
    private int x;
    private int z;
    private short biomeID;

    public BiomeChangePacket() {}
    public BiomeChangePacket(int x, int z, short biomeID) {
        this.x = x;
        this.z = z;
        this.biomeID = biomeID;
    }

    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeInt(x);
        out.writeInt(z);
        out.writeShort(biomeID);
    }

    @Override
    public void read(ByteArrayDataInput in) {
        x = in.readInt();
        z = in.readInt();
        biomeID = in.readShort();
    }

    @Override
    public void handleClient(World world, EntityPlayer player) {
        BlightbusterNetwork.setBiomeAt(Thaumcraft.proxy.getClientWorld(), x, z, BiomeGenBase.getBiome(biomeID));
    }

    @Override
    public void handleServer(World world, EntityPlayerMP player) {

    }
}
