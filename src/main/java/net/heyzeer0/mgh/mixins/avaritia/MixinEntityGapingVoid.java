package net.heyzeer0.mgh.mixins.avaritia;

import net.heyzeer0.mgh.hacks.avaritia.IMixinEntityGapingVoid;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import java.util.List;
import java.util.Random;
import net.minecraft.util.Vec3;

/**
 * Created by HeyZeer0 on 10/06/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "fox/spiteful/avaritia/entity/EntityGapingVoid", remap = false)
public abstract class MixinEntityGapingVoid extends Entity implements IMixinEntityGapingVoid {

    public MixinEntityGapingVoid(World p_i1582_1_) {
        super(p_i1582_1_);
    }

    private EntityPlayer owner;

    @Override
    public void setOwner(EntityPlayer pl) {
        owner = pl;
    }

    @Override
    public EntityPlayer getOwner() {
        return owner;
    }

    @Shadow
    public static final int maxLifetime = 186;

    @Shadow
    public static double collapse;

    @Shadow
    public static double suckrange;

    @Shadow
    private static Random randy;

    @Shadow
    public static IEntitySelector sucklector;

    @Shadow
    public static IEntitySelector nomlector;

    @Overwrite
    public void func_70071_h_() {
        super.onUpdate();

        // tick, tock
        int age = this.getAge();

        if (age >= maxLifetime) {
            if(getOwner() == null) {
                setDead();
                return;
            }

            BlockEvent.BreakEvent evt = new BlockEvent.BreakEvent((int)posX, (int)posY, (int)posZ, worldObj, worldObj.getBlock((int)posX, (int)posY, (int)posZ), worldObj.getBlockMetadata((int)posX, (int)posY, (int)posZ), getOwner());
            MinecraftForge.EVENT_BUS.post(evt);

            if(evt.isCanceled()) {
                setDead();
                return;
            }

            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 6.0f, true).exploder = getOwner();
            this.setDead();
        } else {
            if (age == 0) {
                this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "Avaritia:gapingVoid", 8.0F, 1.0F);
            }
            this.setAge(age+1);
        }

        // poot poot
        double particlespeed = 4.5;

        double size = getVoidScale(age)*0.5 - 0.2;
        for(int i=0; i<50; i++) {
            Vec3 pootdir = Vec3.createVectorHelper(0, 0, size);
            pootdir.rotateAroundY(randy.nextFloat()*180f);
            pootdir.rotateAroundX(randy.nextFloat()*360f);

            Vec3 pootspeed = pootdir.normalize();
            pootspeed.xCoord *= particlespeed;
            pootspeed.yCoord *= particlespeed;
            pootspeed.zCoord *= particlespeed;

            this.worldObj.spawnParticle("portal", this.posX + pootdir.xCoord, this.posY + pootdir.yCoord, this.posZ + pootdir.zCoord, pootspeed.xCoord, pootspeed.yCoord, pootspeed.zCoord);
        }

        // *slurping noises*
        AxisAlignedBB suckzone = AxisAlignedBB.getBoundingBox(this.posX - suckrange, this.posY - suckrange, this.posZ - suckrange, this.posX + suckrange, this.posY + suckrange, this.posZ + suckrange);
        List<Entity> sucked = this.worldObj.selectEntitiesWithinAABB(Entity.class, suckzone, sucklector);

        double radius = getVoidScale(age)*0.5;

        for (Entity suckee : sucked) {
            if (suckee != this) {
                double dx = this.posX - suckee.posX;
                double dy = this.posY - suckee.posY;
                double dz = this.posZ - suckee.posZ;

                double lensquared = dx*dx + dy*dy + dz*dz;
                double len = Math.sqrt(lensquared);
                double lenn = len/suckrange;

                if (len <= suckrange) {
                    double strength = (1-lenn)*(1-lenn);
                    double power = 0.075 * radius;

                    suckee.motionX += (dx/len)*strength*power;
                    suckee.motionY += (dy/len)*strength*power;
                    suckee.motionZ += (dz/len)*strength*power;
                }
            }
        }

        // om nom nom
        double nomrange = radius*0.95;
        AxisAlignedBB nomzone = AxisAlignedBB.getBoundingBox(this.posX - nomrange, this.posY - nomrange, this.posZ - nomrange, this.posX + nomrange, this.posY + nomrange, this.posZ + nomrange);
        List<Entity> nommed = this.worldObj.selectEntitiesWithinAABB(EntityLivingBase.class, nomzone, nomlector);

        for (Entity nommee : nommed) {
            if (nommee != this) {
                double dx = this.posX - nommee.posX;
                double dy = this.posY - nommee.posY;
                double dz = this.posZ - nommee.posZ;

                double lensquared = dx*dx + dy*dy + dz*dz;
                double len = Math.sqrt(lensquared);

                if (len <= nomrange) {
                    nommee.attackEntityFrom(DamageSource.outOfWorld, 3.0f);
                }
            }
        }

        // every half second, SMASH STUFF
        if (age % 10 == 0) {
            int bx = (int) Math.floor(this.posX);
            int by = (int) Math.floor(this.posY);
            int bz = (int) Math.floor(this.posZ);

            int blockrange = (int) Math.round(nomrange);
            int lx,ly,lz;

            for (int y = -blockrange; y <= blockrange; y++) {
                for (int z = -blockrange; z <= blockrange; z++) {
                    for (int x = -blockrange; x <= blockrange; x++) {
                        lx = bx+x;
                        ly = by+y;
                        lz = bz+z;

                        if (ly < 0 || ly > 255) {
                            continue;
                        }

                        double dist = Math.sqrt(x*x+y*y+z*z);
                        if (dist <= nomrange && !this.worldObj.isAirBlock(lx, ly, lz)) {
                            Block b = this.worldObj.getBlock(lx, ly, lz);
                            int meta = this.worldObj.getBlockMetadata(lx, ly, lz);
                            float resist = b.getExplosionResistance(this, this.worldObj, lx, ly, lz, this.posX, this.posY, this.posZ);
                            if (resist <= 10.0) {
                                if(getOwner() == null) {
                                    setDead();
                                    break;
                                }

                                BlockEvent.BreakEvent evt = new BlockEvent.BreakEvent(lx, ly, lz, worldObj, b, meta, getOwner());
                                MinecraftForge.EVENT_BUS.post(evt);

                                if(evt.isCanceled()) {
                                    setDead();
                                    break;
                                }

                                b.dropBlockAsItemWithChance(worldObj, lx, ly, lz, meta, 0.9f, 0);
                                this.worldObj.setBlockToAir(lx, ly, lz);
                            }
                        }
                    }
                }
            }
        }
    }

    @Shadow
    public int getAge() {
        return 0;
    }

    @Shadow
    public static double getVoidScale(double age) {
        return 0;
    }

    @Shadow
    private void setAge(int age) {}

}
