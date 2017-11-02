package net.heyzeer0.mgh.mixins.botania;

import net.heyzeer0.mgh.hacks.IEntityThrowable;
import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.block.tile.mana.TileSpreader;

/**
 * Created by Frani on 02/11/2017.
 */

@Pseudo
@Mixin(TileSpreader.class)
public abstract class MixinTileSpreader implements ITileEntityOwnable {

    @Redirect(method = "tryShootBurst", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntityInWorld(Lnet/minecraft/entity/Entity;)Z"))
    private boolean onSpawnEntity(World world, Entity entity) {
        if (entity instanceof EntityThrowable) ((IEntityThrowable)entity).setThrower(getFakePlayer());
        return world.spawnEntityInWorld(entity);
    }

}
