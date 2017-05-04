package net.heyzeer0.mgh.hacks.botania;

import org.spongepowered.asm.mixin.Pseudo;

import java.util.UUID;

/**
 * Created by HeyZeer0 on 01/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
public interface IMixinTileSpreader {

    void setOwner(UUID u);
}
