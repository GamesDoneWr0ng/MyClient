package net.myclient.hacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.myclient.events.SentPacketListener;
import net.myclient.hack.Hack;
import net.myclient.util.TargetedEntityFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
            assert player != null;
            Vec3d viewDir = getViewDirection(player);
            List<Entity> entities = StreamSupport.stream(MinecraftClient.getInstance().world.getEntities().spliterator(), false).toList();
            TargetedEntityFilter filter = new TargetedEntityFilter(aimAssist, player.getPos(), viewDir);
            List<Entity> filteredEntities = entities.stream().filter(filter::test).toList();
            Entity closest = getClosestEntity(filteredEntities, player.getPos(), viewDir);

            LOGGER.info("Entity: {}", closest);
        }
    }

    public Vec3d getViewDirection(PlayerEntity player) {
        float pitch = player.getPitch();
        float yaw = player.getYaw();
        float x = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float z = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float y = -MathHelper.cos(-pitch * 0.017453292F);
        return new Vec3d(x, y, z);
    }

    public Entity getClosestEntity(List<Entity> entities, Vec3d playerPos, Vec3d viewDir) {
        Entity closest = null;
        double minDistance = Double.MAX_VALUE;
        for (Entity entity : entities) {
            Vec3d entityPos = entity.getPos();
            Vec3d rayToEntity = entityPos.subtract(playerPos);
            double distanceAlongRay = rayToEntity.dotProduct(viewDir);
            Vec3d closestPoint = playerPos.add(viewDir.multiply(distanceAlongRay));
            double distanceToClosestPoint = entityPos.distanceTo(closestPoint);
            if (distanceToClosestPoint < minDistance) {
                closest = entity;
                minDistance = distanceToClosestPoint;
            }
        }
        return closest;
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
