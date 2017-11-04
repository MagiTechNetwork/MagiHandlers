package net.heyzeer0.mgh.mixins.forge;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventBus;
import net.heyzeer0.mgh.MagiHandlers;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Frani on 03/11/2017.
 */

@Mixin(value = EventBus.class, remap = false)
public abstract class MixinEventBus {

    @Inject(method = "post", at = @At("HEAD"))
    private void onEventPostHead(Event event, CallbackInfoReturnable cir) {
        if (event instanceof PlayerEvent && !(event instanceof PlayerOpenContainerEvent) && !(((PlayerEvent)event).entityPlayer instanceof FakePlayer)) {
            MagiHandlers.getStack().push(((PlayerEvent)event).entityPlayer);
        }
    }

    @Inject(method = "post", at = @At("TAIL"))
    private void onEventPostTail(Event event, CallbackInfoReturnable cir) {
        if (event instanceof PlayerEvent && !(event instanceof PlayerOpenContainerEvent) && !(((PlayerEvent)event).entityPlayer instanceof FakePlayer)) {
            MagiHandlers.getStack().remove(((PlayerEvent)event).entityPlayer);
        }
    }

}
