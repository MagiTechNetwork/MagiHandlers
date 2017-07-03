package net.heyzeer0.mgh.hacks;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Created by Frani on 03/07/2017.
 */
public abstract class BlockHelper {

    public static boolean setBlockWithOwner(int x, int y, int z, Block block, World world, EntityPlayer owner) {
        BlockEvent.BreakEvent evt = MixinManager.generateBlockEvent(x, y, z, world, owner);
        MinecraftForge.EVENT_BUS.post(evt);
        if(!evt.isCanceled()) {
            world.setBlock(x, y, z, block);
            return true;
        }
        return false;
    }

}
