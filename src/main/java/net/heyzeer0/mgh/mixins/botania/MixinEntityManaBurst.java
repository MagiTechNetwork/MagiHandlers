package net.heyzeer0.mgh.mixins.botania;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.eventhandler.EventPriority;
import net.heyzeer0.mgh.EventCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.v1_7_R4.PlayerList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.block.tile.mana.TileSpreader;

import java.util.List;
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
            if(((TileSpreader)getShooter()).getIdentifier() != null) {
                Player p = Bukkit.getOfflinePlayer(((TileSpreader)getShooter()).getIdentifier()).getPlayer();

                if(p == null || !p.isOnline()) {
                    setDead();
                    ci.cancel();
                }

                BlockBreakEvent e = new BlockBreakEvent(p.getWorld().getBlockAt(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ), p);

                Bukkit.getPluginManager().callEvent(e);
                if (e.isCancelled()) {
                    setDead();
                    ci.cancel();
                }
            }
        }else if(shooterIdentity != null) {
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

    @Shadow
    public abstract TileEntity getShooter();


}
