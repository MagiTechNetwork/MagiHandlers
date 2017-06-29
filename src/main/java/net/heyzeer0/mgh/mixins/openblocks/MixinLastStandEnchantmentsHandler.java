package net.heyzeer0.mgh.mixins.openblocks;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import openblocks.OpenBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;

import java.util.Map;

/**
 * Created by Frani on 28/06/2017.
 */

@Pseudo
@Mixin(targets = "openblocks/enchantments/LastStandEnchantmentsHandler", remap = false)
public abstract class MixinLastStandEnchantmentsHandler {

    @Overwrite
    public static int countLastStandEnchantmentLevels(EntityPlayer player) {
        if (player != null) {
            int count = 0;
            for (ItemStack stack : player.inventory.armorInventory) {
                if (stack != null) {
                    Item item = stack.getItem();
                    if(item.getUnlocalizedName().contains("wyvern") || item.getUnlocalizedName().contains("draconic")) {
                        return 0;
                    }
                    Map<Integer, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
                    Integer ench = enchantments.get(OpenBlocks.Enchantments.lastStand.effectId);
                    if (ench != null) {
                        count += ench;
                    }
                }
            }
            return count;
        }
        return 0;
    }

}
