package net.heyzeer0.mgh.mixins.enderstorage;

import codechicken.enderstorage.common.BlockEnderStorage;
import codechicken.enderstorage.common.TileFrequencyOwner;
import net.heyzeer0.mgh.hacks.enderstorage.ITileFrequencyOwner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 02/12/2017.
 */
@Pseudo
@Mixin(value = BlockEnderStorage.class, remap = false)
public abstract class MixinBlockEnderStorage {

    @Redirect(method = "func_149727_a", at = @At(value = "INVOKE", target = "Lcodechicken/enderstorage/common/TileFrequencyOwner;setOwner(Ljava/lang/String;)V"))
    private void redirectSetOwner(TileFrequencyOwner tile, String owner) {
        ((ITileFrequencyOwner) tile).setFrequencyOwner(owner);
    }

}
