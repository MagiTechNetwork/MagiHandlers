package net.heyzeer0.mgh.hacks.thaumcraft;

import net.minecraft.world.World;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.lib.world.WorldGenEldritchRing;
import thaumcraft.common.lib.world.dim.MazeThread;

/**
 * Created by HeyZeer0 on 13/06/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class ThaumcraftHelper {

    public static void generatePortal(World world, int x, int y, int z) {
        WorldGenEldritchRing gen = new WorldGenEldritchRing();
        gen.chunkX = x;
        gen.chunkZ = z;
        int w = 11 + world.rand.nextInt(6) * 2;
        int h = 11 + world.rand.nextInt(6) * 2;
        gen.width = w;
        gen.height = h;
        if (gen.generate(world, world.rand, x, y, z)) {
            System.out.println("generated!");
            ThaumcraftWorldGenerator.createRandomNodeAt(world, x, y + 2, z, world.rand, false, true, false);
            Thread t = new Thread(new MazeThread(x, z, w, h, world.rand.nextLong()));
            t.start();
        }
    }

}
