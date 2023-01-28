package net.myclient.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.myclient.mixinterface.IClientPlayerEntity;
import net.myclient.mixinterface.IClientPlayerInteractionManager;
import net.myclient.mixinterface.IMinecraftClient;
import net.myclient.mixinterface.IWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements IMinecraftClient {
    @Shadow @Nullable public ClientWorld world;
    @Shadow @Nullable public ClientPlayerEntity player;
    @Shadow private int itemUseCooldown;
    @Shadow public ClientPlayerInteractionManager interactionManager;

    @Override
    public void rightClick() {
        doItemUse();
    }

    @Override
    public void setItemUseCooldown(int itemUseCooldown) {
        this.itemUseCooldown = itemUseCooldown;
    }

    @Override
    public IClientPlayerInteractionManager getInteractionManager() {
        return (IClientPlayerInteractionManager) interactionManager;
    }

    @Override
    public int getItemUseCooldown() {
        return itemUseCooldown;
    }

    @Override
    public IClientPlayerEntity getPlayer() {
        return (IClientPlayerEntity) player;
    }

    @Override
    public IWorld getWorld() {
        return (IWorld) world;
    }

    @Shadow
    private void doItemUse() {}
}
