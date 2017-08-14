package net.heyzeer0.mgh.mixins.mekanism;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 13/08/2017.
 */

@Pseudo
@Mixin(targets = "mekanism/common/entity/EntityRobit", remap = false)
public abstract class MixinEntityRobit {

    @Redirect(method = "collectItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;onItemPickup(Lnet/minecraft/entity/Entity;I)V", ordinal = 0))
    public void redirectPickup(EntityLivingBase instance, Entity item, int stack) {
        if(!item.isDead) {
            instance.onItemPickup(item, stack);
        }
    }

    @Redirect(method = "collectItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;onItemPickup(Lnet/minecraft/entity/Entity;I)V", ordinal = 1))
    public void redirectPickup2(EntityLivingBase instance, Entity item, int stack) {
        if(!item.isDead) {
            instance.onItemPickup(item, stack);
        }
    }

}
