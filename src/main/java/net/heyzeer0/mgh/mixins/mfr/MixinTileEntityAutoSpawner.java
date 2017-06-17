package net.heyzeer0.mgh.mixins.mfr;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

/**
 * Created by Frani on 17/06/2017.
 */

@Pseudo
@Mixin(targets = "powercrystals/minefactoryreloaded/tile/machine/TileEntityAutoSpawner", remap = false)
public abstract class MixinTileEntityAutoSpawner {

    @Inject(method = "activateMachine", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntityInWorld(Lnet/minecraft/entity/Entity;)Z", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void injectActivateMachine(CallbackInfoReturnable<Boolean> cir, ItemStack var1, NBTTagCompound var2, Entity var3, EntityLivingBase var4, double var9, double var11, double var13) {
        List<Entity> l = var4.worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(var4.posX - 5, var4.posY - 5, var4.posZ - 5, var4.posX + 5, var4.posY + 5, var4.posZ + 5));
        if(l.size() >= 8) {
            cir.setReturnValue(false);
        }
    }

}
