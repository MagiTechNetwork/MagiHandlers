package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.hacks.IEntity;
import net.heyzeer0.mgh.hacks.IMixinChunk;
import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Frani on 15/10/2017.
 */

@Pseudo
@Mixin(World.class)
public abstract class MixinWorld {

    @Shadow public abstract Chunk getChunkFromBlockCoords(int p_72938_1_, int p_72938_2_);

    @Redirect(method = "updateEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/TileEntity;updateEntity()V"))
    private void redirectTileEntityUpdate(TileEntity te) {
        if (te.getBlockType().getUnlocalizedName().toLowerCase().contains("ic2") ||
            te.getBlockType().getUnlocalizedName().toLowerCase().contains("appliedenergistics2")) {
            update(te);
        } else if (!((IMixinChunk)this.getChunkFromBlockCoords(te.xCoord, te.zCoord)).isMarkedToUnload()) {
            update(te);
        }
    }

    @Redirect(method = "updateEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateEntity(Lnet/minecraft/entity/Entity;)V"))
    private void redirectEntityUpdate(World world, Entity entity) {
        if (!((IMixinChunk)world.getChunkFromChunkCoords(entity.chunkCoordX, entity.chunkCoordZ)).isMarkedToUnload()) {
            update(world, entity);
        }
    }

    @Inject(method = "spawnEntityInWorld", at = @At(value = "RETURN"))
    private void onEntitySpawn1(Entity entity, CallbackInfoReturnable cir) {
        spawn(entity);
    }

    @Inject(method = "spawnEntityInWorld", at = @At(value = "RETURN", ordinal = 1))
    private void onEntitySpawn2(Entity entity, CallbackInfoReturnable cir) {
        spawn(entity);
    }

    private void update(TileEntity te) {
        MagiHandlers.getStack().push(te);
        te.updateEntity();
        MagiHandlers.getStack().remove(te);
    }

    private void update(World world, Entity entity) {
        MagiHandlers.getStack().push(entity);
        world.updateEntity(entity);
        MagiHandlers.getStack().remove(entity);
    }

    private void spawn(Entity e) {
        if (MagiHandlers.getStack().getFirst(TileEntity.class).isPresent()) {
            ((IEntity)e).setOwner(((ITileEntityOwnable)MagiHandlers.getStack().getFirst(TileEntity.class).get()).getFakePlayer());
        } else {
            MagiHandlers.getStack().getFirst(EntityPlayer.class).ifPresent(p -> ((IEntity)e).setOwner(p));
        }
    }
}
