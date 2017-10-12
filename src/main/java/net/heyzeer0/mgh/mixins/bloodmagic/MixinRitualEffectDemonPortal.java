package net.heyzeer0.mgh.mixins.bloodmagic;

import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by HeyZeer0 on 25/09/2017.
 * Copyright © HeyZeer0 - 2016
 */

@Pseudo
@Mixin(targets = "WayofTime/alchemicalWizardry/common/rituals/RitualEffectDemonPortal", remap = false)
public abstract class MixinRitualEffectDemonPortal {

    @Inject(method = "startRitual", at = @At("HEAD"), cancellable = true)
    private void injectRitual(IMasterRitualStone ritualStone, EntityPlayer player, CallbackInfo ci) {
        if(ritualStone.getWorld().getProviderName().equalsIgnoreCase("world")) {
            player.addChatMessage(new ChatComponentText("§cEste ritual não pode ser realizado no overworld."));
            ci.cancel();
        }
    }

}
