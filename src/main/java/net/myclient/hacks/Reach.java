package net.myclient.hacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.myclient.events.SentPacketListener;
import net.myclient.hack.Hack;
import net.myclient.util.PacketHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Reach extends Hack implements SentPacketListener {
    private Packet<?> lastpacket;
    private double aimAssist = Math.cos(Math.toRadians(5));
    private double maxTeleportDistance = 9;
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
            ClientWorld world = (ClientWorld) player.world;
            HitResult hit = player.raycast(1000, MinecraftClient.getInstance().getTickDelta(),false);
            Entity closest = getEntityFromRay(world, player, hit.squaredDistanceTo(player));

            if (hit.getType() == HitResult.Type.BLOCK) {
                if (closest == null) {
                    teleport(player.getPos(), hit.getPos());
                    PacketHelper.mine(new BlockPos(hit.getPos()));
                    teleport(hit.getPos(), player.getPos());
                    return;
                }
            }

            teleport(player.getPos(), closest.getPos());
            PacketHelper.attack(closest);
            teleport(closest.getPos(), player.getPos());
        }
    }

    private Entity getEntityFromRay(World world, PlayerEntity player, double range) {
        Vec3d playerPos = player.getEyePos();
        Vec3d lookVec = player.getRotationVec(1.0F);

        List<Entity> entities = world.getEntitiesByClass(Entity.class, player.getBoundingBox().expand(lookVec.x * range, lookVec.y * range, lookVec.z * range).expand(1.0D), entity -> !entity.isSpectator());

        Entity closestEntity = null;
        double closestAngle = aimAssist;

        for (Entity entity : entities) {
            if (entity == player) continue;
            if (!entity.isAttackable()) continue;
            Vec3d entityPos = entity.getPos();
            Vec3d relativePos = entityPos.subtract(playerPos);

            Vec3d closestPoint = playerPos.add(lookVec.multiply(lookVec.dotProduct(relativePos)));

            Box bb = entity.getBoundingBox().offset(playerPos.negate());
            double x = MathHelper.clamp(closestPoint.x, bb.minX, bb.maxX);
            double y = MathHelper.clamp(closestPoint.y, bb.minY, bb.maxY);
            double z = MathHelper.clamp(closestPoint.z, bb.minZ, bb.maxZ);
            closestPoint = new Vec3d(x,y,z);

            double angle = lookVec.normalize().dotProduct(closestPoint.normalize());
            if (angle > closestAngle) {
                closestAngle = angle;
                closestEntity = entity;
            }
        }
        return closestEntity;
    }

    private void teleport(Vec3d from, Vec3d to) {
        to = to.add(from.subtract(to).normalize().multiply(2));
        double distance = from.squaredDistanceTo(to);
        while (distance > (maxTeleportDistance * maxTeleportDistance)) {
            Vec3d nextPosition = from.add(to.subtract(from).normalize().multiply(maxTeleportDistance));
            PacketHelper.sendPosition(nextPosition);
            from = nextPosition;
            distance = from.squaredDistanceTo(to);
        }
        PacketHelper.sendPosition(to);
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
