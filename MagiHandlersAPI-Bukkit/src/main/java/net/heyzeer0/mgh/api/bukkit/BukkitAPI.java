package net.heyzeer0.mgh.api.bukkit;

import org.bukkit.entity.Player;

/**
 * Created by Frani on 13/01/2018.
 */
public class BukkitAPI {

    public static MagiHandlers handlers;

    public static MagiHandlers getMain() {
        return handlers;
    }

    public interface MagiHandlers {

        void closeInventory(Player p);

    }
}
