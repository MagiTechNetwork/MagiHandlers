package net.heyzeer0.mgh.mixins.mfr;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 13/06/2017.
 */

@Pseudo
@Mixin(targets = "powercrystals/minefactoryreloaded/tile/machine/TileEntityChunkLoader", remap = false)
public abstract class MixinTileEntityChunkLoader {

    @Inject(method = "forceChunks", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeChunkManager;forceChunk(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;Lnet/minecraft/world/ChunkCoordIntPair;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void injectForceChunks(CallbackInfo ci) {
        ci.cancel();
    }

}
