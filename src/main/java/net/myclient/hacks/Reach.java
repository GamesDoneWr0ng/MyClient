package net.myclient.hacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.myclient.events.SentPacketListener;
import net.myclient.hack.Hack;
import net.myclient.settings.SliderSetting;
import net.myclient.settings.CycleSetting;
import net.myclient.util.PacketHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Reach extends Hack implements SentPacketListener {
    private Packet<?> lastpacket;
    public final SliderSetting maxTpDistance = new SliderSetting("MaxTpDistance", 9, 1, 10, 0.5);
    public final SliderSetting aimAssist = new SliderSetting("AimAssist", 5, 0, 90, 0.1);
    public final CycleSetting target = new CycleSetting("Target", new String[]{"Mobs", "Players", "All"}, "All");
    public final CycleSetting mobTarget = new CycleSetting("MobTarget", new String[]{"Hostile", "Passive", "All"}, "All");

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

            if (closest == null) {
                if (hit.getType() == HitResult.Type.BLOCK) {
                    teleport(player.getPos(), hit.getPos());
                    PacketHelper.mine(new BlockPos(hit.getPos()));
                    teleport(hit.getPos(), player.getPos());
                    return;
                } else if (hit.getType() == HitResult.Type.MISS) {
                    return;
                } else {
                    closest = ((EntityHitResult) hit).getEntity();
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
        double closestAngle = Math.cos(Math.toRadians(aimAssist.getValue()));

        for (Entity entity : entities) {
            if (entity == player) continue;
            if (!entity.isAttackable()) continue;
            if (!entity.isAlive()) continue;

            if (entity instanceof PlayerEntity == target.getValue().equals("Mobs")) continue;
            if (entity instanceof PassiveEntity == mobTarget.getValue().equals("Hostile")) continue;

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
        while (distance > (maxTpDistance.getValue() * maxTpDistance.getValue())) {
            Vec3d nextPosition = from.add(to.subtract(from).normalize().multiply(maxTpDistance.getValue()));
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
