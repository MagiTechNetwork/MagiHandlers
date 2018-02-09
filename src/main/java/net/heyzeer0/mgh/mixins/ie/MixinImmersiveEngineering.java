package net.heyzeer0.mgh.mixins.ie;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 31/01/2018.
 */
@Mixin(value = ImmersiveEngineering.class, remap = false)
public abstract class MixinImmersiveEngineering {

    @Inject(method = "init", at = @At("HEAD"))
    public void onCallInit(FMLInitializationEvent e, CallbackInfo ci) {
        if (ImmersiveNetHandler.INSTANCE == null) {
            ImmersiveNetHandler.INSTANCE = new ImmersiveNetHandler();
        }
    }

}
