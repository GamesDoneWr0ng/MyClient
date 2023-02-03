package net.myclient.hacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.myclient.events.UpdateListener;
import net.myclient.hack.Hack;
import net.myclient.packetShit.PacketHelper;

import java.util.List;

public class KillAura extends Hack implements UpdateListener {
    float cooldown = 1;
    @Override
    public void onUpdate() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;

        cooldown = (float) (cooldown + player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED) / 20);
        if (cooldown < 1) return;

        World world = player.world;
        List<Entity> entities = world.getOtherEntities(player, player.getBoundingBox().expand(3));
        List<PlayerEntity> players = null;
        Entity closestEntity = null;
        double closestAngle = -1;

        Vec3d playerPos = player.getEyePos();
        Vec3d lookVec = player.getRotationVec(1.0f);

        for (Entity entity : entities) {
            if (!entity.isAlive()) continue;
            if (!entity.isAttackable()) continue;
            if (entity instanceof PlayerEntity) {
                players.add((PlayerEntity) entity);
                closestAngle = -1;
            } else if (players != null) continue;

            Vec3d entityPos = entity.getPos();
            Vec3d relativePos = entityPos.subtract(playerPos);

            Vec3d closestPoint = playerPos.add(lookVec.multiply(lookVec.dotProduct(relativePos)));

            double[] pointArray = new double[]{closestPoint.x, closestPoint.y, closestPoint.z};
            Box bb = entity.getBoundingBox().offset(playerPos.negate());
            double[] maxB = new double[]{bb.maxX, bb.maxY, bb.maxZ};
            double[] minB = new double[]{bb.minX, bb.minY, bb.minZ};

            for (int i = 0; i < pointArray.length; i++) {
                pointArray[i] = Math.max(minB[i], Math.min(maxB[i], pointArray[i]));
            }

            closestPoint = new Vec3d(pointArray[0], pointArray[1], pointArray[2]);

            if (closestPoint.squaredDistanceTo(Vec3d.ZERO) > 25) continue;

            double angle = lookVec.normalize().dotProduct(closestPoint.normalize());
            if (angle > closestAngle) {
                closestAngle = angle;
                closestEntity = entity;
            }
        }

        if (closestEntity != null) {
            PacketHelper.attack(closestEntity);
            player.swingHand(Hand.MAIN_HAND);
            cooldown = 0;
        }
    }

    @Override
    public void onEnable() {
        EVENTS.add(UpdateListener.class, this);
    }

    @Override
    public void onDisable() {
        EVENTS.remove(UpdateListener.class, this);
    }
}
