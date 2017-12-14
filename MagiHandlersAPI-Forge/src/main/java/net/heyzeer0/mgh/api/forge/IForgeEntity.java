package net.heyzeer0.mgh.api.forge;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Frani on 02/11/2017.
 */
public interface IForgeEntity {

    boolean hasOwner();

    EntityPlayer getOwner();

    void setOwner(EntityPlayer player);

}
