package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.hacks.IMixinChunk;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;;

/**
 * Created by Frani on 15/10/2017.
 */

@Mixin(World.class)
public abstract class MixinWorld {

    @Redirect(method = "updateEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/TileEntity;updateEntity()V"))
    private void redirectTileEntityUpdate(TileEntity te) {
        if (!((IMixinChunk)te.getWorldObj().getChunkFromBlockCoords(te.xCoord, te.zCoord)).isMarkedToUnload()) {
            MagiHandlers.instance.phase.push(te);
            te.updateEntity();
            MagiHandlers.instance.phase.pop();
        }
    }

    @Redirect(method = "updateEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateEntity(Lnet/minecraft/entity/Entity;)V"))
    private void redirectEntityUpdate(World world, Entity entity) {
        if (!((IMixinChunk)world.getChunkFromChunkCoords(entity.chunkCoordX, entity.chunkCoordZ)).isMarkedToUnload()) {
            world.updateEntity(entity);
        }
    }
}
