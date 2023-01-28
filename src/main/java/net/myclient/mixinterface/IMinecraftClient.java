package net.myclient.mixinterface;

public interface IMinecraftClient
{
    void rightClick();
    void setItemUseCooldown(int itemUseCooldown);
    IClientPlayerInteractionManager getInteractionManager();
    int getItemUseCooldown();
    IClientPlayerEntity getPlayer();
    IWorld getWorld();
}