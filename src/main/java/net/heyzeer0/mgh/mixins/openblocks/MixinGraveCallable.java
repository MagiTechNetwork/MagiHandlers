package net.heyzeer0.mgh.mixins.openblocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import openblocks.api.GraveSpawnEvent;
import openmods.utils.Coord;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Created by HeyZeer0 on 08/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "openblocks/common/PlayerDeathHandler$GraveCallable", remap = false)
public abstract class MixinGraveCallable {

    @Inject(method = "trySpawnGrave", at = @At(value = "RETURN", ordinal = 2, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void injectGrave(EntityPlayer mp, World world, CallbackInfoReturnable cir, Coord location, String gravestoneText, GraveSpawnEvent evt, int x, int y, int z) {
        mp.addChatMessage(new ChatComponentText("§aSua lápide foi colocada nas seguintes coordenadas:"));
        mp.addChatMessage(new ChatComponentText("§7X:§c " + x + " §7| Y:§c " + y + " §7| Z:§c " + z));
    }

}
