package net.heyzeer0.mgh.mixins.thaumcraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Frani on 17/06/2017.
 */

@Pseudo
@Mixin(targets = "thaumcraft/common/blocks/BlockTable", remap = false)
public abstract class MixinBlockTable {

    @Inject(method = "func_149727_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;openGui(Ljava/lang/Object;ILnet/minecraft/world/World;III)V", ordinal = 0, shift = At.Shift.BEFORE))
    private void injectOnBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are, CallbackInfoReturnable<Boolean> cir) {
        player.closeScreen();
    }

}
