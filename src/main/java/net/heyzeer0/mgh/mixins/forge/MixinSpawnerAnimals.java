package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.hacks.IMixinChunk;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.HashMap;

/**
 * Created by Frani on 15/10/2017.
 */
@Mixin(SpawnerAnimals.class)
public abstract class MixinSpawnerAnimals {

    @Overwrite
    protected static ChunkPosition func_151350_a(World world, int x, int z) {
        Chunk chunk = world.getChunkFromChunkCoords(x, z);
        if (chunk == null || ((IMixinChunk)chunk).isMarkedToUnload()) return null;

        int k = x * 16 + world.rand.nextInt(16);
        int l = z * 16 + world.rand.nextInt(16);
        int i1 = world.rand.nextInt(chunk.getTopFilledSegment() + 16 - 1);
        return new ChunkPosition(k, i1, l);
    }

    @Redirect(method = "findChunksForSpawning", at = @At(value = "INVOKE", target = "Ljava/util/HashMap;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 1))
    private Object replacePut(HashMap map, ChunkCoordIntPair pair, Boolean bool, WorldServer worldServer, boolean b, boolean c, boolean d) {
        Chunk chunk = worldServer.getChunkFromBlockCoords(pair.chunkXPos, pair.chunkZPos);
        if (chunk != null || !((IMixinChunk)chunk).isMarkedToUnload()) {
            return map.put(pair, Boolean.valueOf(false));
        }
        return map.put(pair, Boolean.valueOf(true));
    }

}
