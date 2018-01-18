package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.api.forge.ForgeStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.craftbukkit.v1_7_R4.event.CraftEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Created by Frani on 20/12/2017.
 */
@Mixin(value = CraftEventFactory.class, remap = false)
public class MixinCraftEventFactory {

    @ModifyVariable(method = "callBlockBreakEvent", at = @At("HEAD"), index = 6, argsOnly = true)
    private static EntityPlayerMP replaceBreakingPlayer(EntityPlayerMP old) {
        return MagiHandlers.isFakePlayer(old.getCommandSenderName()) ? (EntityPlayerMP) ForgeStack.getStack().getCurrentEntityPlayer().orElse(old) : old;
    }

    @ModifyVariable(method = "callBlockPlaceEvent", at = @At("HEAD"), index = 1, argsOnly = true)
    private static EntityPlayer replacePlacingPlayer(EntityPlayer who) {
        return MagiHandlers.isFakePlayer(who.getCommandSenderName()) ? ForgeStack.getStack().getCurrentEntityPlayer().orElse(who) : who;
    }

    @ModifyVariable(method = "callPlayerInteractEvent(Lnet/minecraft/entity/player/EntityPlayer;Lorg/bukkit/event/block/Action;IIIILnet/minecraft/item/ItemStack;)Lorg/bukkit/event/player/PlayerInteractEvent;", at = @At("HEAD"), index = 0, argsOnly = true)
    private static EntityPlayer replaceInteractingPlyer(EntityPlayer p) {
        return MagiHandlers.isFakePlayer(p.getCommandSenderName()) ? ForgeStack.getStack().getCurrentEntityPlayer().orElse(p) : p;
    }

    @ModifyVariable(method = "callBlockMultiPlaceEvent", at = @At("HEAD"), index = 1, argsOnly = true)
    private static EntityPlayer replaceMultiPlace(EntityPlayer p) {
        return MagiHandlers.isFakePlayer(p.getCommandSenderName()) ? ForgeStack.getStack().getCurrentEntityPlayer().orElse(p) : p;
    }

}
