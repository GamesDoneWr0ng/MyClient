package net.myclient.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import net.myclient.Main;
import net.myclient.event.EventManager;
import net.myclient.events.PlayerMoveListener.PlayerMoveEvent;
import net.myclient.events.UpdateListener;
import net.myclient.hack.HackManager;
import net.myclient.mixinterface.IClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements IClientPlayerEntity {
    @Shadow private float lastYaw;
    @Shadow private float lastPitch;
    private final HackManager hackManager = Main.INSTANCE.getHackManager();

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "pushOutOfBlocks", at = @At(value = "HEAD"), cancellable = true)
    private void pushOutOfBlocks(CallbackInfo ci) {
        if (hackManager.freeCam.isEnabled()) {
            ci.cancel();
        }
    }

    @Inject(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V",
            ordinal = 0), method = "tick()V")
    private void onTick(CallbackInfo ci) {
        EventManager.fire(UpdateListener.UpdateEvent.INSTANCE);
    }

    @Inject(at = @At("HEAD"), method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V")
    private void onMove(MovementType type, Vec3d offset, CallbackInfo ci) {
        PlayerMoveEvent event = new PlayerMoveEvent(this);
        EventManager.fire(event);
    }

    @Override
    public void setNoClip(boolean noClip) {
        this.noClip = noClip;
    }

    @Override
    public float getLastYaw() {
        return lastYaw;
    }

    @Override
    public float getLastPitch() {
        return lastPitch;
    }

    @Override
    public void setMovementMultiplier(Vec3d movementMultiplier) {
        this.movementMultiplier = movementMultiplier;
    }
}
