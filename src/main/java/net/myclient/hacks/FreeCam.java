package net.myclient.hacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.myclient.events.PlayerMoveListener;
import net.myclient.events.SentPacketListener;
import net.myclient.events.UpdateListener;
import net.myclient.hack.Hack;
import net.myclient.mixinterface.IClientPlayerEntity;
import net.myclient.settings.SliderSetting;

public class FreeCam extends Hack implements SentPacketListener, PlayerMoveListener, UpdateListener {
    private Vec3d pos;
    public final SliderSetting speed = new SliderSetting("Speed", 1.5, 0.01, 10, 0.01);
    public final SliderSetting sprintMultiplier = new SliderSetting("SprintMultiplier", 2.6, 0, 10, 0.01);

    @Override
    public void onUpdate() {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;
        assert player != null;

        double speed = (MinecraftClient.getInstance().options.sprintKey.isPressed() ? sprintMultiplier.getValue() : 1) * this.speed.getValue();
        double xVel = client.options.forwardKey.isPressed() ? 1 : client.options.backKey.isPressed() ? -1 : 0;
        double zVel = client.options.rightKey.isPressed() ? 1 : client.options.leftKey.isPressed() ? -1 : 0;
        double yVel = client.options.jumpKey.isPressed() ? 1 : client.options.sneakKey.isPressed() ? -1 : 0;

        Vec3d velocity = new Vec3d(xVel, yVel, zVel).rotateY((float) -Math.toRadians(player.getYaw() + 90)).normalize().multiply(speed);

        player.setVelocity(velocity);
    }

    @Override
    public void onSentPacket(SentPacketEvent event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket) {event.cancel();}
    }

    @Override
    public void onPlayerMove(IClientPlayerEntity player) {
        player.setNoClip(true);
    }

    @Override
    public void onEnable() {
        EVENTS.add(SentPacketListener.class, this);
        EVENTS.add(PlayerMoveListener.class, this);
        EVENTS.add(UpdateListener.class, this);

        assert MinecraftClient.getInstance().player != null;
        pos = MinecraftClient.getInstance().player.getPos();
    }

    @Override
    public void onDisable() {
        EVENTS.remove(SentPacketListener.class, this);
        EVENTS.remove(PlayerMoveListener.class, this);
        EVENTS.remove(UpdateListener.class, this);

        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.setPos(pos.getX(), pos.getY(), pos.getZ());
    }
}
