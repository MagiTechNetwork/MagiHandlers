package net.heyzeer0.mgh.mixins.forge;

import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.URL;

/**
 * Created by Frani on 16/03/2018.
 */
@Mixin(value = YggdrasilAuthenticationService.class, remap = false)
public abstract class MixinYggdrasilAuthenticationService {

    @Inject(method = "makeRequest", at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z", ordinal = 1, shift = At.Shift.AFTER), cancellable = true)
    private void onMakeRequest(URL url, Object input, Class classOfT, CallbackInfoReturnable cir) {
        cir.setReturnValue(null);
    }

}
