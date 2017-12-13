package net.heyzeer0.mgh.mixins.computercraft;

import dan200.computercraft.shared.computer.blocks.TileComputerBase;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import net.heyzeer0.mgh.api.ITileEntityOwnable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 16/10/2017.
 */
@Pseudo
@Mixin(targets = "dan200/computercraft/shared/turtle/blocks/TileTurtle", remap = false)
public abstract class MixinTileTurtle extends TileComputerBase {

    @Inject(method = "transferStateFrom", at = @At("HEAD"))
    private void injectOnTransferState(TileTurtle copy, CallbackInfo ci) {
        ITileEntityOwnable otherTile = (ITileEntityOwnable) copy;
        if (otherTile.hasTrackedPlayer()) {
            ((ITileEntityOwnable)this).setOwner(otherTile.getOwner());
            ((ITileEntityOwnable)this).setUUID(otherTile.getUUID());
        }
    }

}
