package net.heyzeer0.mgh.mixins.wrcbe;

import codechicken.wirelessredstone.addons.RedstoneEtherServerAddons;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by Frani on 15/03/2018.
 */
@Mixin(value = RedstoneEtherServerAddons.class, remap = false)
public abstract class MixinRedstoneEtherServerAddons {

    private static Method mt;
    private static Constructor ct;
    @Shadow
    private HashMap playerInfos;

    @Inject(method = "deactivateRemote", at = @At("HEAD"))
    private void onDeactivateRemote(World world, EntityPlayer player, CallbackInfoReturnable<Boolean> cir) {
        try {
            if (mt == null) {
                mt = this.getClass().getDeclaredMethod("getPlayerInfo", EntityPlayer.class);
                mt.setAccessible(true);
            }
            if (ct == null) {
                ct = Class.forName("codechicken.wirelessredstone.addons.RedstoneEtherAddons$AddonPlayerInfo").getDeclaredConstructor();
                ct.setAccessible(true);
            }
            if (mt.invoke(this, player) == null) {
                this.playerInfos.put(player.getCommandSenderName(), ct.newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
