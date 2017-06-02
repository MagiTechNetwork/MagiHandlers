package net.heyzeer0.mgh.mixins.extrautilities;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by HeyZeer0 on 02/06/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "com/rwtema/extrautils/gui/ContainerFilter", remap = false)
public class MixinContainerFilter {

    @Inject(method = "func_75144_a", at = @At("HEAD"), cancellable = true)
    private void injectClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer, CallbackInfoReturnable ci) {
        if(par3 == 2) {
            ci.cancel();
        }
    }

}
