package net.heyzeer0.mgh.hacks;

import org.bukkit.craftbukkit.v1_7_R4.util.LongHashSet;


/**
 * Created by Frani on 15/10/2017.
 */
public interface IMixinChunkProvider {

    LongHashSet getChunksToUnload();

}
