package net.heyzeer0.mgh.mixins.iguanatt;

import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by HeyZeer0 on 10/09/2017.
 * Copyright © HeyZeer0 - 2016
 */

@Pseudo
@Mixin(targets = "iguanaman/iguanatweakstconstruct/leveling/handlers/LevelingEventHandler", remap = false)
public abstract class MixinLevelingEventHandler {

    @Inject(method = "onHurt", at = @At("HEAD"), cancellable = true)
    public void onHurt(LivingHurtEvent e, CallbackInfo ci) {
        if(e.isCanceled()) {
            ci.cancel();
        }
    }

}
