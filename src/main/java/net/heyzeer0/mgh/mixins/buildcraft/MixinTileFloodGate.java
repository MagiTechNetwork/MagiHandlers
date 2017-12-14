package net.heyzeer0.mgh.mixins.buildcraft;

import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Frani on 12/08/2017.
 */

@Pseudo
@Mixin(targets = "buildcraft/factory/TileFloodGate", remap = false)
public abstract class MixinTileFloodGate extends TileEntity implements IForgeTileEntity {

    @Inject(method = "canPlaceFluidAt", at = @At("HEAD"), cancellable = true)
    public void fireBreak(Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (!MixinManager.canBuild(x, y, z, worldObj, getFakePlayer())) {
            cir.setReturnValue(false);
        }
    }

}
