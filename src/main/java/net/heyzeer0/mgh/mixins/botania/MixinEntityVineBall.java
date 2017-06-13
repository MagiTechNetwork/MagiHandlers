package net.heyzeer0.mgh.mixins.botania;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

/**
 * Created by HeyZeer0 on 04/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "vazkii/botania/common/entity/EntityVineBall", remap = false)
public abstract class MixinEntityVineBall extends EntityThrowable {

    public MixinEntityVineBall(World world)
    {
        super(world);
    }

    public UUID shooterIdentity = UUID.randomUUID();

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void replaceConstructor(EntityPlayer player, boolean gravity, CallbackInfo ci) {
        shooterIdentity = player.getUniqueID();
    }

    @Inject(method = "func_70184_a", at = @At("HEAD"), cancellable = true)
    private void injectImpact(MovingObjectPosition movingobjectposition, CallbackInfo ci) {
        if(shooterIdentity != null) {
            EntityPlayer plr = worldObj.func_152378_a(shooterIdentity);

            if(plr == null) {
                setDead();
                ci.cancel();
                return;
            }

            BlockEvent.BreakEvent evt = MixinManager.generateBlockEvent(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ, worldObj, plr);
            MinecraftForge.EVENT_BUS.post(evt);

            if(evt.isCanceled()) {
                setDead();
                ci.cancel();
            }
        }
    }

}
