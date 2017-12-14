package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.api.forge.IForgeEntity;
import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

/**
 * Created by Frani on 13/12/2017.
 */
@Mixin(ExplosionEvent.class)
public abstract class MixinExplosionEvent {

    @Shadow @Final public Explosion explosion;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConctructEvent(World world, Explosion exp, CallbackInfo ci) {
        Entity exploder = exp.exploder;
        if (exploder != null && exploder instanceof EntityPlayer) return;

        if (exploder != null && ((IForgeEntity) exploder).hasOwner()) {
            explosion.exploder = ((IForgeEntity) exploder).getOwner();
            return;
        }

        Optional<EntityPlayer> optionalPlayer = MagiHandlers.getStack().getFirst(EntityPlayer.class);
        if (optionalPlayer.isPresent()) {
            explosion.exploder = optionalPlayer.get();
            return;
        }

        Optional<IForgeTileEntity> optionalTile = MagiHandlers.getStack().getFirst(IForgeTileEntity.class);
        if (optionalTile.isPresent() && optionalTile.get().hasTrackedPlayer()) {
            explosion.exploder = optionalTile.get().getFakePlayer();
            return;
        }

        Optional<IForgeEntity> optionalEntity = MagiHandlers.getStack().getFirst(IForgeEntity.class);
        if (optionalEntity.isPresent() && optionalEntity.get().hasOwner()) {
            explosion.exploder = optionalEntity.get().getOwner();
            return;
        }

        MagiHandlers.log("Something is exploding without an owner, stack: ");
        Thread.dumpStack();
    }

}
