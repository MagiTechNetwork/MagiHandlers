package net.heyzeer0.mgh.mixins.ae2;

import appeng.api.networking.ticking.TickRateModulation;
import appeng.parts.automation.PartAnnihilationPlane;
import net.heyzeer0.mgh.MagiHandlers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Frani on 09/02/2018.
 */
@Mixin(value = PartAnnihilationPlane.class, remap = false)
public abstract class MixinPartAnnihilationPlane {

    @Inject(method = "breakBlock", at = @At("HEAD"))
    private void onBreakBlock(boolean modulate, CallbackInfoReturnable<TickRateModulation> cir) {
        MagiHandlers.getStack().push(((PartAnnihilationPlane) (Object) this).getHost().getTile());
    }

    @Inject(method = "breakBlock", at = @At("RETURN"))
    private void onBreakBlockReturn(boolean modulate, CallbackInfoReturnable<TickRateModulation> cir) {
        MagiHandlers.getStack().remove(((PartAnnihilationPlane) (Object) this).getHost().getTile());
    }

}
