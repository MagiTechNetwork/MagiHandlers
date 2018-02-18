package net.heyzeer0.mgh.api.forge;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Frani on 02/11/2017.
 */
public interface IForgeEntity {

    boolean hasOwner();

    default EntityPlayer getMHOwner() {
        return getMHOwner(!this.useRealPlayer());
    }

    void setMHOwner(EntityPlayer player);

    EntityPlayer getMHOwner(boolean fake);

    default boolean useRealPlayer() {
        return true;
    }

}
