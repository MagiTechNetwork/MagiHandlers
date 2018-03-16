package net.heyzeer0.mgh.mixins.openblocks;

import net.heyzeer0.mgh.MagiHandlers;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import openblocks.api.GraveSpawnEvent;
import openmods.utils.Coord;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.lang.ref.WeakReference;

/**
 * Created by HeyZeer0 on 08/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "openblocks/common/PlayerDeathHandler$GraveCallable", remap = false)
public abstract class MixinGraveCallable {

    @Shadow
    @Final
    private WeakReference<EntityPlayer> exPlayer;

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lopenblocks/common/PlayerDeathHandler$GraveCallable;trySpawnGrave(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;)Z"))
    private void onTryPlaceGrave(CallbackInfo ci) {
        EntityPlayer p = exPlayer.get();
        MagiHandlers.getStack().push(p);
    }

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lopenblocks/common/PlayerDeathHandler$GraveCallable;trySpawnGrave(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;)Z", shift = At.Shift.AFTER))
    private void afterTryPlaceGrave(CallbackInfo ci) {
        EntityPlayer p = exPlayer.get();
        MagiHandlers.getStack().remove(p);
    }

    @Inject(method = "trySpawnGrave", at = @At(value = "RETURN", ordinal = 2, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void injectGrave(EntityPlayer mp, World world, CallbackInfoReturnable cir, Coord location, String gravestoneText, GraveSpawnEvent evt, int x, int y, int z) {
        mp.addChatMessage(new ChatComponentText("§aSua lápide foi colocada nas seguintes coordenadas:"));
        mp.addChatMessage(new ChatComponentText("§7X:§c " + x + " §7| Y:§c " + y + " §7| Z:§c " + z));
    }

    @Redirect(method = "trySpawnGrave", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;)Z"))
    private boolean injectSetBlock(World world, int x, int y, int z, Block block) {
        return world.setBlock(x, y, z, Blocks.hardened_clay);
    }

}
