package net.heyzeer0.mgh.mixins.avaritia;

import fox.spiteful.avaritia.items.tools.ToolHelper;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 03/07/2017.
 */

@Pseudo
@Mixin(targets = "fox/spiteful/avaritia/items/tools/ToolHelper", remap = false)
public abstract class MixinToolHelper {

    @Redirect(method = "removeBlocksInIteration", at = @At(value = "INVOKE", target = "Lfox/spiteful/avaritia/items/tools/ToolHelper;removeBlockWithDrops(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;[Lnet/minecraft/block/material/Material;ZIFZ)V"))
    private static void redirectBlockRemoval(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, Block block, Material[] materialsListing, boolean silk, int fortune, float blockHardness, boolean dispose, EntityPlayer player2, ItemStack stack2, World world2, int x2, int y2, int z2, int xs2, int ys2, int zs2, int xe2, int ye2, int ze2, Block block2, Material[] materialsListing2, boolean silk2, int fortune2, boolean dispose2) {
        BlockEvent.BreakEvent evt = MixinManager.generateBlockEvent(x, y, z, world, player);
        MinecraftForge.EVENT_BUS.post(evt);
        if(!evt.isCanceled()) {
            ToolHelper.removeBlockWithDrops(player, stack, world, x, y, z, block, materialsListing, silk, fortune, blockHardness, dispose);
        } else {
            return;
        }
    }

    @Inject(method = "removeBlockWithDrops", at = @At("HEAD"), cancellable = true)
    private static void injectBlockRemoval(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, Block block, Material[] materialsListing, boolean silk, int fortune, float blockHardness, boolean dispose, CallbackInfo ci) {
        BlockEvent.BreakEvent evt = MixinManager.generateBlockEvent(x, y, z, world, player);
        MinecraftForge.EVENT_BUS.post(evt);
        if(evt.isCanceled()) {
            ci.cancel();
        }
    }
}
