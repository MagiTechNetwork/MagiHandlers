package net.heyzeer0.mgh.mixins.ie;

import blusunrize.immersiveengineering.common.entities.EntityIEProjectile;
import net.heyzeer0.mgh.events.ThrowableHitEntityEvent;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 18/03/2018.
 */
@Mixin(value = EntityIEProjectile.class, remap = false)
public abstract class MixinEntityIEProjectile {

    private boolean shouldCancel = false;

    @Redirect(method = "func_70071_h_", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/common/entities/EntityIEProjectile;onImpact(Lnet/minecraft/util/MovingObjectPosition;)V"))
    private void impact(EntityIEProjectile projectile, MovingObjectPosition mop) {
        ThrowableHitEntityEvent event = new ThrowableHitEntityEvent(projectile, mop, null);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            shouldCancel = true;
            projectile.setDead();
        } else {
            projectile.onImpact(mop);
        }
    }

    @Inject(method = "func_70071_h_", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/common/entities/EntityIEProjectile;onImpact(Lnet/minecraft/util/MovingObjectPosition;)V", shift = At.Shift.AFTER), cancellable = true)
    private void returnImpact(CallbackInfo ci) {
        if (shouldCancel) ci.cancel();
    }
}
