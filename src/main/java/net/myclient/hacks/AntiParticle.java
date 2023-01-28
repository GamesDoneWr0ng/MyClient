package net.myclient.hacks;

import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.myclient.events.ReceivedPacketListener;
import net.myclient.hack.Hack;

public class AntiParticle extends Hack implements ReceivedPacketListener {
    @Override
    public void onReceivedPacket(ReceivedPacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof ParticleS2CPacket) {
            event.cancel();
        }
    }

    @Override
    public void onEnable() {
        EVENTS.add(ReceivedPacketListener.class, this);
    }
    @Override
    public void onDisable() {
        EVENTS.remove(ReceivedPacketListener.class, this);
    }
}
