package net.heyzeer0.mgh.mixins.mfr;

import cofh.lib.util.position.BlockPosition;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Created by Frani on 17/06/2017.
 */

@Pseudo
@Mixin(targets = "powercrystals/minefactoryreloaded/tile/machine/TileEntityBlockPlacer", remap = false)
public abstract class MixinTileEntityBlockPlacer extends TileEntity {

    @Inject(method = "activateMachine", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isAirBlock(III)Z", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void injectActivateMachine(CallbackInfoReturnable<Boolean> cir, int var1, ItemStack stack, ItemBlock var3, Block var4, BlockPosition var5) {
        if(stack.getUnlocalizedName().equalsIgnoreCase("tile.machineblock.digitalminer") && stack.getItemDamage() == 4) {
            cir.setReturnValue(false);
        }
    }

}
