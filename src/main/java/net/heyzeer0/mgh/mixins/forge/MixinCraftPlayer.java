package net.heyzeer0.mgh.mixins.forge;

import net.minecraftforge.common.util.FakePlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;

/**
 * Created by Frani on 19/01/2018.
 */
@Pseudo
@Mixin(value = CraftPlayer.class, remap = false)
public abstract class MixinCraftPlayer {

    private static Method getHandleField;

    @Shadow
    public abstract String getPlayerListName();

    @Inject(method = "sendRawMessage", at = @At("HEAD"), cancellable = true)
    private void onSendRawMessage(String message, CallbackInfo ci) {
        try {
            if (getHandleField == null) {
                getHandleField = this.getClass().getDeclaredMethod("getHandle");
            }
            Object o = getHandleField.invoke(this);
            if (o instanceof FakePlayer) {
                Player p = Bukkit.getServer().getPlayer(this.getPlayerListName());
                if (p != null) {
                    //Bukkit.getServer().getPlayer(this.getPlayerListName()).sendMessage(message);
                    p.sendMessage(message);
                    ci.cancel();
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

}
