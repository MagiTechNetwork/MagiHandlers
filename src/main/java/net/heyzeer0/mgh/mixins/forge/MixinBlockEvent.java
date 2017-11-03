package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.hacks.IBlockEvent;
import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 18/10/2017.
 */
@Mixin(BlockEvent.class)
public abstract class MixinBlockEvent implements IBlockEvent {

    private ITileEntityOwnable breaker;

    @Override
    public ITileEntityOwnable getTile() {
        return breaker;
    }

    @Override
    public void setTile(TileEntity tile) {
        this.breaker = (ITileEntityOwnable) tile;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstruct(int a, int b, int c, World d, Block e, int f, CallbackInfo ci) {
        MagiHandlers.getStack().getFirst(TileEntity.class).ifPresent(this::setTile);
    }

    @Mixin(value = BlockEvent.BreakEvent.class, remap = false)
    public static abstract class MixinBreakEvent implements IBlockEvent {

        @Shadow @Final private EntityPlayer player;

        @Overwrite
        public EntityPlayer getPlayer() {
            if (player instanceof FakePlayer && getTile() != null) {
                return getTile().getFakePlayer();
            }
            return player;
        }
    }

    @Mixin(BlockEvent.PlaceEvent.class)
    public static abstract class MixinPlaceEvent implements IBlockEvent {

        @Shadow @Final @Mutable public EntityPlayer player;

        @Inject(method = "<init>", at = @At("RETURN"))
        private void onConstruct(BlockSnapshot blockSnapshot, Block placedAgainst, EntityPlayer p, CallbackInfo ci) {
            if (p instanceof FakePlayer && getTile() != null) {
                player = getTile().getFakePlayer();
            }
        }
    }

}
