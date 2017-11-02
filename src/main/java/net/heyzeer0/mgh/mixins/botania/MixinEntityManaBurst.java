package net.heyzeer0.mgh.mixins.botania;

import net.heyzeer0.mgh.events.ThrowableHitEntityEvent;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.entity.EntityManaBurst;

/**
 * Created by HeyZeer0 on 01/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(EntityManaBurst.class)
public abstract class MixinEntityManaBurst extends EntityThrowable {

    @Shadow public abstract void setDead();

    public MixinEntityManaBurst(World world) {
        super(world);
    }

    @Inject(method = "onImpact", at = @At("HEAD"), cancellable = true)
    private void replaceImpact(MovingObjectPosition movingobjectposition, CallbackInfo ci) {
        ThrowableHitEntityEvent event = new ThrowableHitEntityEvent(this, movingobjectposition, getThrower());
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            setDead();
            ci.cancel();
        }
    }

}
