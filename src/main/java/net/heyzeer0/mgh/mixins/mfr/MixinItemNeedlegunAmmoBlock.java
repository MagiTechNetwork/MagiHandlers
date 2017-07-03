package net.heyzeer0.mgh.mixins.mfr;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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

    @Redirect(method = "onHitBlock", at = @At(value = "INVOKE", target = "Lpowercrystals/minefactoryreloaded/item/gun/ammo/ItemNeedlegunAmmoBlock;placeBlockAt(Lnet/minecraft/world/World;IIID)V"))
    private void redirectHitBlock(World world, int x, int y, int z, double distance, ItemStack stack, EntityPlayer owner, World world2, int x2, int y2, int z2, int side, double distance2) {
        placeBlockAt(world, x, y, z, distance, owner);
    }

    @Redirect(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lpowercrystals/minefactoryreloaded/item/gun/ammo/ItemNeedlegunAmmoBlock;placeBlockAt(Lnet/minecraft/world/World;IIID)V"))
    private void redirectHitEntity(World world, int x, int y, int z, double distance, ItemStack stack, EntityPlayer owner, Entity hit, double distance2) {
        placeBlockAt(world, x, y, z, distance, owner);
    }

    protected void placeBlockAt(World world, int x, int y, int z, double distance, EntityPlayer owner) {
        Block block = world.getBlock(x, y, z);
        if (!world.isRemote && (block == null || block.isAir(world, x, y, z) || block.isReplaceable(world, x, y, z))) {
            BlockEvent.PlaceEvent evt = MixinManager.generatePlaceEvent(x, y, z, world, owner);
            MinecraftForge.EVENT_BUS.post(evt);
            if(!evt.isCanceled()) {
                world.setBlock(x, y, z, _block, _blockMeta, 3);
            }
        }
    }

}
