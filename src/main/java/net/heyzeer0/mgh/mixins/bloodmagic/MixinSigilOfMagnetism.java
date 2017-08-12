package net.heyzeer0.mgh.mixins.bloodmagic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 12/08/2017.
 */

@Pseudo
@Mixin(targets = "WayofTime/alchemicalWizardry/common/items/sigil/SigilOfMagnetism", remap = false)
public abstract class MixinSigilOfMagnetism {

    @Redirect(method = "func_77663_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;onCollideWithPlayer(Lnet/minecraft/entity/player/EntityPlayer;)V", ordinal = 0))
    public void redirectCollide(Entity item, ItemStack stack, World world, Entity player, int par4, boolean par5) {
        ((EntityPlayer)player).onItemPickup(item, ((EntityItem)item).getEntityItem().stackSize);
        item.setDead();
    }

    @Redirect(method = "onArmourUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;onCollideWithPlayer(Lnet/minecraft/entity/player/EntityPlayer;)V", ordinal = 0))
    public void redirectCollide(Entity item, World world, EntityPlayer player, ItemStack thisItemStack) {
        player.onItemPickup(item, ((EntityItem)item).getEntityItem().stackSize);
        item.setDead();
    }

}
