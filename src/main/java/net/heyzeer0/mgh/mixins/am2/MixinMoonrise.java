package net.heyzeer0.mgh.mixins.am2;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Frani on 04/07/2017.
 */

@Pseudo
@Mixin(targets = "am2/spell/components/Moonrise", remap = false)
public abstract class MixinMoonrise {

    @Inject(method = "setNightTime", at = @At("HEAD"), cancellable = true)
    private void injectSetNightTime(World world, CallbackInfoReturnable cir) {
        cir.setReturnValue(false);
    }

}
