package net.heyzeer0.mgh.mixins.thermalexpansion;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 28/07/2017.
 */

@Pseudo
@Mixin(targets = "cofh/thermalexpansion/block/machine/TileAssembler", remap = false)
public abstract class MixinTileAssembler {

    @Redirect(method = "createItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/InventoryCrafting;setInventorySlotContents(ILnet/minecraft/item/ItemStack;)V"))
    public void replaceSetSlot(InventoryCrafting ic, int i, ItemStack stack) {
        if(ic.getStackInSlot(i) != null) {
            if(ic.getStackInSlot(i).hasTagCompound()) {
                return;
            }
        }
        ic.setInventorySlotContents(i, stack);
    }

}


