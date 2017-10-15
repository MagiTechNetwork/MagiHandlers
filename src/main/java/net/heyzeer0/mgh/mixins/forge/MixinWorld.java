package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.hacks.IWorld;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 15/10/2017.
 */

@Mixin(World.class)
public abstract class MixinWorld implements IWorld {

    private TileEntity currentTile;

    @Override
    public TileEntity getCurrentTickingTile() {
        return this.currentTile;
    }

    @Redirect(method = "updateEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/TileEntity;updateEntity()V"))
    private void redirectTileEntityUpdate(TileEntity te) {
        te.updateEntity();
        this.currentTile = te;
    }

}
