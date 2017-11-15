package net.heyzeer0.mgh.mixins.mekanism;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.Loader;
import mekanism.api.energy.IEnergizedItem;
import mekanism.common.util.ChargeUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;

/**
 * Created by Frani on 15/11/2017.
 */
@Pseudo
@Mixin(value = ChargeUtils.class)
public abstract class MixinChargeUtils {

    @Overwrite
    public static boolean canBeOutputted(ItemStack itemstack, boolean chargeSlot)
    {
        if(itemstack.getItem() instanceof IEnergizedItem)
        {
            IEnergizedItem energized = (IEnergizedItem)itemstack.getItem();

            if(chargeSlot)
            {
                return energized.getEnergy(itemstack) == energized.getMaxEnergy(itemstack);
            }
            else {
                return energized.getEnergy(itemstack) == 0;
            }
        }
        else if (itemstack.getItem() instanceof IEnergyContainerItem)
        {
            IEnergyContainerItem energyContainer = (IEnergyContainerItem)itemstack.getItem();

            if(chargeSlot)
            {
                return energyContainer.receiveEnergy(itemstack, 1, true) > 0;
            }
            else {
                return energyContainer.extractEnergy(itemstack, 1, true) > 0;
            }
        }

        return true;
    }

}
