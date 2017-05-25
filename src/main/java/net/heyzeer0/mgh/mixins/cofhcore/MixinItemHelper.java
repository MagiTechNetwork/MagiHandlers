package net.heyzeer0.mgh.mixins.cofhcore;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Created by HeyZeer0 on 25/05/2017.
 * Copyright © HeyZeer0 - 2016
 */

@Pseudo
@Mixin(targets = "cofh/lib/util/helpers/ItemHelper", remap = false)
public abstract class MixinItemHelper {

    @Overwrite
    public static boolean craftingEquivalent(ItemStack paramItemStack1, ItemStack paramItemStack2, String paramString, ItemStack paramItemStack3) {
        if(paramItemStack1.hasTagCompound() && !paramItemStack2.hasTagCompound() || paramItemStack2.hasTagCompound() && !paramItemStack1.hasTagCompound()) {
            return false;
        }
        if(paramItemStack1.hasTagCompound() && paramItemStack2.hasTagCompound()) {
            if(!paramItemStack1.getTagCompound().equals(paramItemStack2.getTagCompound())) {
                return false;
            }
        }
        if (itemsEqualForCrafting(paramItemStack1, paramItemStack2)) {
            return true;
        }
        if ((paramItemStack3 != null) && (isBlacklist(paramItemStack3))) {
            return false;
        }
        if ((paramString == null) || (paramString.equals("Unknown"))) {
            return false;
        }
        return getOreName(paramItemStack1).equalsIgnoreCase(paramString);
    }

    @Shadow
    public static boolean itemsEqualForCrafting(ItemStack paramItemStack1, ItemStack paramItemStack2) {return false;}

    @Shadow
    public static boolean isBlacklist(ItemStack paramItemStack) {return false;}

    @Shadow
    public static String getOreName(ItemStack paramItemStack) {return "";}

}
