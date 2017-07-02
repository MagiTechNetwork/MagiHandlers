package net.heyzeer0.mgh.mixins.bloodmagic;

import com.google.common.collect.HashMultiset;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Created by Frani on 02/07/2017.
 */

@Pseudo
@Mixin(targets = "WayofTime/alchemicalWizardry/common/items/BoundAxe", remap = false)
public abstract class MixinBoundAxe {

    @Inject(method = "func_77659_a",  at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;func_147468_f(III)Z"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void injectOnItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, CallbackInfoReturnable<ItemStack> cir, Vec3 blockVec, int posX, int posY, int posZ, boolean silkTouch, int fortuneLvl, HashMultiset dropMultiset, int i, int j, int k, Block block, int meta, float str) {
        BlockEvent.BreakEvent evt = MixinManager.generateBlockEvent(posX + i, posY + j, posZ + k, par2World, par3EntityPlayer);
        MinecraftForge.EVENT_BUS.post(evt);
        if(evt.isCanceled()) {
            cir.setReturnValue(par1ItemStack);
        }
    }
}
