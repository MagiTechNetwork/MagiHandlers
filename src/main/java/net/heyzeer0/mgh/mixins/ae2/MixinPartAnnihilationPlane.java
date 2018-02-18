package net.heyzeer0.mgh.mixins.ae2;

import appeng.parts.automation.PartAnnihilationPlane;
import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Frani on 09/02/2018.
 */
@Mixin(value = PartAnnihilationPlane.class, remap = false)
public abstract class MixinPartAnnihilationPlane {

    @Inject(method = "canHandleBlock", at = @At("HEAD"), cancellable = true)
    private void onCanHandleBlock(WorldServer w, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        IForgeTileEntity te = (IForgeTileEntity) ((PartAnnihilationPlane) (Object) this).getHost().getTile();
        if (te != null && te.hasMHPlayer()) {
            if (!MixinManager.canBuild(x, y, z, w, te.getMHPlayer())) {
                cir.setReturnValue(false);
            }
        }
    }

}
