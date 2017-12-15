package net.heyzeer0.mgh;

import com.brandon3055.draconicevolution.common.utills.DamageSourceChaos;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.heyzeer0.mgh.api.bukkit.IBukkitTileEntity;
import net.heyzeer0.mgh.api.forge.ForgeStack;
import net.heyzeer0.mgh.api.forge.IForgeEntity;
import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.heyzeer0.mgh.events.ThrowableHitEntityEvent;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.SlotUtil;
import net.lomeli.trophyslots.core.network.MessageSlotsClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Created by HeyZeer0 on 29/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class EventCore {

    //trophyslots slots fix
    @SubscribeEvent
    public void join(EntityJoinWorldEvent e) {
        if(e.entity instanceof EntityPlayer) {
            if(Loader.isModLoaded("trophyslots")) {
                TrophySlots.packetHandler.sendTo(new MessageSlotsClient(SlotUtil.getSlotsUnlocked((EntityPlayer)e.entity)), (EntityPlayerMP) e.entity);
            }
        }
    }

    //witchery vampire death fix
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void hitVampire(LivingHurtEvent e) {
        if(Loader.isModLoaded("witchery")) {
            if(e.entity instanceof EntityPlayer) {
                if(((EntityPlayer) e.entity).getHealth() <= 3) {
                    if(e.source != DamageSource.inFire && e.source != DamageSource.generic) {
                        if(Loader.isModLoaded("DraconicEvolution")) {
                            if(e.source instanceof DamageSourceChaos) return;
                        }
                        if(ExtendedPlayer.get((EntityPlayer)e.entity).isVampire()) {
                            ((EntityPlayer) e.entity).setHealth(4);
                        }
                    }
                }
            }
        }
    }

    //drawer lag fix
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onDrawerBreak(BlockEvent.BreakEvent event) {
        // Fire bukkit events
        if (MagiHandlers.isCauldron) {
            BukkitEventHandlers.checkBukkitPermission(event);
        }

        // Storage drawers logic
        if(Loader.isModLoaded("StorageDrawers") && !event.isCanceled()) {
            TileEntity tile = event.world.getTileEntity(event.x, event.y, event.z);
            if (tile != null && tile instanceof TileEntityDrawers && !event.isCanceled()) {
                TileEntityDrawers te = (TileEntityDrawers) tile;
                if(te.isSealed()) return;
                int total = 0;
                for (int i = 0; i < te.getDrawerCount(); i++) {
                    IDrawer drawer = te.getDrawer(i);
                    total += drawer.getStoredItemCount();
                }
                if (total >= 500) {
                    event.setCanceled(true);
                    event.getPlayer().addChatMessage(new ChatComponentText("§cEsse Drawer está muito cheio! Remova alguns itens §ce §ctente §cquebrar novamente!"));
                    event.getPlayer().addChatMessage(new ChatComponentText("§aDica: use uma Packing Tape para mudar o Drawer de lugar §asem §ater §aque quebra-lo!"));
                }
            }
        }
    }

    //Debugs

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRightClick(PlayerInteractEvent e) {
        // Run debug
        if(e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK
                && e.entityPlayer.getHeldItem() != null
                && e.entityPlayer.getHeldItem().getItem() == Items.arrow
                && MinecraftServer.getServer().getConfigurationManager().func_152596_g(e.entityPlayer.getGameProfile())
                && e.entityPlayer.isSneaking()) {

            TileEntity te = e.world.getTileEntity(e.x, e.y, e.z);
            if (te != null && te instanceof IForgeTileEntity) {
                e.setCanceled(true);
                e.entityPlayer.addChatComponentMessage(new ChatComponentText("Username: " + ((IForgeTileEntity) te).getOwner()));
                e.entityPlayer.addChatComponentMessage(new ChatComponentText("UUID: " + ((IForgeTileEntity) te).getUUID()));
                e.entityPlayer.addChatComponentMessage(new ChatComponentText("IBukkitEntity: " + ((IBukkitTileEntity) te).getBukkitOwner()));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLastInteract(PlayerInteractEvent event) {
        if(event.isCanceled()) {
            event.entityPlayer.closeScreen();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onHit(ThrowableHitEntityEvent event) {
        if (event.thrower != null && event.thrower instanceof EntityPlayer) {
            if (event.projectile.entityHit != null) {
                if (!MixinManager.canAttack((EntityPlayer) event.thrower, event.projectile.entityHit)) {
                    event.setCanceled(true);
                }
            } else {
                if (!MixinManager.canBuild((EntityPlayer) event.thrower, event.projectile, event.entity.worldObj)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlace(BlockEvent.PlaceEvent event) {
        if (event.isCanceled()) return;
        IForgeTileEntity tile = (IForgeTileEntity) event.world.getTileEntity(event.x, event.y, event.z);
        if (tile != null) {
            // Add tracking info
            if (!tile.hasTrackedPlayer()) {
                ForgeStack.getStack().getCurrentEntityPlayer().ifPresent(p -> tile.setPlayer(p));
            }
        } else {
            ForgeStack.getStack().getCurrentEntityPlayer().ifPresent(p -> MagiHandlers.scheduleTileCheck(p, event.world, event.x, event.y, event.z));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMultiPlace(BlockEvent.MultiPlaceEvent e) {
        onPlace(e);
        if (e.isCanceled()) return;
        for (BlockSnapshot snapshot : e.getReplacedBlockSnapshots()) {
            IForgeTileEntity tile = (IForgeTileEntity) e.world.getTileEntity(snapshot.x, snapshot.y, snapshot.z);
            if (tile != null) {
                // Add tracking info
                if (!tile.hasTrackedPlayer()) {
                    ForgeStack.getStack().getCurrentEntityPlayer().ifPresent(p -> tile.setPlayer(p));
                }
            } else {
                ForgeStack.getStack().getCurrentEntityPlayer().ifPresent(p -> MagiHandlers.scheduleTileCheck(p, snapshot.world, snapshot.x, snapshot.y, snapshot.z));
            }
        }
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent e) {
        if (e.entityPlayer.getHeldItem() != null
                && e.entityPlayer.getHeldItem().getItem() == Items.arrow
                && MinecraftServer.getServer().getConfigurationManager().func_152596_g(e.entityPlayer.getGameProfile())
                && e.entityPlayer.isSneaking()) {

            e.setCanceled(true);
            e.entityPlayer.addChatComponentMessage(new ChatComponentText("Username: " + ((IForgeEntity) e.target).getOwner().getCommandSenderName()));
            e.entityPlayer.addChatComponentMessage(new ChatComponentText("UUID: " + ((IForgeEntity) e.target).getOwner().getUniqueID().toString()));
        }
    }

}
