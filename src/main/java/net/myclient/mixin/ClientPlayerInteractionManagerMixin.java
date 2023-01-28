package net.myclient.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.myclient.event.EventManager;
import net.myclient.events.BlockBreakingProgressListener.BlockBreakingProgressEvent;
import net.myclient.mixinterface.IClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin implements IClientPlayerInteractionManager {
    @Final @Shadow private MinecraftClient client;
    @Shadow private float currentBreakingProgress;
    @Shadow private boolean breakingBlock;
    @Shadow private int blockBreakingCooldown;

    @Inject(at = {@At(value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;getId()I",
            ordinal = 0)},
            method = {
                    "updateBlockBreakingProgress(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"})
    private void onPlayerDamageBlock(BlockPos blockPos_1, Direction direction_1, CallbackInfoReturnable<Boolean> cir) {
        BlockBreakingProgressEvent event = new BlockBreakingProgressEvent(blockPos_1, direction_1);
        EventManager.fire(event);
    }

    @Override
    public float getCurrentBreakingProgress() {
        return currentBreakingProgress;
    }

    @Override
    public void setBreakingBlock(boolean breakingBlock) {
        this.breakingBlock = breakingBlock;
    }

    @Override
    public void rightClickItem() {
        interactItem(client.player, Hand.MAIN_HAND);
    }

    @Override
    public void rightClickBlock(BlockPos pos, Direction side, Vec3d hitVec) {
        interactBlock(client.player, Hand.MAIN_HAND,
        new BlockHitResult(hitVec, side, pos, false));
        interactItem(client.player, Hand.MAIN_HAND);
    }

    @Override
    public void setBlockHitDelay(int delay) {
        blockBreakingCooldown = delay;
    }

    @Shadow
    public abstract ActionResult interactItem(PlayerEntity playerEntity_1, Hand hand_1);

    @Shadow
    public abstract ActionResult interactBlock(
            ClientPlayerEntity clientPlayerEntity_1, Hand hand_1,
            BlockHitResult blockHitResult_1);
}
