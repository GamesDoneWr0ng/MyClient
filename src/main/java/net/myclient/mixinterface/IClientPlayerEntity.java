package net.myclient.mixinterface;

import net.minecraft.util.math.Vec3d;

public interface IClientPlayerEntity {
    void setNoClip(boolean noClip);

    float getLastYaw();

    float getLastPitch();

    void setMovementMultiplier(Vec3d movementMultiplier);
}