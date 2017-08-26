package net.heyzeer0.mgh.mixins.botania;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.rod.ItemGravityRod;

/**
 * Created by Frani on 04/07/2017.
 */

@Pseudo
@Mixin(targets = "vazkii/botania/common/item/rod/ItemGravityRod", remap = false)
public abstract class MixinItemGravityRod {

    @Redirect(method = "func_77659_a", at = @At(value = "INVOKE", target = "Lvazkii/botania/common/item/rod/ItemGravityRod;setEntityMotionFromVector(Lnet/minecraft/entity/Entity;Lvazkii/botania/common/core/helper/Vector3;F)V"))
    private void redirectSetEntityMotionFromVector(Entity entity, Vector3 originalPosVector, float modifier, ItemStack stack, World world, EntityPlayer player) {
        if(entity instanceof EntityLiving && !MixinManager.canAttack(player, entity)) {
            return;
        }
        ItemGravityRod.setEntityMotionFromVector(entity, originalPosVector, modifier);
    }

}
