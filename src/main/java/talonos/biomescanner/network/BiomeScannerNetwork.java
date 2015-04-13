package talonos.biomescanner.network;

import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import talonos.biomescanner.map.MapScanner;
import talonos.biomescanner.map.event.UpdateMapEvent;
import talonos.biomescanner.network.packets.BiomeScannerPacketBase;
import talonos.biomescanner.network.packets.UpdateMapPacket;

import java.util.EnumMap;

@ChannelHandler.Sharable
public class BiomeScannerNetwork extends FMLIndexedMessageToMessageCodec<BiomeScannerPacketBase> {
    private static final BiomeScannerNetwork INSTANCE = new BiomeScannerNetwork();
    private static final EnumMap<Side, FMLEmbeddedChannel> channels = Maps.newEnumMap(Side.class);

    public static void init() {
        if (!channels.isEmpty())
            return;

        INSTANCE.addDiscriminator(0, UpdateMapPacket.class);

        channels.putAll(NetworkRegistry.INSTANCE.newChannel("BiomeScanner", INSTANCE));

        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            MapScanner.instance.bus().register(INSTANCE);
            FMLCommonHandler.instance().bus().register(INSTANCE);
        }
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, BiomeScannerPacketBase msg, ByteBuf target) throws Exception {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        msg.write(out);
        target.writeBytes(out.toByteArray());
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, BiomeScannerPacketBase msg) {
        ByteArrayDataInput in = ByteStreams.newDataInput(source.array());

        in.skipBytes(1);
        msg.read(in);

        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            handleClient(msg);
        else
            handleServer(ctx, msg);
    }

    @SideOnly(Side.CLIENT)
    private void handleClient(BiomeScannerPacketBase msg) {
        msg.handleClient(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer);
    }

    private void handleServer(ChannelHandlerContext ctx, BiomeScannerPacketBase msg) {
        EntityPlayerMP player = ((NetHandlerPlayServer)ctx.channel().attr(NetworkRegistry.NET_HANDLER).get()).playerEntity;
        msg.handleServer(player.worldObj, player);
    }

    public static void sendToServer(BiomeScannerPacketBase packet) {
        channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channels.get(Side.CLIENT).writeAndFlush(packet);
    }

    public static void sendToAllPlayers(BiomeScannerPacketBase packet) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        channels.get(Side.SERVER).writeAndFlush(packet);
    }

    public static void sendToPlayer(BiomeScannerPacketBase packet, EntityPlayer player) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        channels.get(Side.SERVER).writeAndFlush(packet);
    }

    @SubscribeEvent
    public void onUpdateMap(UpdateMapEvent event) {
        int minX = event.getX();
        int minY = event.getY();
        int width = event.getWidth();
        int height = event.getHeight();

        byte[] data = new byte[width*height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[(y*width)+x] = MapScanner.instance.getRawColorByte(x+minX, y+minY);
            }
        }
        sendToAllPlayers(new UpdateMapPacket(minX, minY, width, height, data));
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        MapScanner.instance.sendEntireMap(event.player);
    }
}
