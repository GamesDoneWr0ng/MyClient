package net.myclient.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.myclient.mixinterface.IWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Mixin(World.class)
public abstract class WorldMixin implements IWorld, WorldAccess {
    @Override
    public Stream<VoxelShape> getBlockCollisionsStream(@Nullable Entity entity, Box box) {
        return StreamSupport.stream(getBlockCollisions(entity, box).spliterator(), false);
    }
}
