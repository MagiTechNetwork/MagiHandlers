package net.heyzeer0.mgh.events;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.heyzeer0.mgh.api.forge.ForgeStack;
import net.heyzeer0.mgh.api.forge.IForgeEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.EntityEvent;

/**
 * Created by Frani on 14/10/2017.
 */
@Cancelable
public class ThrowableHitEntityEvent extends EntityEvent {

    public MovingObjectPosition projectile;

    public Entity entity;

    public EntityLivingBase thrower;

    public ThrowableHitEntityEvent(Entity entity, MovingObjectPosition mop, EntityLivingBase thrower) {
        super(entity);
        this.projectile = mop;
        this.entity = entity;
        this.thrower = thrower;
        if (thrower == null && entity instanceof EntityThrowable) {
            this.thrower = ((EntityThrowable)entity).getThrower();
            if (this.thrower == null) {
                if (((IForgeEntity) entity).hasOwner()) {
                    this.thrower = ((IForgeEntity) entity).getMHOwner();
                } else {
                    ForgeStack.getStack().getCurrentEntityPlayer().ifPresent(p -> this.thrower = p);
                }
            }
        }
    }
}
