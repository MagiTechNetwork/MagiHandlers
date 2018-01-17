package net.heyzeer0.mgh.hacks;

import net.heyzeer0.mgh.api.forge.ForgeStack;
import net.heyzeer0.mgh.api.forge.IForgeEntity;
import net.minecraft.entity.Entity;

/**
 * Created by HeyZeer0 on 06/11/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class EntityHelper {

    public static Entity checkEntity(Entity e) {
        ForgeStack.getStack().getCurrentEntityPlayer().ifPresent(((IForgeEntity) e)::setOwner);
        return e;
    }

}
