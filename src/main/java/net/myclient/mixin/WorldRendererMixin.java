package net.myclient.mixin;

import net.minecraft.client.render.WorldRenderer;
import net.myclient.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;setupTerrain(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/Frustum;ZZ)V"), index = 3)
    private boolean modifySpectator(boolean isSpectator) {
        return Main.INSTANCE.getHackManager().freeCam.isEnabled() || isSpectator;
    }
}
