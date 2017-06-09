package net.heyzeer0.mgh.mixins.witchery;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by HeyZeer0 on 13/05/2017.
 * Copyright © HeyZeer0 - 2016
 */

@Pseudo
@Mixin(targets = "com/emoniph/witchery/util/EntityUtil", remap = false)
public abstract class MixinEntityUtils {

    @Inject(method = "instantDeath", at = @At("HEAD"), cancellable = true)
    private static void injectDeath(EntityLivingBase entity, EntityLivingBase attacker, CallbackInfo ci) {
        if(entity.isDead) {
            ci.cancel();
        }
    }

}
