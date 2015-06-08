package talonos.blightbuster.network.packets;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import talonos.blightbuster.BlightBuster;

import java.util.Random;

public class SpawnCleanseParticlesPacket extends BlightbusterPacketBase {

    private int spawnX;
    private int spawnZ;

    public SpawnCleanseParticlesPacket() {}
    public SpawnCleanseParticlesPacket(int x, int z) {
        this.spawnX = x;
        this.spawnZ = z;
    }

    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeInt(spawnX);
        out.writeInt(spawnZ);
    }

    @Override
    public void read(ByteArrayDataInput in) {
        spawnX = in.readInt();
        spawnZ = in.readInt();
    }

    @Override
    public void handleClient(World world, EntityPlayer player) {
        //System.out.println("Trying to spawn particles:");

        double height = BlightBuster.proxy.getBestCleanseSpawnHeight();
        Random r = new Random();
        for (int y = 0; y < 50; y++)
        {
            double d1 = r.nextGaussian() * 0.02D;
            double d2 = r.nextGaussian() * 0.02D;
            world.spawnParticle("smoke", spawnX+r.nextDouble(), height - 5 + (r.nextDouble()*15), spawnZ+r.nextDouble(), 0, d1,d2);
        }
    }

    @Override
    public void handleServer(World world, EntityPlayerMP player) {
        //We shouldn't be here
    }
}
