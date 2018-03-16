package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import org.bukkit.craftbukkit.v1_7_R4.scheduler.CraftTask;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 15/03/2018.
 */
@Mixin(value = CraftTask.class, remap = false)
public abstract class MixinCraftTask {

    @Redirect(method = "run", at = @At(value = "INVOKE", target = "Ljava/lang/Runnable;run()V"))
    private void onRun(Runnable instance) {
        final boolean isIgnoring = MagiHandlers.getStack().ignorePhase;
        MagiHandlers.getStack().ignorePhase = true;
        instance.run();
        MagiHandlers.getStack().ignorePhase = isIgnoring;
    }

}
