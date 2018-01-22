package net.heyzeer0.mgh.mixins.thermalexpansion;

import cofh.thermalexpansion.block.device.TileActivator;
import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

/**
 * Created by Frani on 20/01/2018.
 */
@Pseudo
@Mixin(TileActivator.class)
public abstract class MixinTileActivator implements IForgeTileEntity {

    @Override
    public boolean useOriginalMHPlayer() {
        return false;
    }

}
