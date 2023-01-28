package net.myclient.hacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.myclient.events.SentPacketListener;
import net.myclient.hack.Hack;
import net.myclient.mixin.ClientConnectionInvoker;

public class AntiHunger extends Hack implements SentPacketListener {

    @Override
    public void onSentPacket(SentPacketEvent event) {
        if (!(event.getPacket() instanceof ClientCommandC2SPacket ap)) {return;}
        if (ap.getMode() == ClientCommandC2SPacket.Mode.START_SPRINTING) {
            event.cancel();
        }
    }

    @Override
    public void onEnable() {
        EVENTS.add(SentPacketListener.class, this);
        assert MinecraftClient.getInstance().player != null;
        ClientConnectionInvoker connection = (ClientConnectionInvoker) MinecraftClient.getInstance().player.networkHandler.getConnection();
        ClientCommandC2SPacket packet = new ClientCommandC2SPacket(MinecraftClient.getInstance().player, ClientCommandC2SPacket.Mode.STOP_SPRINTING);
        connection._sendImmediately(packet, null);
    }
    @Override
    public void onDisable() {EVENTS.remove(SentPacketListener.class, this);}
}
