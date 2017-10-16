package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.hacks.IMixinChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import org.bukkit.craftbukkit.v1_7_R4.util.LongHashSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Created by Frani on 15/10/2017.
 */
@Mixin(value = ChunkProviderServer.class)
public abstract class MixinChunkProviderServer implements IMixinChunkProvider {

    // Thermos changes the return of chunksToUnload from Set to LongHashSet
    @Shadow private LongHashSet chunksToUnload;

    @Override
    public LongHashSet getChunksToUnload() {
        return this.chunksToUnload;
    }

}
