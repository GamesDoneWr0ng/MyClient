package net.myclient.hacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.myclient.events.SentPacketListener;
import net.myclient.events.UpdateListener;
import net.myclient.hack.Hack;
import net.myclient.settings.SliderSetting;
import net.myclient.util.PacketHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fly extends Hack implements UpdateListener, SentPacketListener {
    private int counter = 70;
    private boolean hijackNext = false;
    private static final Logger LOGGER = LoggerFactory.getLogger("Fly");

    public final SliderSetting speed = new SliderSetting("Speed", 0.1, 0.01, 1, 0.01);
    public final SliderSetting sprintMultiplier = new SliderSetting("SprintMultiplier", 4, 0, 10, 0.01);

    @Override
    public void onSentPacket(SentPacketEvent event) {
        if (!(event.getPacket() instanceof PlayerMoveC2SPacket)) return;

        if (hijackNext) {
            hijackNext = false;
            PlayerMoveC2SPacket ap = (PlayerMoveC2SPacket) event.getPacket();
            hijack(ap);
            event.cancel();
        }
    }

    @Override
    public void onUpdate() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;

        player.getAbilities().allowFlying = true;

        double speed = (MinecraftClient.getInstance().options.sprintKey.isPressed() ? sprintMultiplier.getValue() : 1) * this.speed.getValue();
        player.getAbilities().setFlySpeed((float) speed);

        if (player.isOnGround()) return;
        if (player.getVelocity().getY() < -0.032) {counter = 50;}

        if (--counter < 0) {
            hijackNext = true;
            counter = 50;
        }
    }

    private void hijack(PlayerMoveC2SPacket ap) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        assert MinecraftClient.getInstance().player != null;

        PacketHelper.sendPosition(new Vec3d(ap.getX(player.getX()), player.prevY - 0.032, ap.getZ(player.getZ())));
    }

    @Override
    public void onEnable() {
        EVENTS.add(UpdateListener.class, this);
        EVENTS.add(SentPacketListener.class, this);
    }
    @Override
    public void onDisable() {
        EVENTS.remove(UpdateListener.class, this);
        EVENTS.remove(SentPacketListener.class, this);
    }
}
