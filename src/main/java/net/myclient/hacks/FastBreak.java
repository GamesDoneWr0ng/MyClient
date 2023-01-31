package net.myclient.hacks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.myclient.events.BlockBreakingProgressListener;
import net.myclient.events.SentPacketListener;
import net.myclient.events.UpdateListener;
import net.myclient.hack.Hack;
import net.myclient.mixin.ClientConnectionInvoker;
import net.myclient.mixin.ClientWorldInvoker;
import net.myclient.mixinterface.IClientPlayerInteractionManager;
import net.myclient.mixinterface.IMinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action.*;

public class FastBreak extends Hack implements UpdateListener, SentPacketListener, BlockBreakingProgressListener {
    private PlayerActionC2SPacket toRemove;
    private static final Logger LOGGER = LoggerFactory.getLogger("FastBreak");
    private final IMinecraftClient IClient = (IMinecraftClient) MinecraftClient.getInstance();
    private int ticks = 0;

    @Override
    public void onSentPacket(SentPacketEvent event) {
        if (!(event.getPacket() instanceof PlayerActionC2SPacket ap)) {return;}

        if (ap.getAction() == START_DESTROY_BLOCK) {
            if (toRemove == null) {
                ClientWorldInvoker clientWorld = (ClientWorldInvoker) MinecraftClient.getInstance().world;

                assert MinecraftClient.getInstance().world != null;
                BlockState blockState = MinecraftClient.getInstance().world.getBlockState(ap.getPos());
                PlayerEntity player = MinecraftClient.getInstance().player;

                assert player != null;
                if (blockState.calcBlockBreakingDelta(player, player.world, ap.getPos()) * ticks++ >= 0.7) {
                    try (PendingUpdateManager pendingUpdateManager = clientWorld._getPendingUpdateManager().incrementSequence()) {
                        toRemove = new PlayerActionC2SPacket(STOP_DESTROY_BLOCK, ap.getPos(), ap.getDirection(), pendingUpdateManager.getSequence());
                        ticks = 0;
                    }
                }
            }

        } else if (ap.getAction() == ABORT_DESTROY_BLOCK) {
            event.cancel();
            return;
        }

        LOGGER.info("{}, {}, {}, {}", ap.getAction(), ap.getPos(), ap.getDirection(), ap.getSequence());
    }

    @Override
    public void onUpdate() {
        IClient.getInteractionManager().setBlockHitDelay(0);

        if (toRemove == null) {return;}

        BlockPos pos = toRemove.getPos();
        assert MinecraftClient.getInstance().player != null;
        assert MinecraftClient.getInstance().world != null;
        if (!MinecraftClient.getInstance().world.canPlayerModifyAt(MinecraftClient.getInstance().player, pos)) {
            return;
        }


        BlockState blockState = MinecraftClient.getInstance().world.getBlockState(pos);

        if (blockState != Blocks.AIR.getDefaultState()) {
            ClientConnectionInvoker connection = (ClientConnectionInvoker) MinecraftClient.getInstance().player.networkHandler.getConnection();
            connection._sendImmediately(toRemove, null);
        }
        toRemove = null;
    }

    @Override
    public void onBlockBreakingProgress(BlockBreakingProgressEvent event)
    {
        IClientPlayerInteractionManager im = IClient.getInteractionManager();

        if(im.getCurrentBreakingProgress() >= 1)
            return;

        BlockPos blockPos = event.getBlockPos();
        Direction direction = event.getDirection();
        ClientWorldInvoker clientWorld = (ClientWorldInvoker) MinecraftClient.getInstance().world;
        assert clientWorld != null;
        assert MinecraftClient.getInstance().player != null;
        try (PendingUpdateManager pendingUpdateManager = clientWorld._getPendingUpdateManager().incrementSequence()) {
            PlayerActionC2SPacket finishPacket = new PlayerActionC2SPacket(STOP_DESTROY_BLOCK, blockPos, direction, pendingUpdateManager.getSequence());
            ClientConnectionInvoker connection = (ClientConnectionInvoker) MinecraftClient.getInstance().player.networkHandler.getConnection();
            connection._sendImmediately(finishPacket, null);
        }
    }

    @Override
    public void onEnable() {
        EVENTS.add(UpdateListener.class, this);
        EVENTS.add(SentPacketListener.class, this);
        EVENTS.add(BlockBreakingProgressListener.class, this);
    }
    @Override
    public void onDisable() {
        EVENTS.remove(UpdateListener.class, this);
        EVENTS.remove(SentPacketListener.class, this);
        EVENTS.remove(BlockBreakingProgressListener.class, this);
    }
}
