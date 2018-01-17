package net.heyzeer0.mgh.api.bukkit;

import org.bukkit.entity.Player;

/**
 * Created by Frani on 14/12/2017.
 */
public interface IBukkitTileEntity {

    Player getMHBukkitOwner();

    void setMHOwner(Player player);

    boolean hasMHOwner();

}
