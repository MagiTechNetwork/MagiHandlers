package net.heyzeer0.mgh.mixins.botania;

import net.heyzeer0.mgh.hacks.BlockHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 03/07/2017.
 */

@Pseudo
@Mixin(targets = "vazkii/botania/common/item/rod/ItemTerraformRod", remap = false)
public abstract class MixinItemTerraformRod {

    @Redirect(method = "terraform", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;func_147449_b(IIILnet/minecraft/block/Block;)Z"))
    private boolean injectSetBlock(World world, int x, int y, int z, Block block, ItemStack stack, World par2world, EntityPlayer par3EntityPlayer) {
        return BlockHelper.setBlockWithOwner(x, y, z, block, world, par3EntityPlayer);
    }

}
