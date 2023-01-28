package net.myclient.events;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.myclient.event.Event;
import net.myclient.event.Listener;

import java.util.ArrayList;

public interface BlockBreakingProgressListener extends Listener {
    void onBlockBreakingProgress(BlockBreakingProgressEvent event);

    class BlockBreakingProgressEvent extends Event<BlockBreakingProgressListener> {
        private final BlockPos blockPos;
        private final Direction direction;

        public BlockBreakingProgressEvent(BlockPos blockPos, Direction direction) {
            this.blockPos = blockPos;
            this.direction = direction;
        }

        @Override
        public void fire(ArrayList<BlockBreakingProgressListener> listeners)
        {
            for(BlockBreakingProgressListener listener : listeners)
                listener.onBlockBreakingProgress(this);
        }

        @Override
        public Class<BlockBreakingProgressListener> getListenerType() {return BlockBreakingProgressListener.class;}
        public BlockPos getBlockPos() {return blockPos;}
        public Direction getDirection() {return direction;}
    }
}
