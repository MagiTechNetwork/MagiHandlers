package net.heyzeer0.mgh.mixins.computercraft;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.shared.turtle.upgrades.TurtleTool;
import net.heyzeer0.mgh.hacks.cc.PermissionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Frani on 16/10/2017.
 */

@Pseudo
@Mixin(value = TurtleTool.class, remap = false)
public abstract class MixinTurtleTool {

    @Inject(method = "dig", at = @At("HEAD"), cancellable = true)
    private void onDig(ITurtleAccess turtle, int direction, CallbackInfoReturnable<TurtleCommandResult> cir) {
        if (!PermissionHelper.canTurtleBreak(turtle, direction)) {
            cir.setReturnValue(TurtleCommandResult.failure("Voce nao tem permissao para quebrar esse bloco!"));
        }
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void onAttack(ITurtleAccess turtle, int direction, CallbackInfoReturnable<TurtleCommandResult> cir) {
        if (!PermissionHelper.canTurtleAttack(turtle, direction)) {
            cir.setReturnValue(TurtleCommandResult.failure("Voce nao tem permissao para atacar aqui!"));
        }
    }

}
