package net.myclient.mixinterface;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public interface IClientPlayerInteractionManager
{
    float getCurrentBreakingProgress();
    void setBreakingBlock(boolean breakingBlock);
    void rightClickItem();
    void rightClickBlock(BlockPos pos, Direction side, Vec3d hitVec);
    void setBlockHitDelay(int delay);
}