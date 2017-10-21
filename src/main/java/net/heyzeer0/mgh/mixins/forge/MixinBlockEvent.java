package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.hacks.IBlockEvent;
import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
        if (MagiHandlers.instance.phase.peekFirst() != null) {
            this.setTile(MagiHandlers.instance.phase.peekFirst());
        }
    }

    @Mixin(BlockEvent.BreakEvent.class)
    public static abstract class MixinBreakEvent implements IBlockEvent {

        @Shadow public abstract EntityPlayer getPlayer();

        @Override
        public EntityPlayer getOwner() {
            return getPlayer() instanceof FakePlayer ? getTile().getFakePlayer() : getPlayer();
        }
    }

    @Mixin(BlockEvent.PlaceEvent.class)
    public static abstract class MixinPlaceEvent implements IBlockEvent {

        @Shadow @Final public EntityPlayer player;

        @Override
        public EntityPlayer getOwner() {
            return player instanceof FakePlayer ? getTile().getFakePlayer() : player;
        }
    }

}
