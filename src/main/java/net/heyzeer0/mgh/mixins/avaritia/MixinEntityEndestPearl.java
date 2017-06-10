package net.heyzeer0.mgh.mixins.avaritia;

import fox.spiteful.avaritia.entity.EntityGapingVoid;
import net.heyzeer0.mgh.hacks.avaritia.IMixinEntityGapingVoid;
import net.heyzeer0.mgh.hacks.botania.IMixinTileSpreader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
/**
 * Created by HeyZeer0 on 10/06/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "fox/spiteful/avaritia/entity/EntityEndestPearl", remap = false)
public abstract class MixinEntityEndestPearl extends EntityThrowable {

    public MixinEntityEndestPearl(World p_i1776_1_) {
        super(p_i1776_1_);
    }

    @Overwrite
    protected void func_70184_a(MovingObjectPosition pos) {
        if (pos.entityHit != null)
        {
            pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
        }

        for (int i = 0; i < 100; ++i)
        {
            this.worldObj.spawnParticle("portal", this.posX, this.posY, this.posZ, this.rand.nextGaussian()*3, this.rand.nextGaussian()*3, this.rand.nextGaussian()*3);
        }

        if (!this.worldObj.isRemote)
        {

            Entity ent = new EntityGapingVoid(this.worldObj);
            if(ent instanceof EntityGapingVoid) {
                if(getThrower() != null && getThrower() instanceof EntityPlayer) {
                    ((IMixinEntityGapingVoid) ent).setOwner((EntityPlayer)getThrower());
                }
            }
            ForgeDirection dir = ForgeDirection.getOrientation(pos.sideHit);
            ent.setLocationAndAngles(this.posX + dir.offsetX*0.25, this.posY + dir.offsetY*0.25, this.posZ + dir.offsetZ*0.25, this.rotationYaw, 0.0F);
            this.worldObj.spawnEntityInWorld(ent);

            this.setDead();
        }
    }

}
