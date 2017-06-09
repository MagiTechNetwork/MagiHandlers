package net.heyzeer0.mgh.mixins.bloodmagic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by HeyZeer0 on 09/06/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "WayofTime/alchemicalWizardry/common/items/SacrificialDagger", remap = false)
public abstract class MixinSacrificialDagger {

    @Inject(method = "func_77659_a", at = @At("HEAD"), cancellable = true)
    private void injectDamage(ItemStack stack, World world, EntityPlayer player, CallbackInfoReturnable ci) {
        if(player != null) {
            if(player.isDead || player.getHealth() <= 0) {
                ci.cancel();
            }
        }
    }

}
