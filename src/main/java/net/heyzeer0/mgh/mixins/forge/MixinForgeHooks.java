package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Frani on 03/11/2017.
 */

@Mixin(value = ForgeHooks.class, remap = false)
public abstract class MixinForgeHooks {

    @Inject(method = "onPlaceItemIntoWorld", at = @At("HEAD"))
    private static void handlePlaceInit(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, CallbackInfoReturnable cir) {
        MagiHandlers.getStack().push(player);
    }

    @Inject(method = "onPlaceItemIntoWorld", at = @At("RETURN"))
    private static void handlePlaceEnd(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, CallbackInfoReturnable cir) {
        MagiHandlers.getStack().remove(player);
    }

}
