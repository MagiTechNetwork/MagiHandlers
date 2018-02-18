package net.heyzeer0.mgh.mixins.enderio;

import com.enderio.core.common.util.BlockCoord;
import crazypants.enderio.machine.farm.TileFarmStation;
import crazypants.enderio.machine.farm.farmers.FarmersCommune;
import crazypants.enderio.machine.farm.farmers.IHarvestResult;
import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Frani on 11/02/2018.
 */
@Mixin(value = FarmersCommune.class, remap = false)
public abstract class MixinFarmersCommune {

    @Inject(method = "harvestBlock", at = @At("HEAD"), cancellable = true)
    private void onHarvestBlock(TileFarmStation station, BlockCoord bc, Block block, int meta, CallbackInfoReturnable<IHarvestResult> cir) {
        IForgeTileEntity te = (IForgeTileEntity) station;
        if (te.hasMHPlayer() && !MixinManager.canBuild(bc.x, bc.y, bc.z, station.getWorldObj(), te.getMHPlayer())) {
            cir.setReturnValue(null);
        }
    }

}
