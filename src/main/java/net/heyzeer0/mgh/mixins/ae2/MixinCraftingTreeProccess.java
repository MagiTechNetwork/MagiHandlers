package net.heyzeer0.mgh.mixins.ae2;

import appeng.crafting.CraftingTreeProcess;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 01/07/2018.
 */
@Mixin(value = CraftingTreeProcess.class, remap = false)
public abstract class MixinCraftingTreeProccess {

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/common/FMLCommonHandler;firePlayerCraftingEvent(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;Lnet/minecraft/inventory/IInventory;)V"))
    private void onFireCraftingEvent(FMLCommonHandler instance, EntityPlayer player, ItemStack stack, IInventory inv) {
        if (stack == null) return;
        instance.firePlayerCraftingEvent(player, stack, inv);
    }

}
