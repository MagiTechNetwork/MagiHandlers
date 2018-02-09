package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEventData;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.*;
import net.minecraft.world.storage.ISaveHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

/**
 * Created by Frani on 20/10/2017.
 */
@Mixin(WorldServer.class)
public abstract class MixinWorldServer extends World {

    public MixinWorldServer(ISaveHandler a, String b, WorldSettings c, WorldProvider d, Profiler e) {
        super(a, b, c, d, e);
    }

    private TileEntity currentTile;

    @Inject(method = "func_147485_a", at = @At("HEAD"))
    private void onSendBlockEvents(BlockEventData data, CallbackInfoReturnable<Boolean> cir) {
        TileEntity te = this.getTileEntity(data.func_151340_a(), data.func_151342_b(), data.func_151341_c());
        if (te != null) {
            this.currentTile = te;
            MagiHandlers.getStack().push(te);
        }
    }

    @Inject(method = "func_147488_Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendToAllNear(DDDDILnet/minecraft/network/Packet;)V", shift = At.Shift.AFTER))
    private void onSendToAllNear(CallbackInfo ci) {
        if (currentTile != null) {
            MagiHandlers.getStack().remove(currentTile);
            currentTile = null;
        }
    }

    @Redirect(method = "tickUpdates", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;updateTick(Lnet/minecraft/world/World;IIILjava/util/Random;)V"))
    private void updateTick(Block block, World w, int x, int y, int z, Random rand) {
        TileEntity te = w.getTileEntity(x, y, z);
        if (te != null) {
            MagiHandlers.getStack().push(te);
            block.updateTick(w, x, y, z, rand);
            MagiHandlers.getStack().remove(te);
        } else {
            MagiHandlers.getStack().ignorePhase = true;
            block.updateTick(w, x, y, z, rand);
            MagiHandlers.getStack().ignorePhase = false;
        }
    }

    @Inject(method = "func_147456_g", at = @At("HEAD"))
    private void onUpdateBlocksHead(CallbackInfo ci) {
        MagiHandlers.getStack().ignorePhase = true;
    }

    @Inject(method = "func_147485_a", at = @At("HEAD"))
    private void onUpdateBlocksReturn(CallbackInfo ci) {
        MagiHandlers.getStack().ignorePhase = false;
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/SpawnerAnimals;findChunksForSpawning(Lnet/minecraft/world/WorldServer;ZZZ)I"))
    private int redirectFindChunksForSpawningCall(SpawnerAnimals instance, WorldServer w, boolean a, boolean b, boolean c) {
        MagiHandlers.getStack().ignorePhase = true;
        instance.findChunksForSpawning(w, a, b, c);
        MagiHandlers.getStack().ignorePhase = false;
        return 0;
    }

}
