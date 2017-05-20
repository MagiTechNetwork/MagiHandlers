package net.heyzeer0.mgh.mixins.extrautilities;

import com.rwtema.extrautils.item.ItemBlockMetadata;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.ItemFluidContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Created by HeyZeer0 on 20/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "com/rwtema/extrautils/item/ItemBlockDrum", remap = false)
public abstract class MixinItemBlockDrum extends ItemBlockMetadata implements IFluidContainerItem {

    public MixinItemBlockDrum(Block b) {
        super(b);
    }

    @Shadow
    public ItemFluidContainer slaveItem;

    @Overwrite
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        int to_add = resource.amount / container.stackSize;

        this.slaveItem.fill(container, new FluidStack(resource.getFluid(), (to_add > 0 ? to_add : 0)), doFill);
        return resource.amount;
    }

}
