package net.myclient.hacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.myclient.events.SentPacketListener;
import net.myclient.hack.Hack;
import net.myclient.mixin.ClientConnectionInvoker;
import net.myclient.util.PacketHelper;

public class NoFall extends Hack implements SentPacketListener {
    @Override
    public void onSentPacket(SentPacketEvent event) {
        if (!(event.getPacket() instanceof PlayerMoveC2SPacket ap)) return;

        if (!ap.isOnGround()) {
            PacketHelper.modifyOnGround(ap);
            event.cancel();
        }
    }

    @Override
    public void onEnable() {EVENTS.add(SentPacketListener.class, this);}
    @Override
    public void onDisable() {EVENTS.remove(SentPacketListener.class, this);}
}
