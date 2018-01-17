package net.heyzeer0.mgh.mixins.forge;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 08/01/2018.
 */
@Mixin(value = YggdrasilMinecraftSessionService.class, remap = false)
public abstract class MixinYggdrasilMinecraftSessionService {

    @Redirect(method = "fillGameProfile", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Throwable;)V"))
    private void onWarn(Logger logger, String message, Throwable t, GameProfile profile, boolean requireSecure) {
        logger.warn("Couldn't look up properties for " + profile.getName());
    }

}
