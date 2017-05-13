package net.heyzeer0.mgh.mixins.witchery;

import net.minecraftforge.common.ForgeChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by HeyZeer0 on 13/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "com/emoniph/witchery/blocks/BlockPoppetShelf$TileEntityPoppetShelf", remap = false)
public abstract class MixinTileEntityPoppetShelf {

    @Inject(method = "forceChunkLoading", at = @At("HEAD"), cancellable = true)
    private void injectChunkloading(ForgeChunkManager.Ticket ticket, CallbackInfo ci) {
        ci.cancel();
    }

}