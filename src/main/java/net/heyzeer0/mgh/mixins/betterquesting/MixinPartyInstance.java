package net.heyzeer0.mgh.mixins.betterquesting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Frani on 14/08/2017.
 */

@Pseudo
@Mixin(targets = "betterquesting/questing/party/PartyInstance", remap = false)
public abstract class MixinPartyInstance {

    @Inject(method = "inviteUser", at = @At("HEAD"), cancellable = true)
    public void cancelInvite(UUID uuid, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "kickUser", at = @At("HEAD"), cancellable = true)
    public void cancelKick(UUID uuid, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "getMembers", at = @At("HEAD"), cancellable = true)
    public void cancelInvite(CallbackInfoReturnable<List<UUID>> cir) {
        cir.setReturnValue(new ArrayList<UUID>());
    }

    @Inject(method = "hostMigrate", at = @At("HEAD"), cancellable = true)
    public void cancelMigrate(CallbackInfo ci) {
        ci.cancel();
    }

}
