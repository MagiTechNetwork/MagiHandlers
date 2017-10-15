package net.heyzeer0.mgh.mixins.thaumcraft;

import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.heyzeer0.mgh.mixins.MixinManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.TileThaumcraft;

import java.util.UUID;

/**
 * Created by Frani on 11/08/2017.
 */

@Pseudo
@Mixin(targets = "thaumcraft/common/tiles/TileArcaneBore", remap = false)
public abstract class MixinTileArcaneBore extends TileThaumcraft implements ITileEntityOwnable {

    @Shadow int digX;
    @Shadow int digY;
    @Shadow int digZ;

    @Inject(method = "dig", at = @At("HEAD"), cancellable = true)
    public void fireBreak(CallbackInfo ci) {
        if (!MixinManager.canBuild(digX, digY, digZ, worldObj, getFakePlayer())) {
            ci.cancel();
        }
    }

}
