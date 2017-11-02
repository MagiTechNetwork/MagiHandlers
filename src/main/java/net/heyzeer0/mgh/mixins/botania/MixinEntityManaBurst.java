package net.heyzeer0.mgh.mixins.botania;

import com.emoniph.witchery.entity.EntitySpellEffect;
import net.heyzeer0.mgh.events.ThrowableHitEntityEvent;
import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.heyzeer0.mgh.mixins.witchery.MixinEntitySpellEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.block.tile.mana.TileSpreader;

import java.util.UUID;

/**
 * Created by HeyZeer0 on 01/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "vazkii/botania/common/entity/EntityManaBurst", remap = false)
public abstract class MixinEntityManaBurst extends EntityThrowable {

    @Shadow public abstract void setDead();

    public MixinEntityManaBurst(World world) {
        super(world);
    }

    @Shadow
    UUID shooterIdentity;

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void replaceConstructor(EntityPlayer player, CallbackInfo ci) {
        shooterIdentity = player.getUniqueID();
    }


    @Inject(method = "func_70184_a", at = @At("HEAD"), cancellable = true)
    private void replaceImpact(MovingObjectPosition movingobjectposition, CallbackInfo ci) {
        if(getShooter() instanceof TileSpreader) {
            ITileEntityOwnable owner = (ITileEntityOwnable)getShooter();
            if(owner.getUUID() != null) {
                System.out.println("uuid nao e null: " + owner.getUUID() + " | " + owner.getOwner());

                EntityPlayer plr = worldObj.func_152378_a(UUID.fromString(owner.getUUID()));

                if(plr == null) {
                    setDead();
                    ci.cancel();
                    return;
                }

                ThrowableHitEntityEvent evt = new ThrowableHitEntityEvent(this, movingobjectposition, plr);
                MinecraftForge.EVENT_BUS.post(evt);

                if(evt.isCanceled()) {
                    setDead();
                    ci.cancel();
                }
            }
        }else if(shooterIdentity != null) {
            EntityPlayer plr = worldObj.func_152378_a(shooterIdentity);

            if(plr == null) {
                setDead();
                ci.cancel();
                return;
            }

            ThrowableHitEntityEvent evt = new ThrowableHitEntityEvent(this, movingobjectposition, plr);
            MinecraftForge.EVENT_BUS.post(evt);
            if(evt.isCanceled()) {
                setDead();
                ci.cancel();
            }
        }
    }

    @Shadow
    public abstract TileEntity getShooter();

}
