package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 15/10/2017.
 */
@Mixin(Block.class)
public abstract class MixinBlock {

    @Shadow public abstract boolean hasTileEntity(int metadata);

    @Shadow @Deprecated public abstract boolean hasTileEntity();

    @Inject(method = "onBlockPlacedBy", at = @At("HEAD"))
    public void onBlockPlacedByOwner(World world, int x, int y, int z, EntityLivingBase entityliving, ItemStack itemstack, CallbackInfo ci) {
        if (this.hasTileEntity(itemstack.getItemDamage()) || this.hasTileEntity()) {
            if (entityliving instanceof EntityPlayer) {
                EntityPlayer $owner = (EntityPlayer) entityliving;
                TileEntity $te = world.getTileEntity(x, y, z);
                if ($te instanceof IForgeTileEntity && !((IForgeTileEntity) $te).hasTrackedPlayer() && !($owner instanceof FakePlayer)) {
                    ((IForgeTileEntity) $te).setOwner($owner.getCommandSenderName());
                    ((IForgeTileEntity) $te).setUUID($owner.getUniqueID().toString());
                }
            }
        }
    }

}
