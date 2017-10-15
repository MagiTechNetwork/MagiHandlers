package net.heyzeer0.mgh.mixins.extrautilities;

import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.heyzeer0.mgh.mixins.MixinManager;
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
@Mixin(targets = "com/rwtema/extrautils/tileentity/enderquarry/TileEntityEnderQuarry", remap = false)
public abstract class MixinTileEntityEnderQuarry extends TileEntity implements ITileEntityOwnable {

    @Inject(method = "mineBlock", at = @At("HEAD"), cancellable = true)
    public void fireBreak(final int x, final int y, final int z, final boolean replaceWithDirt, CallbackInfoReturnable<Boolean> cir) {
        if (!MixinManager.canBuild(x, y, z, worldObj, getFakePlayerReplacingBlock())) {
            cir.setReturnValue(false);
        }
    }

}
