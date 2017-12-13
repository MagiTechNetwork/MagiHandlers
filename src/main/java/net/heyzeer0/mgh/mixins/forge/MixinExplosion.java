package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.api.IEntity;
import net.heyzeer0.mgh.api.ITileEntityOwnable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

/**
 * Created by Frani on 04/11/2017.
 */

@Mixin(Explosion.class)
public abstract class MixinExplosion {

    @Shadow public Entity exploder;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstruct(World w, Entity e, double x, double y, double z, float size, CallbackInfo ci) {
        if (exploder != null && exploder instanceof EntityPlayer) return;
        if (e != null && e instanceof EntityPlayer) return;

        Optional<EntityPlayer> optionalPlayer = MagiHandlers.getStack().getFirst(EntityPlayer.class);
        if (optionalPlayer.isPresent()) {
            exploder = optionalPlayer.get();
            return;
        }

        Optional<ITileEntityOwnable> optionalTile = MagiHandlers.getStack().getFirst(ITileEntityOwnable.class);
        if (optionalTile.isPresent() && optionalTile.get().hasTrackedPlayer()) {
            exploder = optionalTile.get().getFakePlayer();
            return;
        }

        Optional<IEntity> optionalEntity = MagiHandlers.getStack().getFirst(IEntity.class);
        if (optionalEntity.isPresent() && optionalEntity.get().hasOwner()) {
            exploder = optionalEntity.get().getOwner();
            return;
        }

        MagiHandlers.log("Something is exploding without an owner, stack: ");
        Thread.dumpStack();
    }

}
