package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.api.forge.ForgeStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
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
@Mixin(LivingEvent.class)
public abstract class MixinLivingEvent {

    @Shadow
    @Final
    @Mutable
    public EntityLivingBase entityLiving;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstruct(EntityLivingBase entity, CallbackInfo ci) {
        if (entity instanceof EntityPlayer) {
            if (MagiHandlers.isFakePlayer(entity.getCommandSenderName())) {
                this.entityLiving = ForgeStack.getStack().getCurrentEntityPlayer().orElse((EntityPlayer) entity);
            }
        }
    }

}
