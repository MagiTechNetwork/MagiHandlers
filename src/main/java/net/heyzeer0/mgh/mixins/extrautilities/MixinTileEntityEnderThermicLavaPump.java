package net.heyzeer0.mgh.mixins.extrautilities;

import net.minecraftforge.common.ForgeChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 13/06/2017.
 */

@Pseudo
@Mixin(targets = "com/rwtema/extrautils/tileentity/TileEntityEnderThermicLavaPump", remap = false)
public abstract class MixinTileEntityEnderThermicLavaPump {

    @Inject(method = "forceChunkLoading", at = @At("HEAD"), cancellable = true)
    private void injectChunkLoading(final ForgeChunkManager.Ticket ticket, CallbackInfo ci) {
        ci.cancel();
    }

}
