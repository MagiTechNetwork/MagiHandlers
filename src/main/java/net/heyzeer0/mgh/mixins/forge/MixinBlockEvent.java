package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.api.forge.ForgeStack;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 18/10/2017.
 */
public abstract class MixinBlockEvent {

    @Mixin(value = BlockEvent.BreakEvent.class, remap = false)
    public static abstract class MixinBreakEvent {

        @Shadow
        @Final
        @Mutable
        private EntityPlayer player;

        @Inject(method = "<init>", at = @At("RETURN"))
        private void onConstruct(int x, int y, int z, World world, Block block, int meta, EntityPlayer p, CallbackInfo ci) {
            if (MagiHandlers.isFakePlayer(p.getCommandSenderName())) {
                this.player = ForgeStack.getStack().getCurrentEntityPlayer().orElse(p);
            }
        }
    }

    @Mixin(BlockEvent.PlaceEvent.class)
    public static abstract class MixinPlaceEvent {

        @Shadow @Final @Mutable public EntityPlayer player;

        @Inject(method = "<init>", at = @At("RETURN"))
        private void onConstruct(BlockSnapshot blockSnapshot, Block placedAgainst, EntityPlayer p, CallbackInfo ci) {
            if (MagiHandlers.isFakePlayer(p.getCommandSenderName())) {
                this.player = ForgeStack.getStack().getCurrentEntityPlayer().orElse(p);
            }
        }
    }

}
