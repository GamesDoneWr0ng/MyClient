package net.myclient.hacks;

import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.myclient.events.SentPacketListener;
import net.myclient.hack.Hack;

public class Reach extends Hack implements SentPacketListener {
    private Packet<?> lastpacket;
    private final float aimAssist = 5;
    @Override
    public void onSentPacket(SentPacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (lastpacket instanceof PlayerInteractEntityC2SPacket || lastpacket instanceof PlayerActionC2SPacket) {return;}
        lastpacket = packet;

        if (packet instanceof HandSwingC2SPacket) {
            
        }
    }

    @Override
    public void onEnable() {
        EVENTS.add(SentPacketListener.class, this);
    }

    @Override
    public void onDisable() {
        EVENTS.remove(SentPacketListener.class, this);
    }
}
