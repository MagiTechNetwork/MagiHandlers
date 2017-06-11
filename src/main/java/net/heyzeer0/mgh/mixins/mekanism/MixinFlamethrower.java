package net.heyzeer0.mgh.mixins.mekanism;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 10/06/2017.
 */

@Pseudo
@Mixin(targets = "mekanism/common/entity/EntityFlame", remap = false)
public abstract class MixinFlamethrower {

    @Shadow Entity owner;

    @Inject(method = "burn", at = @At("HEAD"), cancellable = true)
    private void injectBurn(Entity entity, CallbackInfo ci) {
        AttackEntityEvent e = new AttackEntityEvent((EntityPlayer)owner, entity);
        MinecraftForge.EVENT_BUS.post(e);
        if(e.isCanceled()) {
            ci.cancel();
        }
    }

}
