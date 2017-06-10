package net.heyzeer0.mgh.hacks.avaritia;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by HeyZeer0 on 10/06/2017.
 * Copyright © HeyZeer0 - 2016
 */
public interface IMixinEntityGapingVoid {

    void setOwner(EntityPlayer pl);
    EntityPlayer getOwner();

}
