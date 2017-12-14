package net.heyzeer0.mgh.hacks.funkylocomotion;

import framesapi.BlockPos;
import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frani on 13/11/2017.
 */
public class MoveHelper {

    public static List<BlockPos> checkBlocks(World world, List<BlockPos> list, ForgeDirection dir, IForgeTileEntity tile) {
        if (dir == ForgeDirection.UNKNOWN) {
            throw new IllegalArgumentException("Direction cannot be unknown.");
        }
        if (list == null) return null;
        List<BlockPos> newBlocks = list.isEmpty() ? new ArrayList<>() : new ArrayList<>(list);
        for (BlockPos blockPos : list) {
            BlockPos advance = blockPos.advance(dir);
            if (!MixinManager.canBuild(blockPos.x, blockPos.y, blockPos.z, world, tile.getFakePlayer())) newBlocks.remove(blockPos);
            if (list.contains(advance) && !MixinManager.canBuild(advance.x, advance.y, advance.z, world, tile.getFakePlayer())) newBlocks.remove(blockPos);
        }
        return newBlocks;
    }

}
