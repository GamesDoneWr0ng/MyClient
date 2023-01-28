package net.myclient.mixin;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.myclient.Main;
import net.myclient.event.EventManager;
import net.myclient.events.CaughtFishListener.CaughtFishEvent;
import net.myclient.hack.HackManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin {
    @Shadow private boolean caughtFish;

    @Inject(method = "onTrackedDataSet", at = @At("TAIL"))
    public void onTrackedDataSet(TrackedData<?> data, CallbackInfo ci) {
        CaughtFishEvent event = new CaughtFishEvent(caughtFish);
        EventManager.fire(event);
    }
}
