package net.heyzeer0.mgh.mixins.thermalexpansion;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Created by HeyZeer0 on 04/11/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "cofh/thermalexpansion/item/tool/ItemIgniter", remap = false)
public abstract class MixinItemIgniter {

    @Inject(method = "func_77659_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlock(III)Lnet/minecraft/block/Block;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void injectRightClick(ItemStack stack, World world, EntityPlayer player, CallbackInfoReturnable ci, MovingObjectPosition var4, boolean var5, int[] var6, Block var7) {
        BlockEvent.BreakEvent evt = MixinManager.generateBlockEvent(var4.blockX, var4.blockY, var4.blockZ, world, player);
        MinecraftForge.EVENT_BUS.post(evt);
        if(evt.isCanceled()) {
            ci.setReturnValue(stack);
        }
    }

}
