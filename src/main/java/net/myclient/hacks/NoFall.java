package net.myclient.hacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.myclient.events.SentPacketListener;
import net.myclient.hack.Hack;
import net.myclient.mixin.ClientConnectionInvoker;

public class NoFall extends Hack implements SentPacketListener {
    public static void modifyOnGround(PlayerMoveC2SPacket ap) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        assert MinecraftClient.getInstance().player != null;
        ClientConnectionInvoker connection = (ClientConnectionInvoker) MinecraftClient.getInstance().player.networkHandler.getConnection();

        PlayerMoveC2SPacket.Full groundPacket = new PlayerMoveC2SPacket.Full(ap.getX(player.getX()), ap.getY(player.getY()), ap.getZ(player.getZ()), ap.getYaw(player.getYaw()), ap.getPitch(player.getPitch()), true);
        connection._sendImmediately(groundPacket, null);
    }

    @Override
    public void onSentPacket(SentPacketEvent event) {
        if (!(event.getPacket() instanceof PlayerMoveC2SPacket ap)) return;

        if (!ap.isOnGround()) {
            modifyOnGround(ap);
            event.cancel();
        }
    }

    @Override
    public void onEnable() {EVENTS.add(SentPacketListener.class, this);}
    @Override
    public void onDisable() {EVENTS.remove(SentPacketListener.class, this);}
}
