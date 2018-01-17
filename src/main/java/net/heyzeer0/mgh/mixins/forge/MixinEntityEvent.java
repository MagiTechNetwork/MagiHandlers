package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.api.forge.ForgeStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent;
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
@Mixin(EntityEvent.class)
public abstract class MixinEntityEvent {

    @Shadow
    @Final
    @Mutable
    public Entity entity;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstruct(Entity e, CallbackInfo ci) {
        if (e instanceof EntityPlayer) {
            if (MagiHandlers.isFakePlayer(e.getCommandSenderName())) {
                this.entity = ForgeStack.getStack().getCurrentEntityPlayer().orElse((EntityPlayer) e);
            }
        }
    }

}
