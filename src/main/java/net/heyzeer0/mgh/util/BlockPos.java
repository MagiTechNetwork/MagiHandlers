package net.heyzeer0.mgh.util;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * Created by Frani on 18/03/2018.
 */
public class BlockPos {
    public int x;
    public int y;
    public int z;
    public World world;

    public BlockPos(int x, int y, int z, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public AxisAlignedBB getBox(int radius) {
        return this.getBox().expand(radius, radius, radius);
    }

    public AxisAlignedBB getBox() {
        return AxisAlignedBB.getBoundingBox(x, y, z, x, y, z);
    }
}
