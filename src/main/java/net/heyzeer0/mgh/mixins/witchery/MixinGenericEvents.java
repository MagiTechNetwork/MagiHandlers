package net.heyzeer0.mgh.mixins.witchery;

import com.emoniph.witchery.common.ExtendedPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
            AttackEntityEvent e = new AttackEntityEvent(event.entityPlayer, event.target);
            MinecraftForge.EVENT_BUS.post(e);
            if(e.isCanceled()) {
                ci.cancel();
            }
        }
    }

}
