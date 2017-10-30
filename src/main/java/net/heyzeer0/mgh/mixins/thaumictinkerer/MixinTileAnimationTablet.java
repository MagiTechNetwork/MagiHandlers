package net.heyzeer0.mgh.mixins.thaumictinkerer;

import net.heyzeer0.mgh.hacks.tt.FakeTabletFakePlayer;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.lib.FakeThaumcraftPlayer;
import thaumic.tinkerer.common.block.tile.tablet.TileAnimationTablet;

/**
 * Created by Frani on 20/10/2017.
 */
@Pseudo
@Mixin(value = TileAnimationTablet.class, remap = false)
public abstract class MixinTileAnimationTablet extends TileEntity {

    @Shadow private boolean isBreaking;
    @Shadow protected abstract void stopBreaking();

    @Inject(method = "func_145845_h", at = @At("HEAD"), cancellable = true)
    private void onEntityUpdate(CallbackInfo ci) {
        if (!this.worldObj.blockExists(this.xCoord, this.yCoord, this.zCoord) && isBreaking) {
            stopBreaking();
            ci.cancel();
        }
    }

    @Redirect(method = "func_145845_h", at = @At(value = "NEW", target = "Lthaumic/tinkerer/common/block/tile/tablet/TabletFakePlayer;<init>(Lthaumic/tinkerer/common/block/tile/tablet/TileAnimationTablet;)V"))
    private FakeThaumcraftPlayer constructFakePlayer(TileAnimationTablet tablet) {
        return new FakeTabletFakePlayer(tablet);
    }

}
