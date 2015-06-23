package talonos.blightbuster.network.packets;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import talonos.blightbuster.network.BlightbusterNetwork;

import java.util.ArrayList;
import java.util.List;

public class AccumulatedPacket extends BlightbusterPacketBase {

    private List<BlightbusterPacketBase> accumulatedPackets = new ArrayList<BlightbusterPacketBase>(100);

    public AccumulatedPacket() {}

    @Override
    public void write(ByteArrayDataOutput out) {
        int size = accumulatedPackets.size();

        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            BlightbusterPacketBase packet = accumulatedPackets.get(i);
            out.writeInt(BlightbusterNetwork.getDescriminator(packet));
            packet.write(out);
        }
    }

    @Override
    public void read(ByteArrayDataInput in) {
        int count = in.readInt();

        for (int i = 0; i < count; i++) {
            BlightbusterPacketBase packet = BlightbusterNetwork.getPacket(in.readInt());
            packet.read(in);
            accumulatedPackets.add(packet);
        }
    }

    @Override
    public void handleClient(World world, EntityPlayer player) {
        for (int i = 0; i < accumulatedPackets.size(); i++) {
            accumulatedPackets.get(i).handleClient(world, player);
        }
    }

    @Override
    public void handleServer(World world, EntityPlayerMP player) {
        for (int i = 0; i < accumulatedPackets.size(); i++) {
            accumulatedPackets.get(i).handleServer(world, player);
        }
    }

    public void addPacket(BlightbusterPacketBase packet) {
        accumulatedPackets.add(packet);
    }
}
