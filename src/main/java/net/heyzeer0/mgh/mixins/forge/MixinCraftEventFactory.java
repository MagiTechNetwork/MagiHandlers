package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.api.forge.ForgeStack;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_7_R4.event.CraftEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Frani on 20/12/2017.
 */
@Mixin(value = CraftEventFactory.class, remap = false)
public class MixinCraftEventFactory {

    @Inject(method = "callBlockBreakEvent", at = @At("HEAD"))
    private static void onCallBreakEvent(World world, int x, int y, int z, Block block, int meta, EntityPlayerMP player, CallbackInfoReturnable cir) {
        if (MagiHandlers.isFakePlayer(player.getCommandSenderName())) {
            player = (EntityPlayerMP) ForgeStack.getStack().getCurrentEntityPlayer().orElse(player);
        }
    }

    @Inject(method = "callBlockPlaceEvent", at = @At("HEAD"))
    private static void onCallBlockPlace(World world, EntityPlayer who, BlockState replacedBlockState, int clickedX, int clickedY, int clickedZ, CallbackInfoReturnable cir) {
        if (MagiHandlers.isFakePlayer(who.getCommandSenderName())) {
            who = ForgeStack.getStack().getCurrentEntityPlayer().orElse(who);
        }
    }

}
