package net.heyzeer0.mgh.mixins.ic2;

import ic2.core.block.BlockMultiID;
import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Frani on 05/08/2017.
 */

@Mixin(targets = "ic2/core/block/BlockMultiID", remap = false)
public abstract class MixinBlockMultiID implements ITileEntityOwnable {

    @Shadow public abstract TileEntity getOwnTe(IBlockAccess blockAccess, int x, int y, int z);

    @Redirect(method = "func_149689_a", at = @At(value = "INVOKE", target = "Lic2/core/block/BlockMultiID;getOwnTe(Lnet/minecraft/world/IBlockAccess;III)Lnet/minecraft/tileentity/TileEntity;"))
    public TileEntity injectBlockPlace(BlockMultiID instance, IBlockAccess blockAccess, int x1, int y1, int z1, World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        TileEntity te = getOwnTe(world, x, y, z);
        if(te instanceof ITileEntityOwnable && entity instanceof EntityPlayer) {
            ((ITileEntityOwnable)te).setUUID(entity.getUniqueID().toString());
            ((ITileEntityOwnable)te).setOwner(entity.getCommandSenderName());
            return te;
        }
        return te;
    }

    @Inject(method = "func_149727_a", at = @At("HEAD"))
    public void injectOpen(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float a, float b, float c, CallbackInfoReturnable cir) {
        System.out.println("Player: " + entityPlayer.getCommandSenderName());
        System.out.println("Owner: " + (getOwnTe(world, x, y, z) instanceof ITileEntityOwnable ? ((ITileEntityOwnable)getOwnTe(world, x, y, z)).getOwner() : "No owner"));
    }

}
