package net.heyzeer0.mgh.hacks.mfr;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Created by Frani on 03/07/2017.
 */
public abstract class MFRHelper {

    public static void placeBlockAt(World world, int x, int y, int z, double distance, EntityPlayer owner, Block _block, int _blockMeta) {
        Block block = world.getBlock(x, y, z);
        if (!world.isRemote && (block == null || block.isAir(world, x, y, z) || block.isReplaceable(world, x, y, z))) {
            BlockEvent.PlaceEvent evt = MixinManager.generatePlaceEvent(x, y, z, world, owner);
            MinecraftForge.EVENT_BUS.post(evt);
            if(!evt.isCanceled()) {
                world.setBlock(x, y, z, _block, _blockMeta, 3);
            }
        }
    }

}
