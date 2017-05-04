package net.heyzeer0.mgh.mixins.botania;

import net.heyzeer0.mgh.hacks.botania.IMixinTileSpreader;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vazkii.botania.common.block.tile.mana.TileSpreader;

/**
 * Created by HeyZeer0 on 01/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "vazkii/botania/common/block/mana/BlockSpreader", remap = false)
public class MixinBlockSpreader {

    @Inject(method = "func_149689_a", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onBlockPlacedByReturn(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack, TileSpreader spreader, CallbackInfo cl) {
        if ((par5EntityLivingBase instanceof EntityPlayer)) {
            ((IMixinTileSpreader) spreader).setOwner(((EntityPlayer)par5EntityLivingBase).getUniqueID());
        }
    }

}
