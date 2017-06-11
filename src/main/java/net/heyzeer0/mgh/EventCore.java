package net.heyzeer0.mgh;

import com.emoniph.witchery.common.ExtendedPlayer;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.SlotUtil;
import net.lomeli.trophyslots.core.network.MessageSlotsClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

/**
 * Created by HeyZeer0 on 29/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class EventCore {

    @SubscribeEvent
    public void join(EntityJoinWorldEvent e) {
        if(e.entity instanceof EntityPlayer) {
            if(Loader.isModLoaded("trophyslots")) {
                TrophySlots.packetHandler.sendTo(new MessageSlotsClient(SlotUtil.getSlotsUnlocked((EntityPlayer)e.entity)), (EntityPlayerMP) e.entity);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void hitVampire(LivingHurtEvent e) {
        if(Loader.isModLoaded("witchery")) {
            if(e.entity instanceof EntityPlayer) {
                if(((EntityPlayer) e.entity).getHealth() <= 3) {
                    if(e.source != DamageSource.inFire && e.source != DamageSource.generic) {
                        if(ExtendedPlayer.get((EntityPlayer)e.entity).isVampire()) {
                            ((EntityPlayer) e.entity).setHealth(4);
                        }
                    }
                }
            }
        }
    }

}
