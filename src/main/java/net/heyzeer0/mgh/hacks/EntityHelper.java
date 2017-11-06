package net.heyzeer0.mgh.hacks;

import net.heyzeer0.mgh.MagiHandlers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by HeyZeer0 on 06/11/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class EntityHelper {

    public static void checkEntity(Entity e) {
        if (MagiHandlers.getStack().getFirst(TileEntity.class).isPresent()) {
            ((IEntity)e).setOwner(((ITileEntityOwnable)MagiHandlers.getStack().getFirst(TileEntity.class).get()).getFakePlayer());
        } else if (MagiHandlers.getStack().getFirst(Entity.class).isPresent()) {
            IEntity entity = (IEntity)MagiHandlers.getStack().getFirst(Entity.class).get();
            if (entity.hasOwner()) {
                entity.setOwner(entity.getOwner());
            }
        } else if (MagiHandlers.getStack().getFirst(EntityPlayer.class).isPresent()) {
            ((IEntity)e).setOwner(MagiHandlers.getStack().getFirst(EntityPlayer.class).get());
        } else if (MagiHandlers.getStack().ignorePhase || e instanceof EntityItem || e instanceof EntityLiving || e instanceof EntityFallingBlock) {
            // ignore, for now
        }
    }

}
