package net.myclient.hacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.util.Hand;
import net.myclient.events.CaughtFishListener;
import net.myclient.events.ReceivedPacketListener;
import net.myclient.hack.Hack;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class AutoFish extends Hack implements ReceivedPacketListener, CaughtFishListener {
    private Logger LOGGER = LoggerFactory.getLogger("myclient");

    @Override
    public void onReceivedPacket(ReceivedPacketEvent event) {
        if (!(event.getPacket() instanceof EntitiesDestroyS2CPacket ap)) {return;}

        int id = ap.getEntityIds().getInt(0);
        assert MinecraftClient.getInstance().world != null;
        Entity entity = MinecraftClient.getInstance().world.getEntityById(id);
        if (entity instanceof FishingBobberEntity) {
            MinecraftClient client = MinecraftClient.getInstance();
            assert client.interactionManager != null;
            client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
        }
    }

    @Override
    public void onCaughtFish(Boolean caughtFish) {
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.interactionManager != null;
        client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
    }

    @Override
    public void onEnable() {
        EVENTS.add(ReceivedPacketListener.class, this);
        EVENTS.add(CaughtFishListener.class, this);
    }
    @Override
    public void onDisable() {
        EVENTS.remove(ReceivedPacketListener.class, this);
        EVENTS.remove(CaughtFishListener.class, this);
    }
}
