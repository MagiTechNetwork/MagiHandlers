package net.heyzeer0.mgh.mixins.mfr;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Created by Frani on 02/07/2017.
 */

@Pseudo
@Mixin(targets = "powercrystals/minefactoryreloaded/item/gun/ammo/ItemNeedlegunAmmoStandard", remap = false)
public abstract class MixinItemNeedlegunAmmoStandard {

    @Shadow
    protected int damage;

    @Overwrite
    public boolean onHitEntity(ItemStack stack, EntityPlayer owner, Entity hit, double distance) {
        if(!MixinManager.canAttack(owner, hit)) return false;
        hit.attackEntityFrom(DamageSource.causePlayerDamage(owner).setProjectile(), damage);
        return true;
    }

}
