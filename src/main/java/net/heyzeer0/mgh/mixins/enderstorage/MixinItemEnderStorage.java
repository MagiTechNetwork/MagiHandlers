package net.heyzeer0.mgh.mixins.enderstorage;

import codechicken.enderstorage.common.ItemEnderStorage;
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
@Mixin(value = ItemEnderStorage.class, remap = false)
public abstract class MixinItemEnderStorage {

    @Redirect(method = "placeBlockAt", at = @At(value = "INVOKE", target = "Lcodechicken/enderstorage/common/TileFrequencyOwner;setMHOwner(Ljava/lang/String;)V"))
    public void onSetOwner(TileFrequencyOwner tile, String owner) {
        ((ITileFrequencyOwner) tile).setFrequencyOwner(owner);
    }

}
