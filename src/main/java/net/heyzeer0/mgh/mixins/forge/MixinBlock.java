package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.api.forge.ForgeStack;
import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
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
        if (itemstack != null && this.hasTileEntity(itemstack.getItemDamage()) || this.hasTileEntity()) {
            if (entityliving instanceof EntityPlayer) {
                EntityPlayer $owner = (EntityPlayer) entityliving;
                IForgeTileEntity $te = (IForgeTileEntity) world.getTileEntity(x, y, z);
                if ($te != null && !$te.hasMHPlayer()) {
                    if (MagiHandlers.isFakePlayer($owner.getCommandSenderName())) {
                        ForgeStack.getStack().getCurrentEntityPlayer().ifPresent($te::setMHPlayer);
                    } else {
                        $te.setMHPlayer($owner);
                    }
                }
            }
        }
    }

}
