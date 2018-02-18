package net.heyzeer0.mgh.mixins.thaumcraft;

import net.heyzeer0.mgh.api.forge.IForgeEntity;
import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.entities.golems.EntityGolemBase;

/**
 * Created by Frani on 10/02/2018.
 */
@Mixin(EntityGolemBase.class)
public abstract class MixinEntityGolemBase implements IForgeEntity {

    @Override
    public boolean useRealPlayer() {
        return false;
    }

}
