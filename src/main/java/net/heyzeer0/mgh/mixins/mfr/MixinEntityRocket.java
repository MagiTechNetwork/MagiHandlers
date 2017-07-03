package net.heyzeer0.mgh.mixins.mfr;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 02/07/2017.
 */

@Pseudo
@Mixin(targets = "powercrystals/minefactoryreloaded/entity/EntityRocket", remap = false)
public abstract class MixinEntityRocket extends Entity {

    public MixinEntityRocket(World world) {
        super(world);
    }

    @Shadow
    EntityLivingBase owner;

    @Redirect(method = "func_70071_h_", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;newExplosion(Lnet/minecraft/entity/Entity;DDDFZZ)Lnet/minecraft/world/Explosion;"))
    private void injectExplosion(Entity entity, double posX, double posY, double posZ, float size, boolean smoking, boolean explode) {
        worldObj.newExplosion(entity, posX, posY, posZ, 4.0F, true, true).exploder = owner;
    }

}
