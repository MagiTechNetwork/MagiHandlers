package net.heyzeer0.mgh.mixins.forestry;

import forestry.api.farming.IFarmHousing;
import forestry.farming.tiles.TileFarmGearbox;
import net.heyzeer0.mgh.MagiHandlers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 18/02/2018.
 */
@Mixin(value = TileFarmGearbox.class, remap = false)
public abstract class MixinTileFarmGearbox {

    @Redirect(method = "updateServer", at = @At(value = "INVOKE", target = "Lforestry/api/farming/IFarmHousing;doWork()Z"))
    private boolean redirectDoWork(IFarmHousing instance) {
        MagiHandlers.getStack().push(instance);
        boolean value = instance.doWork();
        MagiHandlers.getStack().remove(instance);
        return value;
    }

}
