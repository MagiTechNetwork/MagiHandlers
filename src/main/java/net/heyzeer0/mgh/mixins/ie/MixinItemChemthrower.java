package net.heyzeer0.mgh.mixins.ie;

import blusunrize.immersiveengineering.common.items.ItemChemthrower;
import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.api.forge.IForgeEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 17/03/2018.
 */
@Mixin(value = ItemChemthrower.class, remap = false)
public abstract class MixinItemChemthrower {

    @Redirect(method = "onUsingTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntityInWorld(Lnet/minecraft/entity/Entity;)Z"))
    private boolean redirectSpawnEntity(World world, Entity entity) {
        boolean r = world.spawnEntityInWorld(entity);
        MagiHandlers.log("chemtrower entity owner: " + ((IForgeEntity) entity).getMHOwner().getCommandSenderName());
        return r;
    }

}
