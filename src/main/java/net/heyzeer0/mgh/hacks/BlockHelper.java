package net.heyzeer0.mgh.hacks;

import cpw.mods.fml.common.FMLCommonHandler;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Created by Frani on 03/07/2017.
 */
public abstract class BlockHelper {

    public static boolean setBlockWithOwner(int x, int y, int z, Block block, World world, EntityPlayer owner) {
        BlockEvent.BreakEvent evt = MixinManager.generateBlockEvent(x, y, z, world, owner);
        MinecraftForge.EVENT_BUS.post(evt);
        if(!evt.isCanceled()) {
            return world.setBlock(x, y, z, block);
        }
        return false;
    }

    public static boolean setBlockToAirWithOwner(int x, int y, int z, World world, Entity entity) {
        EntityPlayer player = (entity instanceof EntityPlayer ? (EntityPlayer) entity : FakePlayerFactory.getMinecraft((WorldServer) world));
        BlockEvent.BreakEvent e = MixinManager.generateBlockEvent(x, y, z, world, player);
        MinecraftForge.EVENT_BUS.post(e);
        if(!e.isCanceled()) {
            return world.setBlockToAir(x, y, z);
        }
        return false;
    }

    public static void sendBlockUpdate(int x, int y, int z, World world) {
        S23PacketBlockChange packet = new S23PacketBlockChange(x, y, z, world);
        FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(packet);
    }

    public static void sendRangedBlockUpdate(int x, int y, int z, World world, int range) {
        for(int i = -range; i <= range; i++) {
            for(int j = -range; j <= range; j++) {
                for(int k = -range; k <= range; k++) {
                    if(!(world.getBlock(x + i, y + j, z + k).getMaterial() == Material.air)) {
                        S23PacketBlockChange packet = new S23PacketBlockChange(x + i, y + j, z + k, world);
                        FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(packet);
                    }
                }
            }
        }
    }

}
