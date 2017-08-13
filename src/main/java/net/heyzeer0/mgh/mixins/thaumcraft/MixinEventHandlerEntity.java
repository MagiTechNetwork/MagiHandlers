package net.heyzeer0.mgh.mixins.thaumcraft;

import com.google.common.io.Files;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategoryList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.research.ResearchManager;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Frani on 13/08/2017.
 */

@Pseudo
@Mixin(targets = "thaumcraft/common/lib/events/EventHandlerEntity", remap = false)
public abstract class MixinEventHandlerEntity {

    @Shadow public abstract File getPlayerFile(String suffix, File playerDirectory, String playername);

    @Shadow public abstract File getLegacyPlayerFile(EntityPlayer player);

    @Overwrite
    @SubscribeEvent
    public void playerLoad(net.minecraftforge.event.entity.player.PlayerEvent.LoadFromFile event) {
        System.out.println("Not loading player from LoadFromFile event");
    }

    @Inject(method = "entitySpawns", at = @At("HEAD"))
    public void playerLoadOnJoin(EntityJoinWorldEvent event, CallbackInfo ci) {
        if(event.entity instanceof EntityPlayer) {
            System.out.println("[THAUMCRAFT] Loading player from EntityJoinWorldEvent");
            EntityPlayer p = (EntityPlayer)event.entity;
            Thaumcraft.proxy.getPlayerKnowledge().wipePlayerKnowledge(p.getCommandSenderName());
            File file1 = this.getPlayerFile("thaum", new File(DimensionManager.getCurrentSaveRootDirectory(), "playerdata"), p.getCommandSenderName());
            boolean legacy = false;
            if (!file1.exists()) {
                File rc = new File(new File(DimensionManager.getCurrentSaveRootDirectory(), "playerdata"), p.getUniqueID().toString() + "." + "thaum");
                File i$;
                if (rc.exists()) {
                    try {
                        Files.copy(rc, file1);
                        Thaumcraft.log.info("Using and converting UUID Thaumcraft savefile for " + p.getCommandSenderName());
                        legacy = true;
                        rc.delete();
                        i$ = new File(new File(DimensionManager.getCurrentSaveRootDirectory(), "playerdata"), p.getUniqueID().toString() + "." + "thaumback");
                        if (i$.exists()) {
                            i$.delete();
                        }
                    } catch (IOException var12) {
                        ;
                    }
                } else {
                    i$ = this.getLegacyPlayerFile(p);
                    if (i$.exists()) {
                        try {
                            Files.copy(i$, file1);
                            Thaumcraft.log.info("Using pre MC 1.7.10 Thaumcraft savefile for " + p.getCommandSenderName());
                            legacy = true;
                        } catch (IOException var11) {
                            ;
                        }
                    }
                }
            }

            ResearchManager.loadPlayerData(p, file1, this.getPlayerFile("thaumback", new File(DimensionManager.getCurrentSaveRootDirectory(), "playerdata"), p.getCommandSenderName()), legacy);
            Collection rc1 = ResearchCategories.researchCategories.values();
            Iterator i$2 = rc1.iterator();

            while (i$2.hasNext()) {
                ResearchCategoryList cat = (ResearchCategoryList) i$2.next();
                Collection res = cat.research.values();
                Iterator i$1 = res.iterator();

                while (i$1.hasNext()) {
                    ResearchItem ri = (ResearchItem) i$1.next();
                    if (ri.isAutoUnlock()) {
                        Thaumcraft.proxy.getResearchManager().completeResearch(p, ri.key);
                    }
                }
            }
        }

    }

}
