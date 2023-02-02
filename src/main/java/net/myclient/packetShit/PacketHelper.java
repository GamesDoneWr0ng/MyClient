package net.myclient.packetShit;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.myclient.mixin.ClientConnectionInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketHelper {
    //private static Logger LOGGER = LoggerFactory.getLogger("myclient");
    public static void sendPosition(Vec3d pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.player != null;
        ClientConnectionInvoker connection = (ClientConnectionInvoker) client.player.networkHandler.getConnection();
        PlayerMoveC2SPacket.PositionAndOnGround packet = new PlayerMoveC2SPacket.PositionAndOnGround(pos.getX(), pos.getY(), pos.getZ(), client.player.isOnGround());
        connection._sendImmediately(packet, null);
    }

    public static void attack(Entity entity) {
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.player != null;
        ClientConnectionInvoker connection = (ClientConnectionInvoker) client.player.networkHandler.getConnection();
        PlayerInteractEntityC2SPacket packet = PlayerInteractEntityC2SPacket.attack(entity,false);
        connection._sendImmediately(packet, null);
    }
}
