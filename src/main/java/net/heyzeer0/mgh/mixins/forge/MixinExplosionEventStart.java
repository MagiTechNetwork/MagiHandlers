package net.heyzeer0.mgh.mixins.forge;

import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 02/11/2017.
 */

@Mixin(ExplosionEvent.Start.class)
public abstract class MixinExplosionEventStart extends ExplosionEvent {

    public MixinExplosionEventStart(World w, Explosion e) {
        super(w, e);
    }

    @Shadow private ExplosionPrimeEvent event;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstruct(World world, Explosion explosion, CallbackInfo ci) {
        if (event.isCancelled()) {
            this.setCanceled(true);
            return;
        }
    }

}
