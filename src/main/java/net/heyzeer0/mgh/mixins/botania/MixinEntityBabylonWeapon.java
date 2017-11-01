package net.heyzeer0.mgh.mixins.botania;

import net.minecraft.entity.Entity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.entity.EntityBabylonWeapon;
import vazkii.botania.common.entity.EntityThrowableCopy;

/**
 * Created by Frani on 31/10/2017.
 */
@Pseudo
@Mixin(value = EntityBabylonWeapon.class)
public abstract class MixinEntityBabylonWeapon extends EntityThrowableCopy {

    public MixinEntityBabylonWeapon(World w) {
        super(w);
    }

    @Redirect(method = "onImpact", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFZ)Lnet/minecraft/world/Explosion;"))
    private Explosion replaceCreateExplosion(World world, Entity entity, double x, double y, double z, float strenght, boolean destroy) {
        Explosion explosion = world.createExplosion(entity, x, y, z, strenght, destroy);
        explosion.exploder = getThrower();
        return explosion;
    }

}
