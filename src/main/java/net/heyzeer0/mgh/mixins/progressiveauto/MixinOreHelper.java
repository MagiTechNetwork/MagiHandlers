package net.heyzeer0.mgh.mixins.progressiveauto;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Frani on 17/03/2018.
 */
@Pseudo
@Mixin(targets = "com/vanhal/progressiveautomation/util/OreHelper", remap = false)
public abstract class MixinOreHelper {

    @Inject(method = "ItemOreMatch", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private static void onReturn(ItemStack stackA, ItemStack stackB, CallbackInfoReturnable<Boolean> cir) {
        if (!ItemStack.areItemStackTagsEqual(stackA, stackB)) cir.setReturnValue(false);
    }

    @Inject(method = "ItemOreMatch", at = @At(value = "RETURN", ordinal = 3), cancellable = true)
    private static void onReturn2(ItemStack stackA, ItemStack stackB, CallbackInfoReturnable<Boolean> cir) {
        if (!ItemStack.areItemStackTagsEqual(stackA, stackB)) cir.setReturnValue(false);
    }


}
