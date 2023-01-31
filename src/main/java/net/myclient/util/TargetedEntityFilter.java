package net.myclient.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class TargetedEntityFilter {
    private final double degrees;
    private final Vec3d playerPos;
    private final Vec3d viewDir;

    public TargetedEntityFilter(double degrees, Vec3d playerPos, Vec3d viewDir) {
        this.degrees = degrees;
        this.playerPos = playerPos;
        this.viewDir = viewDir;
    }

    public boolean test(Entity entity) {
        Vec3d relativePos = entity.getPos().subtract(playerPos);
        double angle = relativePos.normalize().dotProduct(viewDir);
        double maxAngle = Math.cos(Math.toRadians(degrees / 2));
        return angle >= maxAngle;
    }
}