package net.heyzeer0.mgh;

import net.heyzeer0.mgh.api.bukkit.IBukkitEntity;
import net.minecraftforge.event.world.BlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

/**
 * Created by Frani on 12/12/2017.
 */
public class BukkitEventHandlers {

    public static void checkBukkitPermission(BlockEvent.BreakEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueID();
        Player p = Bukkit.getPlayer(playerUUID);
        if (p == null) p = (Player) ((IBukkitEntity) event.getPlayer()).getCraftEntity();
        if (p != null) {
            try {
                World world = (World) event.world.getClass().getMethod("getWorld").invoke(event.world);
                BlockBreakEvent e = new BlockBreakEvent(world.getBlockAt(event.x, event.y, event.z), p);
                Bukkit.getPluginManager().callEvent(e);
                if (e.isCancelled()) event.setCanceled(true);
            } catch (Exception e) {
            }
        }
    }

}
