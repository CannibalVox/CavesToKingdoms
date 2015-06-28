package talonos.blightbuster.network.handlers;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import talonos.blightbuster.BlightBuster;
import talonos.blightbuster.network.packets.SpawnCleanseParticlesPacket;

import java.util.Random;

public class SpawnCleanseParticleHandler implements IMessageHandler<SpawnCleanseParticlesPacket, IMessage> {
    @Override
    public IMessage onMessage(SpawnCleanseParticlesPacket message, MessageContext ctx) {
        //System.out.println("Trying to spawn particles:");

        double height = BlightBuster.proxy.getBestCleanseSpawnHeight();
        Random r = new Random();
        for (int y = 0; y < 50; y++)
        {
            double d1 = r.nextGaussian() * 0.02D;
            double d2 = r.nextGaussian() * 0.02D;
            Minecraft.getMinecraft().theWorld.spawnParticle(message.doUseFlameParticles() ? "flame" : "smoke", message.getSpawnX() + r.nextDouble(), height - 5 + (r.nextDouble() * 15), message.getSpawnZ() + r.nextDouble(), 0, d1, d2);
        }

        return null;
    }
}
