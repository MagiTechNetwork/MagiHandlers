package net.heyzeer0.mgh.mixins.thaumcraft;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Frani on 13/06/2017.
 */

@Pseudo
@Mixin(targets = "thaumcraft/common/entities/projectile/EntityBottleTaint", remap = false)
public abstract class MixinEntityBottleTaint extends EntityThrowable {
    public MixinEntityBottleTaint(World world) {
        super(world);
    }

    @Overwrite
    protected void func_70184_a(MovingObjectPosition p_70184_1_) {
        if(!this.worldObj.isRemote) {
            List a = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ).expand(5.0D, 5.0D, 5.0D));
            if(a.size() > 0) {
                Iterator x = a.iterator();

                while(x.hasNext()) {
                    Object y = x.next();
                    EntityLivingBase z = (EntityLivingBase)y;
                    if(!(z instanceof ITaintedMob) && !z.isEntityUndead()) {
                        z.addPotionEffect(new PotionEffect(Config.potionTaintPoisonID, 100, 0, false));
                    }
                }
            }

            int var10 = (int)this.posX;
            int var11 = (int)this.posY;
            int var12 = (int)this.posZ;

            for(int a1 = 0; a1 < 10; ++a1) {
                int xx = var10 + (int)((this.rand.nextFloat() - this.rand.nextFloat()) * 5.0F);
                int zz = var12 + (int)((this.rand.nextFloat() - this.rand.nextFloat()) * 5.0F);
                if(this.worldObj.rand.nextBoolean() && this.worldObj.getBiomeGenForCoords(xx, zz) != ThaumcraftWorldGenerator.biomeTaint) {

                    BlockEvent.BreakEvent evt = new BlockEvent.BreakEvent(xx, var11, zz, worldObj, worldObj.getBlock(xx, var11, zz), worldObj.getBlockMetadata(xx, var11, zz), (EntityPlayer)getThrower());
                    MinecraftForge.EVENT_BUS.post(evt);

                    if(!evt.isCanceled()) {
                        Utils.setBiomeAt(this.worldObj, xx, zz, ThaumcraftWorldGenerator.biomeTaint);
                        if(this.worldObj.isBlockNormalCubeDefault(xx, var11 - 1, zz, false) && this.worldObj.getBlock(xx, var11, zz).isReplaceable(this.worldObj, xx, var11, zz)) {
                            this.worldObj.setBlock(xx, var11, zz, ConfigBlocks.blockTaintFibres, 0, 3);
                        }
                    }
                }
            }

            this.setDead();
        } else {
            for(int var9 = 0; var9 < Thaumcraft.proxy.particleCount(100); ++var9) {
                Thaumcraft.proxy.taintsplosionFX(this);
            }

            Thaumcraft.proxy.bottleTaintBreak(this.worldObj, this.posX, this.posY, this.posZ);
        }

    }
}
