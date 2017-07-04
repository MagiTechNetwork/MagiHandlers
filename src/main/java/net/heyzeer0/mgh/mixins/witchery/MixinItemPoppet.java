package net.heyzeer0.mgh.mixins.witchery;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Created by HeyZeer0 on 04/07/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "com/emoniph/witchery/item/ItemPoppet", remap = false)
public abstract class MixinItemPoppet {

    @Inject(method = "func_77615_a", at = @At(value = "INVOKE", target = "Lcom/emoniph/witchery/entity/EntityWitchHunter;blackMagicPerformed(Lnet/minecraft/entity/player/EntityPlayer;)V"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void injectStoppedUse(ItemStack itemstack, World world, EntityPlayer player, int ticks, CallbackInfo ci, EntityLivingBase entity) {
        if(!MixinManager.canAttack(player, entity)) {
            ci.cancel();
        }
        if(entity instanceof EntityPlayer) {
            if(!MixinManager.canAttack((EntityPlayer)entity, player)) {
                ci.cancel();
            }
        }
    }

}
