package net.heyzeer0.mgh.mixins.am2;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 04/07/2017.
 */

@Pseudo
@Mixin(targets = "am2/spell/components/Storm", remap = false)
public abstract class MixinStorm {

    @Inject(method = "applyEffect", at = @At("HEAD"), cancellable = true)
    private void injectApplyEffect(EntityLivingBase caster, World world, CallbackInfo ci) {
        ci.cancel();
    }

}
