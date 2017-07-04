package net.heyzeer0.mgh.mixins.am2;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
@Mixin(targets = "am2/spell/components/Disarm", remap = false)
public abstract class MixinDisarm {

    @Inject(method = "applyEffectEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;dropOneItem(Z)Lnet/minecraft/entity/item/EntityItem;"), cancellable = true)
    private void injectApplyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target, CallbackInfoReturnable cir) {
        if(!MixinManager.canAttack((EntityPlayer)caster, target)) {
            cir.setReturnValue(false);
        }
    }

}
