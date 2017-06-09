package net.heyzeer0.mgh.mixins.avaritia;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "fox/spiteful/avaritia/entity/EntityGapingVoid", remap = false)

public abstract class MixinBreakAvaritiaBlock {

    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockToAir(III)Z"))
    private void injectQuebrando(CallbackInfo ci){
        System.out.println("QUEBRANDOOOOO");

    }
}
