package net.myclient.hacks;

import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.myclient.events.SentPacketListener;
import net.myclient.hack.Hack;
import net.myclient.util.PacketHelper;

public class AntiHunger extends Hack implements SentPacketListener {
    @Override
    public void onSentPacket(SentPacketEvent event) {
        if (!(event.getPacket() instanceof ClientCommandC2SPacket ap)) return;
        if (ap.getMode() == ClientCommandC2SPacket.Mode.START_SPRINTING) {
            event.cancel();
        }
    }

    @Override
    public void onEnable() {
        EVENTS.add(SentPacketListener.class, this);
        PacketHelper.stopSprinting();
    }
    @Override
    public void onDisable() {EVENTS.remove(SentPacketListener.class, this);}
}
