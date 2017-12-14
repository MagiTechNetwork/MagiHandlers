package net.heyzeer0.mgh.api.forge;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Frani on 06/08/2017.
 */
public interface IForgeTileEntity {

    String getOwner();

    void setOwner(String owner);

    String getUUID();

    void setUUID(String uuid);

    EntityPlayer getFakePlayer();

    EntityPlayer getFakePlayerReplacingBlock();

    void setPlayer(EntityPlayer player);

    boolean hasTrackedPlayer();

}
