package net.heyzeer0.mgh.mixins.progressiveauto;

import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Frani on 15/10/2017.
 */

@Pseudo
@Mixin(targets = "vanhal/progressiveautomation/entities/miner/TileMiner", remap = false)
public abstract class MixinTileMiner extends TileEntity implements IForgeTileEntity {

    @Inject(method = "canMineBlock", at = @At("HEAD"), cancellable = true)
    private void injectCanMineBlock(int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        if (!MixinManager.canBuild(x, y, z, worldObj, getFakePlayer())) {
            cir.setReturnValue(0);
        }
    }

}
