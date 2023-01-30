package net.myclient.packetShit;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.Packet;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.myclient.events.ReceivedPacketListener;
import net.myclient.events.SentPacketListener;
import net.myclient.hack.Hack;
import net.myclient.mixin.CustomPayloadC2SPacketAccessor;
import net.myclient.mixin.CustomPayloadS2CPacketAccessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PacketLogger extends Hack implements SentPacketListener, ReceivedPacketListener {
    private static final Logger LOGGER = LogManager.getLogger("Packet Logger");

    private static Identifier getChannel(Packet<?> packet) {
        if (packet instanceof CustomPayloadC2SPacketAccessor) {
            return ((CustomPayloadC2SPacketAccessor) packet).getChannel();
        } else if (packet instanceof CustomPayloadS2CPacketAccessor) {
            return ((CustomPayloadS2CPacketAccessor) packet).getChannel();
        }
        return null;
    }

    @Override
    public void onReceivedPacket(ReceivedPacketEvent event) {
        assert event.getPacket() != null;
        Packet<?> packet = event.getPacket();
        Identifier channel = PacketLogger.getChannel(packet);
        if (channel != null) {
            LOGGER.info("Received packet with channel '{}'", channel);
            return;
        }

        LOGGER.info("Received packet with name '{}'", packet.getClass().getName());
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.sendMessage(Text.literal(packet.getClass().getName()));
    }

    @Override
    public void onSentPacket(SentPacketEvent event) {
        Packet<?> packet = event.getPacket();
        Identifier channel = PacketLogger.getChannel(packet);
        if (channel != null) {
            LOGGER.info("Sent packet with channel '{}'", channel);
            return;
        }

        LOGGER.info("Sent packet with name '{}'", packet.getClass().getName());
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.sendMessage(Text.literal(packet.getClass().getName()));
    }

    @Override
    public void onEnable()
    {
        EVENTS.add(SentPacketListener.class, this);
        EVENTS.add(ReceivedPacketListener.class, this);
    }
    @Override
    public void onDisable()
    {
        EVENTS.remove(SentPacketListener.class, this);
        EVENTS.remove(ReceivedPacketListener.class, this);
    }
}