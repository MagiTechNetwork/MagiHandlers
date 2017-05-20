package net.heyzeer0.mgh.mixins.tenergistics;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;

/**
 * Created by HeyZeer0 on 20/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "thaumicenergistics/common/tiles/abstraction/ThETileInventory", remap = false)
public abstract class MixinThETileInventory {

    @Overwrite
    public ItemStack func_70298_a(int slotIndex, int amount) {
        return null;
    }

}

