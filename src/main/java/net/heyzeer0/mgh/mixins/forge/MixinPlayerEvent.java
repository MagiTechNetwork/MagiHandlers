package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.api.forge.ForgeStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 20/12/2017.
 */
@Mixin(PlayerEvent.class)
public abstract class MixinPlayerEvent {

    @Shadow
    @Final
    @Mutable
    public EntityPlayer entityPlayer;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstruct(EntityPlayer player, CallbackInfo ci) {
        if (MagiHandlers.isFakePlayer(player.getCommandSenderName())) {
            this.entityPlayer = ForgeStack.getStack().getCurrentEntityPlayer().orElse(player);
        }
    }

}
