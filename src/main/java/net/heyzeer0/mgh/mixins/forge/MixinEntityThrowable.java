package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.events.ThrowableHitEntityEvent;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 14/10/2017.
 */

@Mixin(EntityThrowable.class)
public abstract class MixinEntityThrowable {

    @Invoker("onImpact")
    protected abstract void impact(MovingObjectPosition mop);

    @Redirect(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/EntityThrowable;onImpact(Lnet/minecraft/util/MovingObjectPosition;)V"))
    private void injectOnImpact(EntityThrowable instance, MovingObjectPosition mop) {
        ThrowableHitEntityEvent event = new ThrowableHitEntityEvent(instance, mop, null);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            ((MixinEntityThrowable)(Object)instance).impact(mop);
        }
    }

}
