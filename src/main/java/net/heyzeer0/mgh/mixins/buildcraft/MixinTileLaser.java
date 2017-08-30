package net.heyzeer0.mgh.mixins.buildcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;

/**
 * Created by HeyZeer0 on 30/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "buildcraft/silicon/TileLaser", remap = false)
public abstract class MixinTileLaser {

    @Overwrite
    protected int getMaxPowerSent() {
        return 120;
    }

}
