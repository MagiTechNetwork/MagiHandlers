package net.heyzeer0.mgh.mixins.mekanism;

import net.heyzeer0.mgh.api.ITileEntityOwnable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 13/08/2017.
 */

@Pseudo
@Mixin(targets = "mekanism/common/block/BlockMachine", remap = false)
public abstract class MixinBlockMachine {

    @Inject(method = "func_149689_a", at = @At("HEAD"))
    public void onBlockPlacedByOwner(World world, int x, int y, int z, EntityLivingBase entityliving, ItemStack itemstack, CallbackInfo ci) {
        if (entityliving instanceof EntityPlayer) {
            EntityPlayer $owner = (EntityPlayer) entityliving;
            TileEntity $te = world.getTileEntity(x, y, z);
            if ($te instanceof ITileEntityOwnable) {
                ((ITileEntityOwnable) $te).setOwner($owner.getCommandSenderName());
                ((ITileEntityOwnable) $te).setUUID($owner.getUniqueID().toString());
            }
        }
    }
}
