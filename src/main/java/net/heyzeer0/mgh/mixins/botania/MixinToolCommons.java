package net.heyzeer0.mgh.mixins.botania;

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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 03/07/2017.
 */

@Pseudo
@Mixin(targets = "vazkii/botania/common/item/equipment/tool/ToolCommons", remap = false)
public abstract class MixinToolCommons {

    @Inject(method = "removeBlockWithDrops(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;IIIIIILnet/minecraft/block/Block;[Lnet/minecraft/block/material/Material;ZIFZZ)V", at = @At(value = "HEAD"), cancellable = true)
    private static void injectBlockRemoval(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int bx, int by, int bz, Block block, Material[] materialsListing, boolean silk, int fortune, float blockHardness, boolean dispose, boolean particles, CallbackInfo ci) {
        if(player != null) {
            BlockEvent.BreakEvent evt = MixinManager.generateBlockEvent(x, y, z, world, player);
            MinecraftForge.EVENT_BUS.post(evt);
            if(evt.isCanceled()) {
                ci.cancel();
            }
        }
    }

}
