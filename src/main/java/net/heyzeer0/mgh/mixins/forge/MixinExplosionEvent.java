package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.hacks.IEntity;
import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
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

        if (exploder != null && ((IEntity) exploder).hasOwner()) {
            explosion.exploder = ((IEntity) exploder).getOwner();
            return;
        }

        Optional<EntityPlayer> optionalPlayer = MagiHandlers.getStack().getFirst(EntityPlayer.class);
        if (optionalPlayer.isPresent()) {
            explosion.exploder = optionalPlayer.get();
            return;
        }

        Optional<ITileEntityOwnable> optionalTile = MagiHandlers.getStack().getFirst(ITileEntityOwnable.class);
        if (optionalTile.isPresent() && optionalTile.get().hasTrackedPlayer()) {
            explosion.exploder = optionalTile.get().getFakePlayer();
            return;
        }

        Optional<IEntity> optionalEntity = MagiHandlers.getStack().getFirst(IEntity.class);
        if (optionalEntity.isPresent() && optionalEntity.get().hasOwner()) {
            explosion.exploder = optionalEntity.get().getOwner();
            return;
        }

        MagiHandlers.log("Something is exploding without an owner, stack: ");
        Thread.dumpStack();
    }

}
