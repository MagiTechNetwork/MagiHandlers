package net.heyzeer0.mgh.mixins;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;


/**
 * Created by HeyZeer0 on 01/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class MixinManager {

    //just to organize, ignore that

    public static BlockEvent.BreakEvent generateBlockEvent(int x, int y, int z, World worldobj, EntityPlayer plr) {
        return new BlockEvent.BreakEvent(x, y, z, worldobj, worldobj.getBlock(x, y, z), worldobj.getBlockMetadata(x, y, z), plr);
    }


}
