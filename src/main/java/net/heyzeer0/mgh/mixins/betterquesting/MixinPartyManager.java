package net.heyzeer0.mgh.mixins.betterquesting;

import betterquesting.api.questing.party.IParty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Frani on 14/08/2017.
 */

@Pseudo
@Mixin(targets = "betterquesting/questing/party/PartyManager", remap = false)
public abstract class MixinPartyManager {

    @Inject(method = "getUserParty", at = @At("HEAD"), cancellable = true)
    public void cancelGetParty(UUID uuid, CallbackInfoReturnable<IParty> cir) {
        cir.setReturnValue(null);
    }

    @Inject(method = "getPartyInvites", at = @At("HEAD"), cancellable = true)
    public void cancelGetInvites(UUID uuid, CallbackInfoReturnable<List<Integer>> cir) {
        cir.setReturnValue(new ArrayList<Integer>());
    }

    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    public void cancelGetParty(IParty party, Integer id, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

}
