package net.heyzeer0.mgh.mixins.funkylocomotion;

import com.rwtema.funkylocomotion.movers.MoveManager;
import framesapi.BlockPos;
import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Created by Frani on 13/11/2017.
 */
@Pseudo
@Mixin(value = MoveManager.class, remap = false)
public abstract class MixinMoveManager {

    @Inject(method = "startMoving", at = @At("HEAD"), cancellable = true)
    private static void onStartMoving(World world, List<BlockPos> list, ForgeDirection dir, int maxTime, CallbackInfo ci) {
        IForgeTileEntity tile = MagiHandlers.getStack().getFirst(IForgeTileEntity.class).get();
        for (BlockPos pos : list) {
            if (!MixinManager.canBuild(pos.x, pos.y, pos.z, world, tile.getFakePlayer())) {
                ci.cancel();
                return;
            }
            BlockPos pos2 = pos.advance(dir);
            if (!MixinManager.canBuild(pos2.x, pos2.y, pos2.z, world, tile.getFakePlayer())) {
                ci.cancel();
                return;
            }
        }
    }
}
