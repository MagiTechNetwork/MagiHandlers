package net.heyzeer0.mgh.mixins.ae2;

import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.data.IAEItemStack;
import appeng.parts.automation.PartFormationPlane;
import net.heyzeer0.mgh.MagiHandlers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Frani on 09/02/2018.
 */
@Mixin(value = PartFormationPlane.class, remap = false)
public abstract class MixinPartFormationPlane {

    @Inject(method = "injectItems", at = @At("HEAD"))
    private void onInjectHead(IAEItemStack input, Actionable type, BaseActionSource src, CallbackInfoReturnable<IAEItemStack> cir) {
        MagiHandlers.getStack().push(((PartFormationPlane) (Object) this).getTile());
    }

    @Inject(method = "injectItems", at = @At("RETURN"))
    private void onInjectReturn(IAEItemStack input, Actionable type, BaseActionSource src, CallbackInfoReturnable<IAEItemStack> cir) {
        MagiHandlers.getStack().remove(((PartFormationPlane) (Object) this).getTile());
    }

}
