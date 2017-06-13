package net.heyzeer0.mgh.mixins.mekanism;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;

/**
 * Created by HeyZeer0 on 15/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "mekanism/api/util/StackUtils", remap = false)
public abstract class MixinStackUtils {

    @Overwrite
    public static boolean equalsWildcard(ItemStack wild, ItemStack check) {
        if(wild == null || check == null)
        {
            return check == wild;
        }

        if(!(wild.isStackable() && check.isStackable())) {
            return false;
        }

        return wild.getItem() == check.getItem() && (wild.getItemDamage() == OreDictionary.WILDCARD_VALUE || wild.getItemDamage() == check.getItemDamage());
    }

    @Overwrite
    public static boolean diffIgnoreNull(ItemStack stack1, ItemStack stack2)
    {
        if ((stack1 == null) || (stack2 == null))
        {
            return false;
        }

        //não me pergunte o porquê + o método esta ao contrario /shrug, existe a possibilidade de isto causar outro dupe, mais acho q n
        if(stack1.hasTagCompound() && !stack2.hasTagCompound() || stack2.hasTagCompound() && !stack1.hasTagCompound()) {
            return true;
        }


        //não me pergunte o porquê + o método esta ao contrario /shrug, existe a possibilidade de isto causar outro dupe, mais acho q n
        if(stack1.hasTagCompound() && stack2.hasTagCompound()) {
            if(!stack1.getTagCompound().equals(stack2.getTagCompound())) {
                return true;
            }
        }

        return (stack1.getItem() != stack2.getItem()) || (stack1.getItemDamage() != stack2.getItemDamage());
    }

}