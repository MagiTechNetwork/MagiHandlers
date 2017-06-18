package net.heyzeer0.mgh.mixins.enderio;

import com.emoniph.witchery.common.ExtendedPlayer;
import cpw.mods.fml.common.Loader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 17/06/2017.
 */

@Pseudo
@Mixin(targets = "crazypants/enderio/enchantment/EnchantmentSoulBound", remap = false)
public abstract class MixinEnchantmentSoulBound {

    @Inject(method = "onPlayerDeath", at = @At("HEAD"), cancellable = true)
    private void injectPlayerDeath(PlayerDropsEvent evt, CallbackInfo ci) {
        if(Loader.isModLoaded("witchery")) {
            if(ExtendedPlayer.get((EntityPlayer)evt.entity).getWerewolfLevel() >= 1) {
                System.out.println("EH LOBISOMEM S");
                ci.cancel();
            }
        }
    }

}
