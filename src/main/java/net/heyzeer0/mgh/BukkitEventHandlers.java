package net.heyzeer0.mgh;

import net.minecraftforge.event.world.BlockEvent;
import org.bukkit.Bukkit;
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
        if (p == null) p = Bukkit.getOfflinePlayer(playerUUID).getPlayer();
        if (p != null) {
            BlockBreakEvent e = new BlockBreakEvent(p.getWorld().getBlockAt(event.x, event.y, event.z), p);
            Bukkit.getPluginManager().callEvent(e);
            if (e.isCancelled()) event.setCanceled(true);
        }
    }

}
