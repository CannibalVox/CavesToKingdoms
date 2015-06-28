package talonos.blightbuster.network.packets;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import talonos.blightbuster.BlightBuster;

import java.util.Random;

public class SpawnCleanseParticlesPacket implements IMessage {

    private int spawnX;
    private int spawnZ;
    private boolean useFlameParticles;

    public SpawnCleanseParticlesPacket() {}
    public SpawnCleanseParticlesPacket(int x, int z, boolean useFlameParticles) {
        this.spawnX = x;
        this.spawnZ = z;
        this.useFlameParticles = useFlameParticles;
    }

    @Override
    public void fromBytes(ByteBuf in) {
        spawnX = in.readInt();
        spawnZ = in.readInt();
        useFlameParticles = in.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf out) {
        out.writeInt(spawnX);
        out.writeInt(spawnZ);
        out.writeBoolean(useFlameParticles);
    }

    public int getSpawnX() { return spawnX; }
    public int getSpawnZ() { return spawnZ; }
    public boolean doUseFlameParticles() { return useFlameParticles; }
}
