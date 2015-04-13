package talonos.biomescanner.network.packets;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public abstract class BiomeScannerPacketBase {
    public abstract void write(ByteArrayDataOutput out);
    public abstract void read(ByteArrayDataInput in);

    @SideOnly(Side.CLIENT)
    public abstract void handleClient(World world, EntityPlayer player);

    public abstract void handleServer(World world, EntityPlayerMP player);
}
