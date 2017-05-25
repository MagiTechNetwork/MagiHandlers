package net.heyzeer0.mgh.mixins.mekanism;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;

/**
 * Created by HeyZeer0 on 25/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "mekanism/common/recipe/BinRecipe", remap = false)
public abstract class MixinBinRecipe {

    @Overwrite
    public ItemStack getResult(IInventory inv) {
        return null;
    }

}
