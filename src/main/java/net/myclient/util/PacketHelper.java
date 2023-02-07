package net.myclient.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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

    public static void mine(BlockPos pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.player != null;
        ClientConnectionInvoker connection = (ClientConnectionInvoker) client.player.networkHandler.getConnection();
        PlayerActionC2SPacket packet = new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, Direction.DOWN);
        connection._sendImmediately(packet, null);
    }

    public static void modifyOnGround(PlayerMoveC2SPacket ap) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        assert MinecraftClient.getInstance().player != null;
        ClientConnectionInvoker connection = (ClientConnectionInvoker) MinecraftClient.getInstance().player.networkHandler.getConnection();

        PlayerMoveC2SPacket.Full groundPacket = new PlayerMoveC2SPacket.Full(ap.getX(player.getX()), ap.getY(player.getY()), ap.getZ(player.getZ()), ap.getYaw(player.getYaw()), ap.getPitch(player.getPitch()), true);
        connection._sendImmediately(groundPacket, null);
    }

    public static void stopSprinting() {
        assert MinecraftClient.getInstance().player != null;
        ClientConnectionInvoker connection = (ClientConnectionInvoker) MinecraftClient.getInstance().player.networkHandler.getConnection();
        ClientCommandC2SPacket packet = new ClientCommandC2SPacket(MinecraftClient.getInstance().player, ClientCommandC2SPacket.Mode.STOP_SPRINTING);
        connection._sendImmediately(packet, null);
    }
}
