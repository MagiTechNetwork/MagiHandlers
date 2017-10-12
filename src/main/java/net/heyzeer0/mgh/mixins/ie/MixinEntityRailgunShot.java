package net.heyzeer0.mgh.mixins.ie;

import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Frani on 18/09/2017.
 */

@Pseudo
@Mixin(targets = "blusunrize/immersiveengineering/common/entities/EntityRailgunShot", remap = false)
public abstract class MixinEntityRailgunShot extends EntityThrowable {

    public MixinEntityRailgunShot(World w) {
        super(w);
    }

    @Inject(method = "func_70184_a", at = @At("HEAD"))
    public void checkImpact(MovingObjectPosition mop, CallbackInfo ci) {
        if (mop.entityHit != null && mop.entityHit instanceof EntityLiving && getThrower() instanceof EntityPlayer) {
            if (!MixinManager.canAttack(((EntityPlayer)getThrower()), mop.entityHit)) {
                ci.cancel();
            }
        }
    }
}
