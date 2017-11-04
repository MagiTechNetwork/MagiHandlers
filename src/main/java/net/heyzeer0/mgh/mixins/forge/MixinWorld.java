package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.hacks.EntityHelper;
import net.heyzeer0.mgh.hacks.IMixinChunk;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 15/10/2017.
 */

@Mixin(World.class)
public abstract class MixinWorld {

    @Shadow public abstract Chunk getChunkFromBlockCoords(int p_72938_1_, int p_72938_2_);

    @Redirect(method = "updateEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/TileEntity;updateEntity()V"))
    private void redirectTileEntityUpdate(TileEntity te) {
        if (te.getBlockType().getUnlocalizedName().toLowerCase().contains("ic2") ||
            te.getBlockType().getUnlocalizedName().toLowerCase().contains("appliedenergistics2")) {
            EntityHelper.update(te);
        } else if (!((IMixinChunk)this.getChunkFromBlockCoords(te.xCoord, te.zCoord)).isMarkedToUnload()) {
            EntityHelper.update(te);
        }
    }

    @Redirect(method = "updateEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateEntity(Lnet/minecraft/entity/Entity;)V"))
    private void redirectEntityUpdate(World world, Entity entity) {
        if (!((IMixinChunk)world.getChunkFromChunkCoords(entity.chunkCoordX, entity.chunkCoordZ)).isMarkedToUnload()) {
            EntityHelper.update(world, entity);
        }
    }

    @Inject(method = "onEntityAdded", at = @At(value = "HEAD"))
    private void onEntitySpawn1(Entity entity, CallbackInfo cir) {
        EntityHelper.spawn(entity);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        for (Runnable r : MagiHandlers.instance.tasks) {
            r.run();
            MagiHandlers.instance.tasks.remove(r);
        }
    }

}
