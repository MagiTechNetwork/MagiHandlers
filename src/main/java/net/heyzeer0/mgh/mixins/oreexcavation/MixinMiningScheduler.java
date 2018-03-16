package net.heyzeer0.mgh.mixins.oreexcavation;

import net.heyzeer0.mgh.MagiHandlers;
import oreexcavation.handlers.MiningAgent;
import oreexcavation.handlers.MiningScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 19/02/2018.
 */
@Mixin(value = MiningScheduler.class, remap = false)
public abstract class MixinMiningScheduler {

    @Redirect(method = "tickAgents", at = @At(value = "INVOKE", target = "Loreexcavation/handlers/MiningAgent;tickMiner()Z"))
    private boolean onTickMiner(MiningAgent agent) {
        MagiHandlers.getStack().push(agent.player);
        boolean r = agent.tickMiner();
        MagiHandlers.getStack().remove(agent.player);
        return r;
    }

}
