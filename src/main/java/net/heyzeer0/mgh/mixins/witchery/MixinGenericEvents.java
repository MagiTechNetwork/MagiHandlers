package net.heyzeer0.mgh.mixins.witchery;

import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.TransformCreature;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by HeyZeer0 on 13/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "com/emoniph/witchery/common/GenericEvents", remap = false)
public abstract class MixinGenericEvents {

    @Inject(method = "onEntityInteract", at = @At("HEAD"), cancellable = true)
    private void injectDamage(EntityInteractEvent event, CallbackInfo ci) {
        if(ExtendedPlayer.get(event.entityPlayer).getSelectedVampirePower() == ExtendedPlayer.VampirePower.DRINK && event.target instanceof EntityLivingBase) {
            if(!MixinManager.canAttack(event.entityPlayer, event.target)) {
                ci.cancel();
            }
        }
    }

    @SubscribeEvent
    @Overwrite
    public void onServerChat(ServerChatEvent event) {
        boolean chatMasqueradeAllowed = Config.instance().allowChatMasquerading;
        ExtendedPlayer playerEx = ExtendedPlayer.get(event.player);
        if (playerEx != null && chatMasqueradeAllowed && playerEx.getCreatureType() == TransformCreature.PLAYER && playerEx.getOtherPlayerSkin() != null && !playerEx.getOtherPlayerSkin().isEmpty()) {
            event.setCanceled(true);

            Set<Player> players = new HashSet<Player>();
            Player sender = Bukkit.getOfflinePlayer(playerEx.getOtherPlayerSkin()).getPlayer();
            players.addAll(Bukkit.getServer().getOnlinePlayers());

            AsyncPlayerChatEvent e = new AsyncPlayerChatEvent(false, sender, event.message, players);
            Bukkit.getPluginManager().callEvent(e);
        }
    }
}
