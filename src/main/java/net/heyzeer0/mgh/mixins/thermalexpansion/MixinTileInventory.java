package net.heyzeer0.mgh.mixins.thermalexpansion;

import cofh.lib.util.helpers.SecurityHelper;
import com.mojang.authlib.GameProfile;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

/**
 * Created by Frani on 02/12/2017.
 */
@Pseudo
@Mixin(targets = "cofh/thermalexpansion/block/TileInventory$1", remap = false)
public abstract class MixinTileInventory {

    @Redirect(method = "run", at = @At(value = "INVOKE", target = "Lcofh/lib/util/helpers/SecurityHelper;getProfile(Ljava/util/UUID;Ljava/lang/String;)Lcom/mojang/authlib/GameProfile;"))
    private GameProfile redirectGetProfile(UUID id, String name) {
        while (true) {
            try {
                return SecurityHelper.getProfile(id, name);
            } catch (Exception e) {
                try {
                    LogManager.getLogger().warn("Could not get profile, retrying...");
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {}
            }
        }
    }

}
