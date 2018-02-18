package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.api.IMixinChunk;
import net.heyzeer0.mgh.api.forge.ForgeStack;
import net.heyzeer0.mgh.api.forge.IForgeEntity;
import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Frani on 15/10/2017.
 */

@Mixin(World.class)
public abstract class MixinWorld {

    @Shadow public abstract Chunk getChunkFromBlockCoords(int p_72938_1_, int p_72938_2_);

    @Shadow
    public abstract TileEntity getTileEntity(int p_147438_1_, int p_147438_2_, int p_147438_3_);

    @Redirect(method = "updateEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/TileEntity;updateEntity()V"))
    public void redirectTileEntityUpdate(TileEntity te) {
        if (te.getBlockType().getUnlocalizedName().toLowerCase().contains("ic2") ||
            te.getBlockType().getUnlocalizedName().toLowerCase().contains("appliedenergistics2")) {
            MagiHandlers.getStack().push(te);
            te.updateEntity();
            MagiHandlers.getStack().remove(te);
        } else if (!((IMixinChunk)this.getChunkFromBlockCoords(te.xCoord, te.zCoord)).isMarkedToUnload()) {
            MagiHandlers.getStack().push(te);
            te.updateEntity();
            MagiHandlers.getStack().remove(te);
        }
    }

    @Redirect(method = "updateEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateEntity(Lnet/minecraft/entity/Entity;)V"))
    public void redirectEntityUpdate(World world, Entity entity) {
        if (!((IMixinChunk)world.getChunkFromChunkCoords(entity.chunkCoordX, entity.chunkCoordZ)).isMarkedToUnload()) {
            MagiHandlers.getStack().push(entity);
            world.updateEntity(entity);
            MagiHandlers.getStack().remove(entity);
        }
    }

    @ModifyVariable(method = "onEntityAdded", at = @At("HEAD"), argsOnly = true)
    public Entity replaceEntity(Entity e) {
        ForgeStack.getStack().getCurrentEntityPlayer().ifPresent(((IForgeEntity) e)::setMHOwner);
        return e;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        for (Runnable r : MagiHandlers.instance.tasks) {
            r.run();
            MagiHandlers.instance.tasks.remove(r);
        }
    }

    @Inject(method = "setBlock(IIILnet/minecraft/block/Block;II)Z", at = @At("RETURN"), cancellable = true)
    private void onSetBlock(int x, int y, int z, Block newBlock, int meta, int flags, CallbackInfoReturnable<Boolean> cir) {
        if (!MagiHandlers.getStack().isIgnoringPhase()) {
            EntityPlayer owner = MagiHandlers.getStack().getCurrentEntityPlayer().orElse(null);
            if (owner != null) {
                IForgeTileEntity te = (IForgeTileEntity) this.getTileEntity(x, y, z);
                if (te != null && !te.hasMHPlayer()) {
                    te.setMHPlayer(owner);
                }
            }
        }
    }

    @Inject(method = "setBlock(IIILnet/minecraft/block/Block;II)Z", at = @At("HEAD"), cancellable = true)
    private void onSetBlockHead(int x, int y, int z, Block newBlock, int meta, int flags, CallbackInfoReturnable<Boolean> cir) {
        if (!MagiHandlers.getStack().isIgnoringPhase()) {
            EntityPlayer owner = MagiHandlers.getStack().getCurrentEntityPlayer().orElse(null);
            if (owner == null) {
                MagiHandlers.log("Something is trying to set a block without an owner, stack: ");
                Thread.dumpStack();
                return;
            }
            if (!MixinManager.canBuild(x, y, z, (World) (Object) this, owner)) {
                cir.setReturnValue(false);
            }
        }
    }

}
