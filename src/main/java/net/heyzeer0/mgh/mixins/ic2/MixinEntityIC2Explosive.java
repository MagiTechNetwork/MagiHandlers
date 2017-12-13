package net.heyzeer0.mgh.mixins.ic2;

import ic2.core.ExplosionIC2;
import ic2.core.block.EntityIC2Explosive;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ExplosionEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 13/12/2017.
 */
@Pseudo
@Mixin(value = EntityIC2Explosive.class, remap = false)
public abstract class MixinEntityIC2Explosive {

    @Redirect(method = "explode", at = @At(value = "INVOKE", target = "Lic2/core/ExplosionIC2;doExplosion()V"))
    private void onExplode(ExplosionIC2 explosion) {
        ExplosionEvent.Start event = new ExplosionEvent.Start(explosion.exploder.worldObj, explosion);
        if (!MinecraftForge.EVENT_BUS.post(event)) explosion.doExplosion();
    }

}
