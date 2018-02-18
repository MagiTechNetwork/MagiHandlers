package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import org.bukkit.event.Event;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.SimplePluginManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 18/02/2018.
 */
@Mixin(value = SimplePluginManager.class, remap = false)
public abstract class MixinSimplePluginManager {

    @Redirect(method = "fireEvent", at = @At(value = "INVOKE", target = "Lorg/bukkit/plugin/RegisteredListener;callEvent(Lorg/bukkit/event/Event;)V"))
    private void redirectFireEvent(RegisteredListener listener, Event event) throws Exception {
        final boolean isIgnoring = MagiHandlers.getStack().ignorePhase;
        MagiHandlers.getStack().ignorePhase = true;
        listener.callEvent(event);
        MagiHandlers.getStack().ignorePhase = isIgnoring;
    }

}
