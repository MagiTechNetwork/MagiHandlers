package net.heyzeer0.mgh.api.forge;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Frani on 06/08/2017.
 */
public interface IForgeTileEntity {

    String getMHOwner();

    void setMHOwner(String owner);

    String getMHUuid();

    void setMHUuid(String uuid);

    EntityPlayer getMHPlayer();

    default void setMHPlayer(EntityPlayer player) {
        setMHOwner(player.getCommandSenderName());
        setMHUuid(player.getUniqueID().toString());
    }

    EntityPlayer getMHPlayerReplacing();

    boolean hasMHPlayer();

    default void setMHOwner(String name, String uuid) {
        setMHOwner(name);
        setMHUuid(uuid);
    }

}
