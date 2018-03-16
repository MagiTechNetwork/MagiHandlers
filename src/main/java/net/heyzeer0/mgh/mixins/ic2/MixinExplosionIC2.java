package net.heyzeer0.mgh.mixins.ic2;

import ic2.core.ExplosionIC2;
import net.heyzeer0.mgh.api.forge.ForgeStack;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Frani on 16/03/2018.
 */
@Mixin(value = ExplosionIC2.class, remap = false)
public abstract class MixinExplosionIC2 {

    @Redirect(method = "doExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesWithinAABBExcludingEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;"))
    private List<Entity> onGetEntitiesExcluding(World world, Entity entity, AxisAlignedBB bb) {
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(entity, bb);
        return list.stream().filter(e -> {
            if (!(e instanceof EntityLiving)) return false;
            EntityPlayer player = ForgeStack.getStack().getCurrentEntityPlayer().orElse(null);
            if (player != null) {
                return MixinManager.canAttack(player, e);
            }
            return true;
        }).collect(Collectors.toList());
    }

    @Redirect(method = "doExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;"))
    private List<Entity> onGetEntities(World world, Class entityClass, AxisAlignedBB bb) {
        List<Entity> list = world.getEntitiesWithinAABB(entityClass, bb);
        return list.stream().filter(e -> {
            if (!(e instanceof EntityLiving)) return false;
            EntityPlayer player = ForgeStack.getStack().getCurrentEntityPlayer().orElse(null);
            if (player != null) {
                return MixinManager.canAttack(player, e);
            }
            return true;
        }).collect(Collectors.toList());
    }

    @Inject(method = "damageEntities", at = @At(value = "INVOKE", target = "Lic2/core/util/Util;square(D)D"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void onDamageEntities(double x, double y, double z, int step, double power, CallbackInfo ci, int i, Object entry, Entity entity, int index, int distanceMax) {
        ForgeStack.getStack().getCurrentEntityPlayer().ifPresent(p -> {
            if (!MixinManager.canAttack(p, entity)) ci.cancel();
        });
    }

}
