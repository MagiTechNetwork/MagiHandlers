package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.hacks.IMixinChunk;
import net.heyzeer0.mgh.hacks.IMixinChunkProvider;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 15/10/2017.
 */
@Mixin(Chunk.class)
public abstract class MixinChunk implements IMixinChunk {

    @Shadow public World worldObj;

    @Shadow @Final public int xPosition;

    @Shadow @Final public int zPosition;

    @Override
    public boolean isMarkedToUnload() {
        return ((IMixinChunkProvider)worldObj.getChunkProvider())
                .getChunksToUnload()
                .contains(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(xPosition, zPosition)));
    }

    @Inject(method = "onChunkLoad", at = @At("HEAD"))
    private void head(CallbackInfo ci) {
        MagiHandlers.getStack().ignorePhase = true;
    }

    @Inject(method = "onChunkLoad", at = @At("TAIL"))
    private void tail(CallbackInfo ci) {
        MagiHandlers.getStack().ignorePhase = false;
    }

}
