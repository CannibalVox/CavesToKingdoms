package talonos.blightbuster.network;

import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
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
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import talonos.biomescanner.map.MapScanner;
import talonos.biomescanner.map.event.UpdateMapEvent;
import talonos.blightbuster.network.packets.*;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketBiomeChange;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

@ChannelHandler.Sharable
public class BlightbusterNetwork extends FMLIndexedMessageToMessageCodec<BlightbusterPacketBase> {
    private static final BlightbusterNetwork INSTANCE = new BlightbusterNetwork();
    private static final EnumMap<Side, FMLEmbeddedChannel> channels = Maps.newEnumMap(Side.class);

    private List<BlightbusterPacketBase> accumulatedPackets = new ArrayList<BlightbusterPacketBase>(100);
    private HashMap<Integer, Class> discriminatorMap = new HashMap<Integer, Class>();
    private HashMap<Class, Integer> reverseDiscriminatorMap = new HashMap<Class, Integer>();

    public static void init() {
        if (!channels.isEmpty())
            return;

        INSTANCE.addDiscriminator(0, SpawnCleanseParticlesPacket.class);
        INSTANCE.addDiscriminator(1, UpdateMapPacket.class);
        INSTANCE.addDiscriminator(2, BiomeChangePacket.class);
        INSTANCE.addDiscriminator(3, AccumulatedPacket.class);

        channels.putAll(NetworkRegistry.INSTANCE.newChannel("Blightbuster", INSTANCE));

        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            MapScanner.instance.bus().register(INSTANCE);
        }

        FMLCommonHandler.instance().bus().register(INSTANCE);
    }

    @Override
    public FMLIndexedMessageToMessageCodec<BlightbusterPacketBase> addDiscriminator(int discriminator, Class cls) {
        discriminatorMap.put(discriminator, cls);
        reverseDiscriminatorMap.put(cls, discriminator);

        return super.addDiscriminator(discriminator, cls);
    }

    public static BlightbusterPacketBase getPacket(int discriminator) {
        Class cls = INSTANCE.discriminatorMap.get(discriminator);
        try {
            return (BlightbusterPacketBase)cls.newInstance();
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Cannot instantiate class "+cls.getName(), ex);
        } catch (InstantiationException ex) {
            throw new RuntimeException("Cannot instantiate class "+cls.getName(), ex);
        }
    }

    public static int getDescriminator(BlightbusterPacketBase packet) {
        return INSTANCE.reverseDiscriminatorMap.get(packet.getClass());
    }

    @SubscribeEvent
    public void endOfTick(TickEvent.WorldTickEvent event) {
        if (event.world.provider.dimensionId != 0 || event.phase != TickEvent.Phase.END)
            return;

        long time = event.world.getWorldTime();

        if (time % 5 == 0 && accumulatedPackets.size() > 0) {
            flushAccumulatedPackets();
        }
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

    private void flushAccumulatedPackets() {
        int packetCount = accumulatedPackets.size();
        if (packetCount == 0)
            return;

        AccumulatedPacket packet = new AccumulatedPacket();
        for (int i = 0; i < packetCount; i++) {
            packet.addPacket(accumulatedPackets.get(i));
        }

        accumulatedPackets.clear();

        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        channels.get(Side.SERVER).writeAndFlush(packet);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, BlightbusterPacketBase msg, ByteBuf target) throws Exception {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        msg.write(out);
        target.writeBytes(out.toByteArray());
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, BlightbusterPacketBase msg) {
        ByteArrayDataInput in = ByteStreams.newDataInput(source.array());

        in.skipBytes(1);
        msg.read(in);

        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            handleClient(msg);
        else
            handleServer(ctx, msg);
    }

    @SideOnly(Side.CLIENT)
    private void handleClient(BlightbusterPacketBase msg) {
        msg.handleClient(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer);
    }

    private void handleServer(ChannelHandlerContext ctx, BlightbusterPacketBase msg) {
        EntityPlayerMP player = ((NetHandlerPlayServer)ctx.channel().attr(NetworkRegistry.NET_HANDLER).get()).playerEntity;
        msg.handleServer(player.worldObj, player);
    }

    public static void sendToServer(BlightbusterPacketBase packet) {
        channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channels.get(Side.CLIENT).writeAndFlush(packet);
    }

    public static void sendToAllPlayers(BlightbusterPacketBase packet) {
        INSTANCE.accumulatedPackets.add(packet);
        if (INSTANCE.accumulatedPackets.size() >= 100)
            INSTANCE.flushAccumulatedPackets();
    }

    public static void sendToPlayer(BlightbusterPacketBase packet, EntityPlayer player) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        channels.get(Side.SERVER).writeAndFlush(packet);
    }

    public static void sendToNearbyPlayers(BlightbusterPacketBase packet, int dim, double x, double y, double z, double range) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(new NetworkRegistry.TargetPoint(dim, x, y, z, range));
        channels.get(Side.SERVER).writeAndFlush(packet);
    }

    public static void setBiomeAt(World world, int x, int z, BiomeGenBase biome) {
        if(biome != null) {
            if (!world.isRemote || world.getChunkProvider().chunkExists(x >> 4, z >> 4)) {
                Chunk chunk = world.getChunkFromBlockCoords(x, z);
                byte[] array = chunk.getBiomeArray();
                array[(z & 15) << 4 | x & 15] = (byte) (biome.biomeID & 255);
                chunk.setBiomeArray(array);
            }

            if(!world.isRemote) {
                sendToAllPlayers(new BiomeChangePacket(x, z, (short) biome.biomeID));
            }
        }
    }
}
