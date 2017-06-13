package net.heyzeer0.mgh.mixins;

import net.heyzeer0.mgh.hacks.thaumcraft.IMixinTileHole;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileHole;


/**
 * Created by HeyZeer0 on 01/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class MixinManager {

    //just to organize, ignore that

    public static boolean createHole(EntityPlayer plr, World world, int ii, int jj, int kk, int side, byte count, int max) {
        Block bi = world.getBlock(ii, jj, kk);

        BlockEvent.BreakEvent evt = new BlockEvent.BreakEvent(ii, jj, kk, world, bi, world.getBlockMetadata(ii, jj,kk), plr);
        MinecraftForge.EVENT_BUS.post(evt);

        if(evt.isCanceled()) {
            return true;
        }

        if ((world.getTileEntity(ii, jj, kk) == null) && (!ThaumcraftApi.portableHoleBlackList.contains(bi)) && (bi != Blocks.bedrock) && (bi != ConfigBlocks.blockHole) && (!bi.isAir(world, ii, jj, kk)) && (!bi.canPlaceBlockAt(world, ii, jj, kk)) && (bi.getBlockHardness(world, ii, jj, kk) != -1.0F))
        {
            TileHole ts = new TileHole(bi, world.getBlockMetadata(ii, jj, kk), (short)max, count, (byte)side, null);

            ((IMixinTileHole)ts).setOwner(plr);

            world.setBlock(ii, jj, kk, Blocks.air, 0, 0);
            if (world.setBlock(ii, jj, kk, ConfigBlocks.blockHole, 0, 0)) {
                world.setTileEntity(ii, jj, kk, ts);
            }
            world.markBlockForUpdate(ii, jj, kk);
            Thaumcraft.proxy.blockSparkle(world, ii, jj, kk, 4194368, 1);
            return true; }
        return false;
    }

    public static BlockEvent.BreakEvent generateBlockEvent(int x, int y, int z, World worldobj, EntityPlayer plr) {
        return new BlockEvent.BreakEvent(x, y, z, worldobj, worldobj.getBlock(x, y, z), worldobj.getBlockMetadata(x, y, z), plr);
    }


}
