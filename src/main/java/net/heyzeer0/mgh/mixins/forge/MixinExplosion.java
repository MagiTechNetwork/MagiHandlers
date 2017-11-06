package net.heyzeer0.mgh.mixins.forge;

import net.heyzeer0.mgh.MagiHandlers;
import net.heyzeer0.mgh.hacks.IEntity;
import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 04/11/2017.
 */

@Mixin(Explosion.class)
public abstract class MixinExplosion {

    @Shadow public Entity exploder;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstruct(World w, Entity e, double x, double y, double z, float size, CallbackInfo ci) {
        if (exploder == null && MagiHandlers.getStack().getFirst(ITileEntityOwnable.class).isPresent()) {
            exploder = MagiHandlers.getStack().getFirst(ITileEntityOwnable.class).get().getFakePlayer();
        }
        if ((exploder == null || !(exploder instanceof EntityPlayer)) && e != null && ((IEntity)e).hasOwner()) {
            exploder = ((IEntity)e).getOwner();
        }
    }

}
