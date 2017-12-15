package net.heyzeer0.mgh.mixins;

import net.heyzeer0.mgh.api.bukkit.IBukkitEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.BlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by HeyZeer0 on 01/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class MixinManager {

    //just to organize, ignore that

    public static BlockEvent.BreakEvent generateBlockEvent(int x, int y, int z, World worldobj, EntityPlayer plr) {
        return new BlockEvent.BreakEvent(x, y, z, worldobj, worldobj.getBlock(x, y, z), worldobj.getBlockMetadata(x, y, z), plr);
    }

    public static BlockEvent.PlaceEvent generatePlaceEvent(int x, int y, int z, World world, EntityPlayer player) {
        return new BlockEvent.PlaceEvent(BlockSnapshot.getBlockSnapshot(world, x, y, z), world.getBlock(x, y, z), player);
    }

    public static boolean canAttack(EntityPlayer attacker, Entity damaged) {
        AttackEntityEvent forgeEvent = new AttackEntityEvent(attacker, damaged);
        MinecraftForge.EVENT_BUS.post(forgeEvent);
        if (forgeEvent.isCanceled()) {
            return false;
        }
        EntityDamageByEntityEvent bukkitEvent = new EntityDamageByEntityEvent(
                ((IBukkitEntity) attacker).getCraftEntity(),
                ((IBukkitEntity) damaged).getCraftEntity(),
                EntityDamageEvent.DamageCause.ENTITY_ATTACK,
                5
        );
        Bukkit.getPluginManager().callEvent(bukkitEvent);
        return !bukkitEvent.isCancelled();
    }

    public static boolean canBuild(EntityPlayer player, Object location, World world) {
        if (location instanceof MovingObjectPosition) {
            MovingObjectPosition pos = (MovingObjectPosition) location;
            return !MinecraftForge.EVENT_BUS.post(generateBlockEvent(pos.blockX, pos.blockY, pos.blockZ, world, player));
        }
        return false;
    }

    public static boolean canBuild(int x, int y, int z, World world, EntityPlayer player) {
        return !MinecraftForge.EVENT_BUS.post(generateBlockEvent(x, y, z, world, player));
    }


}
