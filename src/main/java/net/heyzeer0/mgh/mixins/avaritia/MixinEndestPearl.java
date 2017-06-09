package net.heyzeer0.mgh.mixins.avaritia;

import net.minecraft.util.MovingObjectPosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "fox/spiteful/avaritia/entity/EntityEndestPearl", remap = false)
public abstract class MixinEndestPearl {
    @Inject(method = "func_70184_a", at = @At("HEAD"), cancellable = true)
    private void injectImpact(MovingObjectPosition mop, CallbackInfo ci) {
        ci.cancel();
    }
}
