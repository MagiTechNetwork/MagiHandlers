package net.heyzeer0.mgh.mixins.thermalexpansion;

import cofh.thermalexpansion.block.machine.TileMachineBase;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 28/07/2017.
 */

@Pseudo
@Mixin(targets = "cofh/thermalexpansion/block/machine/TileAssembler", remap = false)
public abstract class MixinTileAssembler extends TileMachineBase {

    @Redirect(method = "createItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/InventoryCrafting;setInventorySlotContents(ILnet/minecraft/item/ItemStack;)V", ordinal = 0))
    public void replaceSetSlot(InventoryCrafting ic, int i, ItemStack stack) {
        if(ic.getStackInSlot(i) != null) {
            if(ic.getStackInSlot(i).hasTagCompound()) {
                return;
            }
        }
        ic.setInventorySlotContents(i, stack);
    }

    @Redirect(method = "createItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/InventoryCrafting;setInventorySlotContents(ILnet/minecraft/item/ItemStack;)V", ordinal = 1))
    public void replaceSetSlot1(InventoryCrafting ic, int i, ItemStack stack) {
        if(ic.getStackInSlot(i) != null) {
            if(ic.getStackInSlot(i).hasTagCompound()) {
                return;
            }
        }
        ic.setInventorySlotContents(i, stack);
    }

    @Redirect(method = "createItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/InventoryCrafting;setInventorySlotContents(ILnet/minecraft/item/ItemStack;)V", ordinal = 2))
    public void replaceSetSlot2(InventoryCrafting ic, int i, ItemStack stack) {
        if(ic.getStackInSlot(i) != null) {
            if(ic.getStackInSlot(i).hasTagCompound()) {
                return;
            }
        }
        ic.setInventorySlotContents(i, stack);
    }

    @Overwrite
    public boolean canCreate(ItemStack itemStack) {
        if (itemStack != null) {
            if (this.inventory[1] == null) {
                return true;
            }

            if(itemStack.hasTagCompound() && !this.inventory[1].hasTagCompound() || this.inventory[1].hasTagCompound() && !itemStack.hasTagCompound()) {
                return false;
            }
            if (itemStack.hasTagCompound() && this.inventory[1].hasTagCompound()) {
                if(!itemStack.getTagCompound().equals(this.inventory[1].getTagCompound())) {
                    return false;
                }
            }

            if (itemStack.isItemEqual(this.inventory[1]) && this.inventory[1].stackSize + itemStack.stackSize <= itemStack.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }


}


