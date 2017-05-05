package net.heyzeer0.mgh.mixins.botania;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
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
            Player p = Bukkit.getOfflinePlayer(shooterIdentity).getPlayer();

            if(p != null) {
                BlockBreakEvent e = new BlockBreakEvent(p.getWorld().getBlockAt(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ), p);

                Bukkit.getPluginManager().callEvent(e);
                if (e.isCancelled()) {
                    setDead();
                    ci.cancel();
                }
            }
        }
    }

}
