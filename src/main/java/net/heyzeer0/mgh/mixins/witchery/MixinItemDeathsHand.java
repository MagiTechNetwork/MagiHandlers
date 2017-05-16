package net.heyzeer0.mgh.mixins.witchery;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by HeyZeer0 on 13/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "com/emoniph/witchery/item/ItemTaglockKit", remap = false)
public abstract class MixinItemDeathsHand {

    @Inject(method = "onLeftClickEntity", at = @At("HEAD"), cancellable = true)
    private void injectDamage(ItemStack stack, EntityPlayer player, Entity otherEntity, CallbackInfoReturnable ci) {
        if(!player.worldObj.isRemote && otherEntity instanceof EntityLivingBase) {
            AttackEntityEvent e = new AttackEntityEvent(player, otherEntity);
            MinecraftForge.EVENT_BUS.post(e);
            if(e.isCanceled()) {
                ci.cancel();
            }
        }
    }

}
