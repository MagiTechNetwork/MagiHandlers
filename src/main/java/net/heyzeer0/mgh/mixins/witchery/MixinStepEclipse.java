package net.heyzeer0.mgh.mixins.witchery;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.RitualStep;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;

/**
 * Created by HeyZeer0 on 13/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "com/emoniph/witchery/ritual/rites/RiteEclipse$StepEclipse", remap = false)
public abstract class MixinStepEclipse {

    @Overwrite
    public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
        return RitualStep.Result.ABORTED_REFUND;
    }

}
