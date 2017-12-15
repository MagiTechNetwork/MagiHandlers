package net.heyzeer0.mgh.api.bukkit;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Created by Frani on 13/12/2017.
 */
public interface IBukkitEntity {

    void setOwner(Player player);

    boolean hasOwner();

    Player getBukkitOwner();

    Entity getCraftEntity();

}
