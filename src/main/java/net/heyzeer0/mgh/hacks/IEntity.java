package net.heyzeer0.mgh.hacks;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Frani on 02/11/2017.
 */
public interface IEntity {

    void setOwner(EntityPlayer player);
    boolean hasOwner();
    EntityPlayer getOwner();

}
