package net.heyzeer0.mgh.mixins.botania;

import net.heyzeer0.mgh.hacks.botania.BotaniaHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 11/06/2017.
 */
@Mixin(targets = "vazkii/botania/common/item/ItemGrassHorn", remap = false)
public abstract class MixinItemGrassHorn {
    @Redirect(method = "onUsingTick", at = @At(value = "INVOKE", target = "Lvazkii/botania/common/item/ItemGrassHorn;breakGrass(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V"))
    private void onBreakGrass(World world, ItemStack stack, int stackDmg, int srcx, int srcy, int srcz, ItemStack stack2, EntityPlayer player, int time){
        BotaniaHelper.breakGrass(world, stack, stackDmg, srcx, srcy, srcz, player);
    }

}
