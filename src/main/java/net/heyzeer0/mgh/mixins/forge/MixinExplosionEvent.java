package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.api.forge.ForgeStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 13/12/2017.
 */
@Mixin(ExplosionEvent.class)
public abstract class MixinExplosionEvent {

    @Shadow @Final public Explosion explosion;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConctructEvent(World world, Explosion exp, CallbackInfo ci) {
        Entity exploder = exp.exploder;
        if (exploder != null && exploder instanceof EntityPlayer) return;
        ForgeStack.getStack().getCurrentEntityPlayer().ifPresent(p -> explosion.exploder = p);
    }

}
