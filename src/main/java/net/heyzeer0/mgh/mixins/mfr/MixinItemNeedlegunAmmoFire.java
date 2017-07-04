package net.heyzeer0.mgh.mixins.mfr;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.entity.Entity;
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
 * Created by Frani on 02/07/2017.
 */

@Pseudo
@Mixin(targets = "powercrystals/minefactoryreloaded/item/gun/ammo/ItemNeedlegunAmmoFire", remap = false)
public abstract class MixinItemNeedlegunAmmoFire {

    @Inject(method = "onHitEntity", at = @At("HEAD"), cancellable = true)
    private void injectHitEntity(ItemStack stack, EntityPlayer owner, Entity hit, double distance, CallbackInfoReturnable cir) {
        if(!MixinManager.canAttack(owner, hit)) {
            cir.setReturnValue(false);
        }
    }

}
