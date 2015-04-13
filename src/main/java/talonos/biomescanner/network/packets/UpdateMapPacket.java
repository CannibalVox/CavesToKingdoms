package talonos.biomescanner.network.packets;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import talonos.biomescanner.map.MapScanner;

public class UpdateMapPacket extends BiomeScannerPacketBase {

    private int mapX;
    private int mapY;
    private int updateWidth;
    private int updateHeight;

    private byte[] updateData;

    public UpdateMapPacket(int mapX, int mapY, int width, int height, byte[] data) {
        this.mapX = mapX;
        this.mapY = mapY;
        this.updateWidth = width;
        this.updateHeight = height;
        this.updateData = data;
    }

    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeInt(mapX);
        out.writeInt(mapY);
        out.writeInt(updateWidth);
        out.writeInt(updateHeight);
        out.write(updateData);
    }

    @Override
    public void read(ByteArrayDataInput in) {
        this.mapX = in.readInt();
        this.mapY = in.readInt();
        this.updateWidth = in.readInt();
        this.updateHeight = in.readInt();
        this.updateData = new byte[updateWidth * updateHeight];
        in.readFully(this.updateData, 0, updateData.length);
    }

    @Override
    public void handleClient(World world, EntityPlayer player) {
        MapScanner.instance.updateFromNetwork(mapX, mapY, updateWidth, updateHeight, updateData);
    }

    @Override
    public void handleServer(World world, EntityPlayerMP player) {
        //Shouldn't be here
    }
}
