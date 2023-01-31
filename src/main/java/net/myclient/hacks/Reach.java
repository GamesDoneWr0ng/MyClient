package net.myclient.hacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.myclient.events.SentPacketListener;
import net.myclient.hack.Hack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Reach extends Hack implements SentPacketListener {
    private Packet<?> lastpacket;
    private final float aimAssist = 5;
    private final Logger LOGGER = LoggerFactory.getLogger("Reach");
    @Override
    public void onSentPacket(SentPacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (lastpacket instanceof PlayerInteractEntityC2SPacket || lastpacket instanceof PlayerActionC2SPacket) {
            lastpacket = null;
            return;
        }
        lastpacket = packet;

        if (packet instanceof HandSwingC2SPacket) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            ClientWorld world = MinecraftClient.getInstance().world;
            assert player != null;
            Entity closest = getEntityFromRay(world, player, 50);

            LOGGER.info("Entity: {}", closest);
        }
    }

    public static Entity getEntityFromRay(World world, PlayerEntity player, double range) {
        Vec3d playerPos = player.getEyePos();
        Vec3d lookVec = player.getRotationVec(1.0F);
        Vec3d endPos = playerPos.add(lookVec.multiply(range));

        List<Entity> entities = world.getEntitiesByClass(Entity.class, player.getBoundingBox().expand(lookVec.x * range, lookVec.y * range, lookVec.z * range).expand(1.0D), entity -> !entity.isSpectator());

        Entity closestEntity = null;
        double closestDistance = Double.MAX_VALUE;

        for (Entity entity : entities) {
            Vec3d entityPos = entity.getPos();
            Vec3d rayVec = entityPos.subtract(playerPos);

            if (lookVec.normalize().dotProduct(rayVec.normalize()) > Math.cos(Math.toRadians(5))) {
                double distance = rayVec.lengthSquared();
                if (distance < closestDistance) {
                    closestEntity = entity;
                    closestDistance = distance;
                }
            }
        }
        return closestEntity;
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
