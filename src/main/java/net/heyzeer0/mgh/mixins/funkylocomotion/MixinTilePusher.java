package net.heyzeer0.mgh.mixins.funkylocomotion;

import com.rwtema.funkylocomotion.blocks.TilePusher;
import com.rwtema.funkylocomotion.movers.MoveManager;
import framesapi.BlockPos;
import net.heyzeer0.mgh.MagiHandlers;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

/**
 * Created by Frani on 13/11/2017.
 */

@Pseudo
@Mixin(value = TilePusher.class, remap = false)
public abstract class MixinTilePusher {

    @Redirect(method = "startMoving", at = @At(value = "INVOKE", target = "Lcom/rwtema/funkylocomotion/movers/MoveManager;startMoving(Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraftforge/common/util/ForgeDirection;I)V"))
    private void redirectMove(World world, List<BlockPos> blocks, ForgeDirection dir, int time) {
        MagiHandlers.getStack().push(this);
        MoveManager.startMoving(world, blocks, dir, time);
        MagiHandlers.getStack().remove(this);
    }

}
