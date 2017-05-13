package net.heyzeer0.mgh.mixins.witchery;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by HeyZeer0 on 13/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "com/emoniph/witchery/common/ExtendedPlayer", remap = false)
public abstract class MixinExtendedPlayer {

    @Shadow
    private int vampireUltimateCharges;

    @Shadow
    EntityPlayer player;

    @Inject(method = "triggerSelectedVampirePower", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/storage/WorldInfo;setThunderTime(I)V"), cancellable = true)
    private void injectVampirePower(CallbackInfo ci) {
        player.addChatMessage(new ChatComponentText("§cDesculpe, esta habilidade encontra-se bloqueada."));
        vampireUltimateCharges -= 1;
        sync();
        ci.cancel();
    }

    @Shadow
    public void sync() {}

}
