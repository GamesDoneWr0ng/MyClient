package net.myclient.mixinterface;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public interface IWorld
{
    Stream<VoxelShape> getBlockCollisionsStream(@Nullable Entity entity, Box box);

    default Stream<Box> getCollidingBoxes(@Nullable Entity entity, Box box)
    {
        return getBlockCollisionsStream(entity, box)
                .flatMap(vs -> vs.getBoundingBoxes().stream())
                .filter(b -> b.intersects(box));
    }
}