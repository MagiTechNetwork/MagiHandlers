package net.heyzeer0.mgh.mixins.magicalcrops;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

/**
 * Created by HeyZeer0 on 26/09/2017.
 * Copyright © HeyZeer0 - 2016
 */

@Pseudo
@Mixin(targets = "com/mark719/magicalcrops/worldgen/WorldGenEssence", remap = false)
public abstract class MixinWorldGenEssence {

    @Overwrite
    public void generate(Random random, int chunkX, int chunkZ, net.minecraft.world.World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        switch (world.provider.dimensionId)
        {
            case -100:
                generateSurface(world, random, chunkX * 16, chunkZ * 16);
                break;
            case -1:
                generateNether(world, random, chunkX * 16, chunkZ * 16);
                break;
            case 0:
                generateSurface(world, random, chunkX * 16, chunkZ * 16);
                break;
            case 1:
                generateEnd(world, random, chunkX * 16, chunkZ * 16);
        }
    }

    @Shadow
    private void generateSurface(World world, Random random, int i, int j) { }

    @Shadow
    private void generateEnd(World world, Random random, int i, int j) {}

    @Shadow
    private void generateNether(World world, Random random, int i, int j) {}

}
