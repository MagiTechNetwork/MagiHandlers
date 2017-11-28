package net.heyzeer0.mgh.hacks;

import net.heyzeer0.mgh.MagiHandlers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Optional;

/**
 * Created by HeyZeer0 on 06/11/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class EntityHelper {

    public static Entity checkEntity(Entity e) {
        Optional<ITileEntityOwnable> tile = MagiHandlers.getStack().getFirst(ITileEntityOwnable.class);
        if (tile.isPresent()) {
            ((IEntity) e).setOwner(tile.get().getFakePlayer());
            return e;
        }

        Optional<EntityPlayer> player = MagiHandlers.getStack().getFirst(EntityPlayer.class);
        if (player.isPresent()) {
            ((IEntity) e).setOwner(player.get());
            return e;
        }

        Optional<IEntity> optEntity = MagiHandlers.getStack().getFirst(IEntity.class);
        if (optEntity.isPresent() && optEntity.get().hasOwner()) {
            ((IEntity) e).setOwner(optEntity.get().getOwner());
            return e;
        }

        return e;
    }

}
