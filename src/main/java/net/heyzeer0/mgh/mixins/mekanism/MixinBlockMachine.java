package net.heyzeer0.mgh.mixins.mekanism;

import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 13/08/2017.
 */

@Pseudo
@Mixin(targets = "mekanism/common/block/BlockMachine", remap = false)
public abstract class MixinBlockMachine {

    EntityPlayer owner;

    @Redirect(method = "func_149689_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getTileEntity(III)Lnet/minecraft/tileentity/TileEntity;"))
    public TileEntity redirectBlockPlacedBy(World instance, int x, int y, int z, World world, int x1, int y1, int z1, EntityLivingBase entity, ItemStack stack) {
        if(entity instanceof EntityPlayer) {
            this.owner = (EntityPlayer) entity;
        }
        TileEntity te = world.getTileEntity(x, y, z);
        if(te instanceof ITileEntityOwnable && owner != null) {
            ((ITileEntityOwnable) te).setOwner(owner.getCommandSenderName());
            ((ITileEntityOwnable) te).setUUID(owner.getUniqueID().toString());
        }
        return te;
    }

}
