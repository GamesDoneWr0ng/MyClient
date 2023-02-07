package net.myclient.hacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.myclient.events.ReceivedPacketListener;
import net.myclient.hack.Hack;

public class AntiVelocity extends Hack implements ReceivedPacketListener {
    @Override
    public void onReceivedPacket(ReceivedPacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof EntityVelocityUpdateS2CPacket ap && MinecraftClient.getInstance().player != null) {
            if (MinecraftClient.getInstance().player.getId() == ap.getId()) {
                event.cancel();
            }
        }
    }

    @Override
    public void onEnable() {EVENTS.add(ReceivedPacketListener.class, this);}

    @Override
    public void onDisable() {EVENTS.remove(ReceivedPacketListener.class, this);}
}
