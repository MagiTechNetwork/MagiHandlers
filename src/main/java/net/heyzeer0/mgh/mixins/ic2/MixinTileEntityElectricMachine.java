package net.heyzeer0.mgh.mixins.ic2;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventBus;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
import net.heyzeer0.mgh.MagiHandlers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 18/02/2018.
 */
@Mixin(value = TileEntityElectricMachine.class, remap = false)
public abstract class MixinTileEntityElectricMachine {

    @Redirect(method = "onLoaded", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/common/eventhandler/EventBus;post(Lcpw/mods/fml/common/eventhandler/Event;)Z"))
    private boolean onPost(EventBus bus, Event toPost) {
        MagiHandlers.getStack().push(this);
        boolean r = bus.post(toPost);
        MagiHandlers.getStack().remove(this);
        return r;
    }

    @Redirect(method = "onUnloaded", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/common/eventhandler/EventBus;post(Lcpw/mods/fml/common/eventhandler/Event;)Z"))
    private boolean onPostUnload(EventBus bus, Event toPost) {
        MagiHandlers.getStack().push(this);
        boolean r = bus.post(toPost);
        MagiHandlers.getStack().remove(this);
        return r;
    }

}
