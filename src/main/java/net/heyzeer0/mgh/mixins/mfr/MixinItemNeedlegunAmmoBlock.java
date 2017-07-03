package net.heyzeer0.mgh.mixins.mfr;

import net.heyzeer0.mgh.hacks.mfr.MFRHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import powercrystals.minefactoryreloaded.item.gun.ammo.ItemNeedlegunAmmoBlock;

/**
 * Created by Frani on 02/07/2017.
 */

@Pseudo
@Mixin(targets = "powercrystals/minefactoryreloaded/item/gun/ammo/ItemNeedlegunAmmoBlock", remap = false)
public abstract class MixinItemNeedlegunAmmoBlock {

    @Shadow
    protected Block _block;

    @Shadow
    protected int _blockMeta;

    @Redirect(method = "onHitBlock", at = @At(value = "INVOKE", target = "Lpowercrystals/minefactoryreloaded/item/gun/ammo/ItemNeedlegunAmmoBlock;placeBlockAt(Lnet/minecraft/world/World;IIID)V", ordinal = 0))
    private void redirectHitBlock(ItemNeedlegunAmmoBlock block, World world, int x, int y, int z, double distance, ItemStack stack, EntityPlayer owner, World world2, int x2, int y2, int z2, int side, double distance2) {
        MFRHelper.placeBlockAt(world, x, y, z, distance, owner, _block, _blockMeta);
    }

    @Redirect(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lpowercrystals/minefactoryreloaded/item/gun/ammo/ItemNeedlegunAmmoBlock;placeBlockAt(Lnet/minecraft/world/World;IIID)V", ordinal = 1))
    private void redirectHitEntity(ItemNeedlegunAmmoBlock block, World world, int x, int y, int z, double distance, ItemStack stack, EntityPlayer owner, Entity hit, double distance2) {
        MFRHelper.placeBlockAt(world, x, y, z, distance, owner, _block, _blockMeta);
    }

}
