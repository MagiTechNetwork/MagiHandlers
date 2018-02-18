package net.heyzeer0.mgh.mixins.ae2;

import appeng.hooks.TickHandler;
import appeng.util.IWorldCallable;
import net.heyzeer0.mgh.MagiHandlers;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 18/02/2018.
 */
@Mixin(value = TickHandler.class, remap = false)
public abstract class MixinTickHandler {

    @Redirect(method = "processQueue", at = @At(value = "INVOKE", target = "Lappeng/util/IWorldCallable;call(Lnet/minecraft/world/World;)Ljava/lang/Object;"))
    private Object redirectCall(IWorldCallable c, World world) throws Exception {
        final boolean isIgnoring = MagiHandlers.getStack().ignorePhase;
        MagiHandlers.getStack().ignorePhase = true;
        Object r = c.call(world);
        MagiHandlers.getStack().ignorePhase = isIgnoring;
        return r;
    }

}
