package net.heyzeer0.mgh.mixins.extrautilities;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.gui.SlotItemContainer;
import net.heyzeer0.mgh.MagiHandlers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Created by Frani on 29/06/2018.
 */
@Pseudo
@Mixin(SlotItemContainer.class)
public abstract class MixinSlotItemContainer extends Slot {

    @Shadow(remap = false)
    private IInventory filterInv;

    public MixinSlotItemContainer(IInventory inv, int index, int x, int y) {
        super(inv, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack i) {
        if (this.filterInv instanceof InventoryPlayer) {
            EntityPlayer p = ((InventoryPlayer) this.filterInv).player;
            if (p.getHeldItem().getItem() == ExtraUtils.goldenBag) {
                return MagiHandlers.isItemValidForBag(i);
            }
        }
        return true;
    }
}
