package net.heyzeer0.mgh.hacks;

import net.heyzeer0.mgh.MagiHandlers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by HeyZeer0 on 04/11/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class EntityHelper {

    public static void update(TileEntity te) {
        MagiHandlers.getStack().push(te);
        te.updateEntity();
        MagiHandlers.getStack().remove(te);
    }

    public static void update(World world, Entity entity) {
        MagiHandlers.getStack().push(entity);
        world.updateEntity(entity);
        MagiHandlers.getStack().remove(entity);
    }

    public static void spawn(Entity e) {
        if (MagiHandlers.getStack().getFirst(TileEntity.class).isPresent()) {
            ((IEntity)e).setOwner(((ITileEntityOwnable)MagiHandlers.getStack().getFirst(TileEntity.class).get()).getFakePlayer());
        } else if (MagiHandlers.getStack().getFirst(EntityPlayer.class).isPresent()) {
            MagiHandlers.getStack().getFirst(EntityPlayer.class).ifPresent(((IEntity)e)::setOwner);
        } else if (MagiHandlers.getStack().getFirst(Entity.class).isPresent()) {
            MagiHandlers.getStack().getFirst(Entity.class).ifPresent(entity -> {
                if (((IEntity) entity).hasOwner()) {
                    ((IEntity) e).setOwner(((IEntity) entity).getOwner());
                }
            });
        } else if (MagiHandlers.getStack().isSpawningTick || e instanceof EntityItem || e instanceof EntityPlayer || e instanceof EntityFallingBlock) {
            // ignore, for now
        } else {
            MagiHandlers.log("Spawning entity with no owner, stacktrace: ");
            Thread.dumpStack();
        }
    }

}
