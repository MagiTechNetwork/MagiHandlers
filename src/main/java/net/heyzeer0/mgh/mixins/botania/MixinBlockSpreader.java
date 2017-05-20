package net.heyzeer0.mgh.mixins.botania;

import net.heyzeer0.mgh.hacks.botania.IMixinTileSpreader;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import vazkii.botania.common.block.tile.mana.TileSpreader;

/**
 * Created by HeyZeer0 on 01/05/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "vazkii/botania/common/block/mana/BlockSpreader", remap = false)
public abstract class MixinBlockSpreader {
    
    @Overwrite
    public void func_149689_a(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
        int orientation = BlockPistonBase.determineOrientation(par1World, par2, par3, par4, par5EntityLivingBase);
        TileSpreader spreader = (TileSpreader) par1World.getTileEntity(par2, par3, par4);

        if ((par5EntityLivingBase instanceof EntityPlayer)) {
            ((IMixinTileSpreader) spreader).setOwner(((EntityPlayer)par5EntityLivingBase).getUniqueID());
        }

        par1World.setBlockMetadataWithNotify(par2, par3, par4, par6ItemStack.getItemDamage(), 1 | 2);

        switch(orientation) {
            case 0:
                spreader.rotationY = -90F;
                break;
            case 1:
                spreader.rotationY = 90F;
                break;
            case 2:
                spreader.rotationX = 270F;
                break;
            case 3:
                spreader.rotationX = 90F;
                break;
            case 4:
                break;
            default:
                spreader.rotationX = 180F;
                break;
        }
    }

}
